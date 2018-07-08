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
import com.moneymemo.adminpc.moneymemo.model.IncomeData;
import com.moneymemo.adminpc.moneymemo.model.SpinnerIncome;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class IncomeActivity extends AppCompatActivity {

    private static final String TAG = "IncomeActivity";

    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    private EditText income_amount,et_search;
    private EditText income_date;
    private EditText income_memo;
    private Button bt_addincome,bt_clear;
    private ListView listIncome;
    private Spinner sp_incomecate;
    private TextView tv_total,total,bt_searchInc;
    private ArrayList<IncomeData> listIncomeData = new ArrayList<IncomeData>();
    private  String selectDate;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        android.support.v7.app.ActionBar actionBar;
        actionBar = getSupportActionBar();

        // TODO: Remove the redundant calls to getSupportActionBar()

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String username = getIntent().getStringExtra("username");
        sp_incomecate = (Spinner) findViewById(R.id.sp_incomecate);
        et_search  = (EditText) findViewById(R.id.et_search);
        mDisplayDate = (EditText) findViewById(R.id.income_date);
        income_amount = (EditText) findViewById(R.id.income_amount);
        income_date = (EditText) findViewById(R.id.income_date);
        income_memo = (EditText) findViewById(R.id.income_memo);
        bt_addincome = (Button) findViewById(R.id.bt_addincome);
        bt_searchInc = (TextView) findViewById(R.id.bt_searchInc);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        listIncome = (ListView) findViewById(R.id.listIncome);
        total = (TextView) findViewById(R.id.total);
        tv_total = (TextView) findViewById(R.id.tv_total);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        income_amount.addTextChangedListener(new NumberTextWatcherForThousand2(income_amount));

        bt_addincome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                addIncome();

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
                        IncomeActivity.this,
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
                        IncomeActivity.this,
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
                if(month <10 && day<10) {
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

        bt_searchInc.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                showList();
                tv_total.setText("");
                listIncomeData.clear();
                selectDate = et_search.getText().toString();
                getSearchData();
                getSum();

            }
        });

        setListViewHeightBasedOnChildren(listIncome);

    }

    private void getSum(){
        String username = getIntent().getStringExtra("username");
        String getTotal = "SELECT  sum(income_amount) as sum_income " +
                " FROM  incomedata  " +
                "  WHERE  strftime('%Y-%m-%d' ,income_date ) = ?  AND username = ?  " ;

        Cursor c = database.rawQuery(getTotal,new String[] {""+selectDate, ""+ username});

        if (c.moveToFirst()) {
            do {


                String sum_inc = c.getString(0);

                DecimalFormat formatter = new DecimalFormat("#,###.00");


                if( sum_inc  != null ) {
                    Double sum = Double.parseDouble(String.valueOf(sum_inc));

                    tv_total.setText("+"+ formatter.format(sum)+ "฿");


                }else{
                    Toast.makeText(IncomeActivity.this , "No data" ,Toast.LENGTH_SHORT).show();

                    tv_total.setText("0.00");
                }
                total.setText("Total");

            } while (c.moveToNext());

        }

    }

    private void getSearchData(){

        String username = getIntent().getStringExtra("username");
        database = dbHelper.getReadableDatabase();

        String query = "SELECT income_id,incomedata.income_cid,income_amount  ,income_date,income_memo,incomecate.income_cid,income_cname " +
                "FROM incomedata,incomecate " +
                "WHERE income_id = income_id AND incomedata.income_cid = incomecate.income_cid AND income_date = ?" +
                "AND incomedata.username = ? AND incomecate.username = ? " +
                "ORDER BY incomedata.income_id DESC";


        Cursor mCursor = database.rawQuery(query, new String[] {""+ selectDate,""+ username , ""+ username});

        if (mCursor != null) {
            mCursor.moveToFirst();
            listIncomeData.clear();

            if (mCursor.getCount() > 0) {
                do {

                    int income_id = mCursor.getInt(mCursor.getColumnIndex("income_id"));
                    int income_cid = mCursor.getInt(mCursor.getColumnIndex("income_cid"));
                    String income_cname = mCursor.getString(mCursor.getColumnIndex("income_cname"));
                    String income_amount = mCursor.getString(mCursor.getColumnIndex("income_amount"));
                    String income_date = mCursor.getString(mCursor.getColumnIndex("income_date"));
                    String income_memo = mCursor.getString(mCursor.getColumnIndex("income_memo"));


                    listIncomeData.add(new IncomeData(income_id, income_cid, income_cname, income_amount, income_date, income_memo));
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
        List<SpinnerIncome> labels = db.getAllLabelsIncome(username);

        ArrayAdapter<SpinnerIncome> dataAdapter = new ArrayAdapter<SpinnerIncome>(this, R.layout.spinner_item, labels);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_incomecate.setAdapter(new NothingSelectedSpinnerAdapter2(dataAdapter,R.layout.contact_spinner_row_nothing_selected,this));
    }

    private void showList() {

        getIncome( );

        listIncome.setAdapter(new AdapterListViewDataIncome(this, listIncomeData));
    }

    private void addIncome() {
        // TODO Auto-generated method stub

        String username = getIntent().getStringExtra("username");
        int pos = sp_incomecate.getSelectedItemPosition();

        if(pos == 0){
            Toast.makeText(IncomeActivity.this, "Please select category" , Toast.LENGTH_SHORT).show();
            return;
        }
        if (income_amount.length() > 0 && income_date.length() > 0 && pos != 0) {

            String income_amnt = NumberTextWatcherForThousand2.trimCommaOfString(income_amount.getText().toString());
            ContentValues values = new ContentValues();
            values.put("username",username);
            values.put("income_cid", ((SpinnerIncome) sp_incomecate.getSelectedItem()).getId());
            values.put("income_amount", income_amnt);
            values.put("income_date", income_date.getText().toString());
            values.put("income_memo", income_memo.getText().toString());

            database.insert("incomedata", null, values);

            income_amount.setText("");
            income_date.setText("");
            income_memo.setText("");
            sp_incomecate.setSelection(0);

            showList();

        } else {
            Toast.makeText(this, "Please input data", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteIncome(int income_id) {
        database.delete("incomedata", "income_id = " + income_id , null);
        showList();
    }

    private void getIncome() {

        String username = getIntent().getStringExtra("username");
        String query = "SELECT income_id,incomedata.income_cid,income_amount  ,income_date,income_memo,incomecate.income_cid,income_cname " +
                "FROM incomedata,incomecate WHERE income_id = income_id AND incomedata.income_cid =  incomecate.income_cid " +
                "AND incomedata.username = ? AND incomecate.username = ? ORDER BY incomedata.income_id DESC";

        Cursor mCursor = database.rawQuery(query, new String[] {""+ username , ""+ username});

        if (mCursor != null) {
            mCursor.moveToFirst();

            listIncomeData.clear();

            if (mCursor.getCount() > 0) {
                do {
                    int income_id = mCursor.getInt(mCursor.getColumnIndex("income_id"));
                    int income_cid = mCursor.getInt(mCursor.getColumnIndex("income_cid"));
                    String income_cname = mCursor.getString(mCursor.getColumnIndex("income_cname"));
                    String income_amount = mCursor.getString(mCursor.getColumnIndex("income_amount"));
                    String income_date = mCursor.getString(mCursor.getColumnIndex("income_date"));
                    String income_memo = mCursor.getString(mCursor.getColumnIndex("income_memo"));

                    listIncomeData.add(new IncomeData(income_id, income_cid,income_cname, income_amount, income_date, income_memo));

                } while (mCursor.moveToNext());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 1 && resultCode == RESULT_OK) {

            int income_id = intent.getExtras().getInt("keyIncome_id");
            int income_cid = intent.getExtras().getInt("keyIncome_cid");
            String income_amount = intent.getExtras().getString("keyIncome_amount");
            String income_date = intent.getExtras().getString("keyIncome_date");
            String income_memo = intent.getExtras().getString("keyIncome_memo");

            editIncome(income_id,income_cid,income_amount,income_date,income_memo);

        }

    }

    public void showEdit(int income_id, int income_cid,String income_cname, String income_amount, String income_date, String income_memo) {
        Intent i = new Intent(this, EditIncomeActivity.class);

        income_cid =  sp_incomecate.getSelectedItemPosition();
        String username = getIntent().getStringExtra("username");

        i.putExtra("username",username);
        i.putExtra("keyIncome_id", income_id);
        i.putExtra("keyIncome_cid", income_cid);
        i.putExtra("keyIncome_cname",income_cname);
        i.putExtra("keyIncome_amount", income_amount);
        i.putExtra("keyIncome_date", income_date);
        i.putExtra("keyIncome_memo", income_memo);

        startActivityForResult(i, 1);
    }

    public void editIncome(int income_id, int income_cid, String income_amount, String income_date, String income_memo) {

        ContentValues values = new ContentValues();
        values.put("income_id", income_id);
        values.put("income_cid", income_cid);
        values.put("income_amount", String.valueOf(income_amount));
        values.put("income_date", income_date);
        values.put("income_memo", income_memo);

        database.update("incomedata", values, "income_id = ? ", new String[]{"" + income_id});

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
                Intent i = new Intent(IncomeActivity.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(IncomeActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(IncomeActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(IncomeActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(IncomeActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(IncomeActivity.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(IncomeActivity.this,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(IncomeActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(IncomeActivity.this,LoginActivity.class);
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
