package com.moneymemo.adminpc.moneymemo;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.moneymemo.adminpc.moneymemo.model.ExpenseData;
import com.moneymemo.adminpc.moneymemo.model.SpinnerObject;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ExpenseActivity extends AppCompatActivity {

    private static final String TAG = "ExpenseActivity";

    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    private EditText exp_amount;
    private EditText exp_date;
    private EditText exp_memo;
    private EditText et_search;
    private Button bt_addexpense,bt_clear;
    private ListView listExpense;
    private Spinner sp_expcate;
    private ArrayList<ExpenseData> listExpenseData = new ArrayList<ExpenseData>();
    private TextView total,tv_total,bt_searchexp;
    private String selectDate;

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        android.support.v7.app.ActionBar actionBar;
        actionBar = getSupportActionBar();

        // TODO: Remove the redundant calls to getSupportActionBar()

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String username = getIntent().getStringExtra("username");
        sp_expcate = (Spinner) findViewById(R.id.sp_expcate);
        et_search  = (EditText) findViewById(R.id.et_search);
        mDisplayDate = (EditText) findViewById(R.id.exp_date);
        exp_amount = (EditText) findViewById(R.id.exp_amount);
        exp_date = (EditText) findViewById(R.id.exp_date);
        exp_memo = (EditText) findViewById(R.id.exp_memo);
        bt_addexpense = (Button) findViewById(R.id.bt_addexpense);
        bt_searchexp = (TextView) findViewById(R.id.bt_searchExp);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        listExpense = (ListView) findViewById(R.id.listExpense);
        total = (TextView) findViewById(R.id.total);
        tv_total = (TextView) findViewById(R.id.tv_total);


        dbHelper = new DBHelper(this);

        //นำตัวจัดการฐานข้อมูลมาใช้งาน
        database = dbHelper.getWritableDatabase();

        exp_amount.addTextChangedListener(new NumberTextWatcherForThousand(exp_amount));

        bt_addexpense.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                addExpense();

            }
        });

        showList();
        loadSpinnerData(username);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ExpenseActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,

                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        et_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal2 = Calendar.getInstance();
                int year2 = cal2.get(Calendar.YEAR);
                int month2 = cal2.get(Calendar.MONTH);
                int day2 = cal2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog2 = new DatePickerDialog(
                        ExpenseActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year2, month2, day2);
                dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog2.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: date: yy-mm-dd: " + year + "-" + month + "-" + day);
                if(month < 10 && day >= 10 ) {
                    String date = year + "-0" + month + "-" + day;
                    mDisplayDate.setText(date);
                    if(mDisplayDate.getText().length() > 0){
                        mDisplayDate.setSelection(mDisplayDate.getText().length());
                    }

                }
                if(month >= 10 && day < 10 )  {
                    String date = year + "-" + month + "-0" + day;
                    mDisplayDate.setText(date);
                    if(mDisplayDate.getText().length() > 0){
                        mDisplayDate.setSelection(mDisplayDate.getText().length());
                    }

                }
                if(month <10 && day <10) {
                    String date = year + "-0" + month + "-0" + day;
                    mDisplayDate.setText(date);
                    if(mDisplayDate.getText().length() > 0){
                        mDisplayDate.setSelection(mDisplayDate.getText().length());
                    }
                }
                if (month >= 10 && day>=10){
                    String date = year + "-" + month + "-" + day;
                    mDisplayDate.setText(date);
                    if(mDisplayDate.getText().length() > 0){
                        mDisplayDate.setSelection(mDisplayDate.getText().length());
                    }
                }

            }
        };

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker2, int year2, int month2, int day2) {
                month2 = month2 + 1;
                Log.d(TAG, "onDateSet: date: yy-mm-dd: " + year2 + "-" + month2 + "-" + day2);
                if(month2 < 10 && day2 >= 10 ) {
                    String date2 = year2 + "-0" + month2 + "-" + day2;
                    et_search.setText(date2);
                    if(et_search.getText().length() > 0){
                        et_search.setSelection(et_search.getText().length());
                    }
                }
                if(month2 >= 10 && day2 < 10 )  {
                    String date2 = year2 + "-" + month2 + "-0" + day2;
                    et_search.setText(date2);
                    if(et_search.getText().length() > 0){
                        et_search.setSelection(et_search.getText().length());
                    }

                }
                if(month2 <10 && day2<10) {
                    String date2 = year2 + "-0" + month2 + "-0" + day2;
                    et_search.setText(date2);
                    if(et_search.getText().length() > 0){
                        et_search.setSelection(et_search.getText().length());
                    }
                }
                if (month2 >= 10 && day2>=10){
                    String date2 = year2 + "-" + month2 + "-" + day2;
                    et_search.setText(date2);
                    if(et_search.getText().length() > 0){
                        et_search.setSelection(et_search.getText().length());
                    }
                }
            }
        };

        bt_clear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                et_search.setText("");
                tv_total.setText("");
                total.setText("");
                showList();
            }
        });

        bt_searchexp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                showList();
                tv_total.setText("");
                listExpenseData.clear();
                selectDate = et_search.getText().toString();
                getSearchData();
                getSum();

            }
        });

        setListViewHeightBasedOnChildren(listExpense);

    }

    private void getSum(){
        String username = getIntent().getStringExtra("username");
        String getTotal = "SELECT  sum(exp_amount) as sum_expense " +
                " FROM  expensedata  " +
                "  WHERE  strftime('%Y-%m-%d' ,exp_date ) = ?  AND username = ?  " ;

        Cursor c = database.rawQuery(getTotal,new String[] {""+selectDate, ""+ username});

        if (c.moveToFirst()) {
            do {

                String sum_exp = c.getString(0);

                DecimalFormat formatter = new DecimalFormat("#,###.00");

                if( sum_exp  != null ) {
                    Double sum = Double.parseDouble(String.valueOf(sum_exp));

                    tv_total.setText("-"+ formatter.format(sum)+ "฿");

                }else{

                    Toast.makeText(ExpenseActivity.this , "No data" ,Toast.LENGTH_SHORT).show();
                    tv_total.setText("0.00");
                }
                total.setText("Total");

            } while (c.moveToNext());

        }

    }

    private void getSearchData(){

        String username = getIntent().getStringExtra("username");
        database = dbHelper.getReadableDatabase();

        String query = "SELECT exp_id,expensedata.exp_cid,exp_amount  ,exp_date,exp_memo,expensecate.exp_cid,exp_cname " +
                "FROM expensedata,expensecate WHERE exp_id = exp_id AND expensedata.exp_cid =  expensecate.exp_cid AND exp_date = ?" +
                "AND expensedata.username = ? AND expensecate.username = ? ORDER BY expensedata.exp_id DESC";

        Cursor mCursor = database.rawQuery(query, new String[] {""+ selectDate,""+ username , ""+ username});

        if (mCursor != null) {
            mCursor.moveToFirst();
            listExpenseData.clear();

            if (mCursor.getCount() > 0) {
                do {

                    int exp_id = mCursor.getInt(mCursor.getColumnIndex("exp_id"));
                    int exp_cid = mCursor.getInt(mCursor.getColumnIndex("exp_cid"));
                    String exp_cname = mCursor.getString(mCursor.getColumnIndex("exp_cname"));
                    String exp_amount = mCursor.getString(mCursor.getColumnIndex("exp_amount"));
                    String exp_date = mCursor.getString(mCursor.getColumnIndex("exp_date"));
                    String exp_memo = mCursor.getString(mCursor.getColumnIndex("exp_memo"));

                    listExpenseData.add(new ExpenseData(exp_id, exp_cid, exp_cname, exp_amount, exp_date, exp_memo));
                } while (mCursor.moveToNext());
            }
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void loadSpinnerData(String username) {
        DBHelper db = new DBHelper(getApplicationContext());
        List<SpinnerObject> labels = db.getAllLabels(username);

        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this, R.layout.spinner_item, labels);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_expcate.setPrompt("Select your category");
        sp_expcate.setAdapter(new NothingSelectedSpinnerAdapter(dataAdapter,R.layout.contact_spinner_row_nothing_selected,this));
    }

    private void showList() {

        getExpense( );

        listExpense.setAdapter(new AdapterListViewDataExpense(this, listExpenseData));
    }

    private void addExpense() {
        // TODO Auto-generated method stub

            String username = getIntent().getStringExtra("username");
        int pos = sp_expcate.getSelectedItemPosition();

        if(pos == 0){
            Toast.makeText(ExpenseActivity.this, "please select your category" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (exp_amount.length() > 0 && exp_date.length() > 0 && pos != 0) {
            //เตรียมข้อมูลสำหรับใส่ลงไปในตาราง
            String exp_amnt = NumberTextWatcherForThousand.trimCommaOfString(exp_amount.getText().toString());
           // exp_amnt = exp_amnt.replaceAll(",$","");
            ContentValues values = new ContentValues();
            values.put("username",username);
            values.put("exp_cid", ((SpinnerObject) sp_expcate.getSelectedItem()).getId());
            values.put("exp_amount", exp_amnt);
            values.put("exp_date", exp_date.getText().toString());
            values.put("exp_memo", exp_memo.getText().toString());

            database.insert("expensedata", null, values);

            exp_amount.setText("");
            exp_date.setText("");
            exp_memo.setText("");
            sp_expcate.setSelection(0);

            showList();

        } else {
            Toast.makeText(this, "Please input data", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteExpense(int exp_id) {
        database.delete("expensedata", "exp_id = " + exp_id , null);
        Toast.makeText(this, "Delete data complete", Toast.LENGTH_SHORT).show();

        showList();
    }

    private void getExpense() {

        String username = getIntent().getStringExtra("username");
        String query = "SELECT exp_id,expensedata.exp_cid,exp_amount  ,exp_date,exp_memo,expensecate.exp_cid,exp_cname " +
                "FROM expensedata,expensecate WHERE exp_id = exp_id AND expensedata.exp_cid =  expensecate.exp_cid " +
                "AND expensedata.username = ? AND expensecate.username = ? ORDER BY expensedata.exp_id DESC";

        Cursor mCursor = database.rawQuery(query, new String[] {""+ username , ""+ username});

        if (mCursor != null) {
            mCursor.moveToFirst();
            listExpenseData.clear();

            if (mCursor.getCount() > 0) {
                do {
                    int exp_id = mCursor.getInt(mCursor.getColumnIndex("exp_id"));
                    int exp_cid = mCursor.getInt(mCursor.getColumnIndex("exp_cid"));
                    String exp_cname = mCursor.getString(mCursor.getColumnIndex("exp_cname"));
                    String exp_amount = mCursor.getString(mCursor.getColumnIndex("exp_amount"));
                    String exp_date = mCursor.getString(mCursor.getColumnIndex("exp_date"));
                    String exp_memo = mCursor.getString(mCursor.getColumnIndex("exp_memo"));

                    listExpenseData.add(new ExpenseData(exp_id, exp_cid,exp_cname, exp_amount, exp_date, exp_memo));
                } while (mCursor.moveToNext());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 1 && resultCode == RESULT_OK) {

            int exp_id = intent.getExtras().getInt("keyExp_id");
            int exp_cid = intent.getExtras().getInt("keyExp_cid");
            String exp_amount = intent.getExtras().getString("keyExp_amount");
            String exp_date = intent.getExtras().getString("keyExp_date");
            String exp_memo = intent.getExtras().getString("keyExp_memo");

            editExpense(exp_id,exp_cid,exp_amount,exp_date,exp_memo);

        }

    }

    public void showEdit(int exp_id, int exp_cid,String exp_cname, String exp_amount, String exp_date, String exp_memo) {
        Intent i = new Intent(this, EditExpenseActivity.class);

        exp_cid =  sp_expcate.getSelectedItemPosition();
        String username = getIntent().getStringExtra("username");

        i.putExtra("username",username);
        i.putExtra("keyExp_id", exp_id);
        i.putExtra("keyExp_cid", exp_cid);
        i.putExtra("keyExp_cname",exp_cname);
        i.putExtra("keyExp_amount", exp_amount);
        i.putExtra("keyExp_date", exp_date);
        i.putExtra("keyExp_memo", exp_memo);

        startActivityForResult(i, 1);
    }

    public void editExpense(int exp_id, int exp_cid, String exp_amount, String exp_date, String exp_memo) {

        ContentValues values = new ContentValues();
        values.put("exp_id", exp_id);
        values.put("exp_cid", exp_cid);
        values.put("exp_amount", String.valueOf(exp_amount));
        values.put("exp_date", exp_date);
        values.put("exp_memo", exp_memo);

        database.update("expensedata", values, "exp_id = ? ", new String[]{"" + exp_id});

        showList();
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
                Intent i = new Intent(ExpenseActivity.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;


            case R.id.Income:
                Intent i1 = new Intent(ExpenseActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(ExpenseActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(ExpenseActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(ExpenseActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(ExpenseActivity.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseActivity.this,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(ExpenseActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(ExpenseActivity.this,LoginActivity.class);
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
