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
import com.moneymemo.adminpc.moneymemo.model.IncomecateData;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class IncomecateActivity extends AppCompatActivity {

    private EditText txtIncCname;
    private Button btnAddIncCate;
    private ListView listIncCate;
    private ArrayList<IncomecateData> listIncCateData = new ArrayList<IncomecateData >();
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomecate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtIncCname = (EditText)findViewById(R.id.txtIncCname );
        btnAddIncCate = (Button)findViewById(R.id.btnAddIncCate);
        listIncCate = (ListView)findViewById(R.id.listIncCate);

        btnAddIncCate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                addIncomeCategory();

            }
        });

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        showList();
    }

    public int getCount() {
        String income_cname = txtIncCname.getText().toString();

        Cursor c = null;
        try {
            database = dbHelper.getReadableDatabase();
            String query = "select count(*) from incomecate where income_cname = ? ";
            c = database.rawQuery(query, new String[]{income_cname});

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

    public void editIncomeCategory(int income_cid,String income_cname){

        if (income_cname == null) {

            Toast.makeText(getApplicationContext(), "Please input data",
                    Toast.LENGTH_SHORT).show();
            return;

        }if (getCount() != 0) {

            Toast.makeText(getApplicationContext(), "Income category already taken", Toast.LENGTH_SHORT).show();

            return;
        }if (getCount() == 0) {


            dbHelper.editIncomeCategory(income_cid, income_cname);
            Toast.makeText(getApplicationContext(), "Edit data complete", Toast.LENGTH_SHORT).show();
            showList();

        }else{}

        showList();
    }

    public void deleteIncomeCategory(int income_cid){
        database.delete("incomecate", "income_cid = " + income_cid, null);
        Toast.makeText(this, "Delete data complete", Toast.LENGTH_SHORT).show();

        showList();
    }

    private void getIncomeCategory() {
        String username = getIntent().getStringExtra("username");
        String query = "select * from incomecate where username = ?";
        Cursor mCursor = database.rawQuery(query,new String[] { ""+ username });

        if (mCursor != null) {
            mCursor.moveToFirst();

            listIncCateData.clear();

            if(mCursor.getCount() > 0){
                do {
                    int income_cid = mCursor.getInt(mCursor.getColumnIndex("income_cid"));
                    String income_cname = mCursor.getString(mCursor.getColumnIndex("income_cname"));



                    listIncCateData.add(new IncomecateData(income_cid,income_cname));
                }while (mCursor.moveToNext());
            }
        }
    }

    private void addIncomeCategory() {
        // TODO Auto-generated method stub
        String username = getIntent().getStringExtra("username");
        String income_cname = txtIncCname.getText().toString();

        if (txtIncCname.length() == 0) {

            Toast.makeText(getApplicationContext(), "Please input data", Toast.LENGTH_SHORT).show();
            return;
        }if (getCount() > 0  ) {
            Toast.makeText(getApplicationContext(), "Income category already taken",
                    Toast.LENGTH_SHORT).show();
            return;

        }else {

            dbHelper.addIncomeCate(income_cname,username);

            txtIncCname.setText("");
            showList();
        }
        showList();
    }

    private void showList() {

        getIncomeCategory();

        listIncCate.setAdapter( new AdapterListViewDataIncomecate(this,listIncCateData ));
    }

    public void showEdit(int income_cid,String income_cname){
        Intent i = new Intent(this,EditIncomecateActivity.class);

        i.putExtra("keyIncome_cid", income_cid);
        i.putExtra("keyIncome_cname", income_cname);

        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode == 1 && resultCode == RESULT_OK){

            int income_cid = intent.getExtras().getInt("keyIncome_cid");
            String income_cname = intent.getExtras().getString("keyIncome_cname");

            editIncomeCategory(income_cid, income_cname);
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
                Intent i = new Intent(IncomecateActivity.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(IncomecateActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(IncomecateActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(IncomecateActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(IncomecateActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(IncomecateActivity.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(IncomecateActivity.this,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(IncomecateActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(IncomecateActivity.this,LoginActivity.class);
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
