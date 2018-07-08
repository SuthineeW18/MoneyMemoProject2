package com.moneymemo.adminpc.moneymemo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.moneymemo.adminpc.moneymemo.model.ExpensecateData;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ExpensecateActivity extends AppCompatActivity {

    private EditText txtExpCname;
    private Button btnAddExpCate;
    private ListView listExpCate;
    private ArrayList<ExpensecateData> listExpCateData = new ArrayList<ExpensecateData >();

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expensecate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtExpCname = (EditText)findViewById(R.id.txtExpCname );
        btnAddExpCate = (Button)findViewById(R.id.btnAddExpCate);
        listExpCate = (ListView)findViewById(R.id.listExpCate);

        btnAddExpCate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                addExpenseCategory();

            }
        });

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        showList();
    }

    public int getCount() {
        String exp_cname = txtExpCname.getText().toString();

        Cursor c = null;
        try {
            database = dbHelper.getReadableDatabase();
            String query = "select count(*) from expensecate where exp_cname = ? ";
            c = database.rawQuery(query, new String[]{exp_cname});

            if (c.moveToFirst()) {
                return c.getInt(0);

            }
            return 0;

        } finally {
            if (c != null) {
                c.close();
            }

        }
    }

    public void editExpenseCategory(int exp_cid,String exp_cname){

        if (exp_cname == null) {

            Toast.makeText(getApplicationContext(), "Please input data",
                    Toast.LENGTH_SHORT).show();
            return;

        }if (getCount() != 0) {

            Toast.makeText(getApplicationContext(), "Expense category already taken", Toast.LENGTH_SHORT).show();

           return;
        }if (getCount() == 0) {


            dbHelper.editExpenseCategory(exp_cid, exp_cname);
            Toast.makeText(getApplicationContext(), "Edit data complete", Toast.LENGTH_SHORT).show();
            showList();

        }else{}

        showList();
    }

    public void deleteExpenseCategory(int exp_cid){
        database.delete("expensecate", "exp_cid = " + exp_cid, null);
        Toast.makeText(this, "Delete data complete", Toast.LENGTH_SHORT).show();

        showList();
    }

    private void getExpenseCategory() {

        String username = getIntent().getStringExtra("username");
        String query = "select * from expensecate where username = ?";
        Cursor mCursor = database.rawQuery(query,new String[] { ""+ username });

        if (mCursor != null) {
            mCursor.moveToFirst();

            listExpCateData.clear();

            if(mCursor.getCount() > 0){
                do {
                    int exp_cid = mCursor.getInt(mCursor.getColumnIndex("exp_cid"));
                    String exp_cname = mCursor.getString(mCursor.getColumnIndex("exp_cname"));

                    listExpCateData.add(new ExpensecateData(exp_cid,exp_cname));
                }while (mCursor.moveToNext());
            }
        }
    }

    private void addExpenseCategory() {
        // TODO Auto-generated method stub
        String username = getIntent().getStringExtra("username");
        String exp_cname = txtExpCname.getText().toString();

        if (txtExpCname.length() == 0) {

            Toast.makeText(getApplicationContext(), "Please input data", Toast.LENGTH_SHORT).show();
            return;
        }if (getCount() > 0  ) {
            Toast.makeText(getApplicationContext(), "Expense category already taken",
                    Toast.LENGTH_SHORT).show();
            return;

        }else {

            dbHelper.addExpenseCate(exp_cname,username);

            txtExpCname.setText("");
            showList();
        }
        showList();
    }

    private void showList() {

        getExpenseCategory();

        listExpCate.setAdapter( new AdapterListViewDataExpcate(this,listExpCateData ));
    }

    public void showEdit(int exp_cid,String exp_cname){
        Intent i = new Intent(this,EditExpensecateActivity.class);

        i.putExtra("keyExp_cid", exp_cid);
        i.putExtra("keyExp_cname", exp_cname);

        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode == 1 && resultCode == RESULT_OK){

            int exp_cid = intent.getExtras().getInt("keyExp_cid");
            String exp_cname = intent.getExtras().getString("keyExp_cname");

            editExpenseCategory(exp_cid, exp_cname);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String username = getIntent().getStringExtra("username");

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            case R.id.Home:
                Intent i = new Intent(ExpensecateActivity.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(ExpensecateActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(ExpensecateActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(ExpensecateActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(ExpensecateActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(ExpensecateActivity.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ExpensecateActivity.this,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ??");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(ExpensecateActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(ExpensecateActivity.this,LoginActivity.class);
                                        startActivity(intent);

                                        finish();


                                    }
                                }, 4000);
                    }
                });

                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:

                super.onOptionsItemSelected(item);

        }
        return true;

    }

}
