package com.moneymemo.adminpc.moneymemo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.moneymemo.adminpc.moneymemo.model.SpinnerIncome;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditIncomeActivity extends AppCompatActivity {

    private Spinner sp_incomecate;
    private EditText income_amount;
    private TextView income_date;
    private EditText income_memo;
    private Button btnEditIncome;
    private static final String TAG = "EditIncomeActivity ";
    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int income_id;
    private  int income_cid;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_income);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sp_incomecate = (Spinner) findViewById(R.id.sp_incomecate);
        income_amount = (EditText) findViewById(R.id.income_amount);
        income_date = (EditText) findViewById(R.id.income_date);
        income_memo = (EditText) findViewById(R.id.income_memo);
        btnEditIncome = (Button) findViewById(R.id.bt_editIncome);
        mDisplayDate = (EditText) findViewById(R.id.income_date);

        String username = getIntent().getStringExtra("username");
        this.income_id = getIntent().getExtras().getInt("keyIncome_id");
        income_cid = getIntent().getExtras().getInt("keyIncome_cid");

        final double inc_amount = Double.parseDouble(getIntent().getExtras().getString("keyIncome_amount"));
        DecimalFormat formatter = new DecimalFormat("#,###");
        income_amount.setText(formatter.format(inc_amount));
        income_date.setText(getIntent().getExtras().getString("keyIncome_date"));
        income_memo.setText(getIntent().getExtras().getString("keyIncome_memo"));

        income_amount.addTextChangedListener(new NumberTextWatcherForThousand2(income_amount));

        if(income_amount.getText().length() > 0){
            income_amount.setSelection(income_amount.getText().length());
        }

        if(income_memo.getText().length() > 0){
            income_memo.setSelection(income_memo.getText().length());
        }
        loadSpinnerData(username);


        mDisplayDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditIncomeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);

                dialog.show();
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

                }
                if(month >= 10 && day < 10 )  {
                    String date = year + "-" + month + "-0" + day;
                    mDisplayDate.setText(date);

                }
                if(month <10 && day <10) {
                    String date = year + "-0" + month + "-0" + day;
                    mDisplayDate.setText(date);
                }
                if (month >= 10 && day>=10){
                    String date = year + "-" + month + "-" + day;
                    mDisplayDate.setText(date);
                }

            }
        };


        btnEditIncome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();

                setResult(RESULT_OK,i);
                String income_amnt = NumberTextWatcherForThousand2.trimCommaOfString(income_amount.getText().toString());

                i.putExtra("keyIncome_id", income_id);
                i.putExtra("keyIncome_cid", ((SpinnerIncome) sp_incomecate.getSelectedItem()).getId() );
                i.putExtra("keyIncome_amount",income_amnt);
                i.putExtra("keyIncome_date", income_date.getText().toString());
                i.putExtra("keyIncome_memo", income_memo.getText().toString());

                finish();
            }
        });

    }

    private void loadSpinnerData(String username) {
        DBHelper db = new DBHelper(getApplicationContext());
        List<SpinnerIncome> labels = db.getAllLabelsIncome(username);

        ArrayAdapter<SpinnerIncome> dataAdapter = new ArrayAdapter<SpinnerIncome>(this,R.layout.spinner_item, labels);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String income_cname = getIntent().getExtras().getString("keyIncome_cname");
        sp_incomecate.setAdapter(dataAdapter);
        sp_incomecate.setSelection(getIndex(sp_incomecate,income_cname));

    }

    private int getIndex(Spinner sp_incomecate, String income_cname){
        int index = 0;
        for(int i=0 ; i< sp_incomecate.getCount();i++){

            if(sp_incomecate.getItemAtPosition(i).toString().equalsIgnoreCase(income_cname)){
                index = i;
                break;
            }
        }
        return index;
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
                Intent i = new Intent(EditIncomeActivity.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(EditIncomeActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(EditIncomeActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(EditIncomeActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(EditIncomeActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(EditIncomeActivity.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditIncomeActivity.this,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(EditIncomeActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(EditIncomeActivity.this,LoginActivity.class);
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
