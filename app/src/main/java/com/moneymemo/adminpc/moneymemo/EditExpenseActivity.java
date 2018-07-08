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
import com.moneymemo.adminpc.moneymemo.model.SpinnerObject;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditExpenseActivity extends AppCompatActivity {

    private Spinner sp_expcate;
    private EditText exp_amount;
    private TextView exp_date;
    private EditText exp_memo;
    private Button btnEditExpense;
    private static final String TAG = "EditExpenseActivity ";
    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private int exp_id;
    private int exp_cid;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sp_expcate = (Spinner) findViewById(R.id.sp_expcate);
        exp_amount = (EditText) findViewById(R.id.exp_amount);
        exp_date = (EditText) findViewById(R.id.exp_date);
        exp_memo = (EditText) findViewById(R.id.exp_memo);

        btnEditExpense = (Button) findViewById(R.id.bt_editExpense);
        mDisplayDate = (EditText) findViewById(R.id.exp_date);

        String username = getIntent().getStringExtra("username");

        this.exp_id = getIntent().getExtras().getInt("keyExp_id");
        exp_cid = getIntent().getExtras().getInt("keyExp_cid");
        final double exp_amnt = Double.parseDouble(getIntent().getExtras().getString("keyExp_amount"));
        DecimalFormat formatter = new DecimalFormat("#,###");
        exp_amount.setText(formatter.format(exp_amnt));
        exp_date.setText(getIntent().getExtras().getString("keyExp_date"));
        exp_memo.setText(getIntent().getExtras().getString("keyExp_memo"));

        exp_amount.addTextChangedListener(new NumberTextWatcherForThousand(exp_amount));

        if(exp_amount.getText().length() > 0){
            exp_amount.setSelection(exp_amount.getText().length());
        }

        if(exp_memo.getText().length() > 0){
            exp_memo.setSelection(exp_memo.getText().length());
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
                        EditExpenseActivity.this,
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

        btnEditExpense.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent();
                setResult(RESULT_OK,i);

                String exp_amnt = NumberTextWatcherForThousand.trimCommaOfString(exp_amount.getText().toString());
                i.putExtra("keyExp_id", exp_id);
                i.putExtra("keyExp_cid", ((SpinnerObject) sp_expcate.getSelectedItem()).getId() );
                i.putExtra("keyExp_amount", exp_amnt);
                i.putExtra("keyExp_date", exp_date.getText().toString());
                i.putExtra("keyExp_memo", exp_memo.getText().toString());

                finish();
            }
        });

    }

    private void loadSpinnerData(String username) {
        DBHelper db = new DBHelper(getApplicationContext());
        List<SpinnerObject> labels = db.getAllLabels(username);

        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this,R.layout.spinner_item, labels);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_expcate.setAdapter(dataAdapter);
        String exp_cname = getIntent().getExtras().getString("keyExp_cname");
        sp_expcate.setAdapter(dataAdapter);
        sp_expcate.setSelection(getIndex(sp_expcate,exp_cname));

    }

    private int getIndex(Spinner sp_expcate, String exp_cname){
        int index = 0;
        for(int i=0 ; i< sp_expcate.getCount();i++){

            if(sp_expcate.getItemAtPosition(i).toString().equalsIgnoreCase(exp_cname)){
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
                Intent i = new Intent(EditExpenseActivity.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(EditExpenseActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(EditExpenseActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(EditExpenseActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(EditExpenseActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(EditExpenseActivity.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditExpenseActivity.this,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(EditExpenseActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(EditExpenseActivity.this,LoginActivity.class);
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
