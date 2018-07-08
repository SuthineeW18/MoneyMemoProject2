package com.moneymemo.adminpc.moneymemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import java.text.DecimalFormat;
import java.util.Calendar;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    TextView tv_thisMonth,tv_sumIncome, tv_sumExpense, tv_moneyLeft, tv_goal, tv_sumAll,tv_welcome;
    EditText et_saveGoal;
    Button bt_submit;
    private String et_goal ;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();

        tv_welcome = (TextView) findViewById(R.id.tv_welcome);
        tv_thisMonth = (TextView) findViewById(R.id.tv_thisMonth);
        tv_sumIncome = (TextView) findViewById(R.id.tv_sumIncome);
        tv_sumExpense = (TextView) findViewById(R.id.tv_sumExpense);
        tv_moneyLeft = (TextView) findViewById(R.id.tv_moneyLeft);
        tv_goal = (TextView) findViewById(R.id.tv_goal);
        tv_sumAll = (TextView) findViewById(R.id.tv_sumAll);
        et_saveGoal = (EditText) findViewById(R.id.et_saveGoal);
        bt_submit = (Button) findViewById(R.id.bt_submit);

        String username = getIntent().getStringExtra("username");

        tv_welcome.setText("Hello, " + username + " !");

        database = dbHelper.getReadableDatabase();
        String query = "SELECT strftime('%d ', income_date) as inc_date , strftime('%d ', exp_date) as exp_date, " +
                "min(strftime('%d ', income_date)) as min_date, max(strftime('%d ', income_date)) as max_date " +
                ",min(strftime('%d ', exp_date)) as min_edate ,max(strftime('%d ', exp_date)) as max_edate, " +
                "min(min(strftime('%d ', income_date)),min(strftime('%d ', exp_date)) ) as min_rs, " +
                "max(max(strftime('%d ', income_date)) ,max(strftime('%d ', exp_date))) as max_rs " +
                "FROM incomedata,expensedata " +
                "WHERE incomedata.username = ? AND expensedata.username = ? AND strftime('%Y-%m', income_date) = strftime('%Y-%m','now') " +
                "AND strftime('%Y-%m', exp_date) = strftime('%Y-%m','now') ";
        Cursor cursor = database.rawQuery(query, new String [] {"" + username, "" + username} );
        String min_date = "";
        String max_date = "";
        String min_edate = "";
        String max_edate = "";
        String min_rs = "";
        String max_rs = "";
        String inc_date= "";
        String exp_date = "";

        if(cursor.moveToNext())
        {
            do{
                inc_date = cursor.getString(0);
                exp_date = cursor.getString(1);
                min_date = cursor.getString(2);
                max_date = cursor.getString(3);
                min_edate = cursor.getString(4);
                max_edate = cursor.getString(5);
                min_rs = cursor.getString(6);
                max_rs = cursor.getString(7);


            }
            while (cursor.moveToNext());
        }
        cursor.close();
        database.close();

        Calendar mCalender = Calendar.getInstance();
        int month = mCalender.get(Calendar.MONTH)+1;

        int year = mCalender.get(Calendar.YEAR);

        if(inc_date != null && exp_date != null && min_rs.equals(max_rs) ){
                if (month == 1) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " มกราคม " + year);
                }
                if (month == 2) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " กุมภาพันธ์ " +year);
                }
                if (month == 3) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " มีนาคม " + year);
                }
                if (month == 4) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " เมษายน "+ year);
                }
                if (month == 5) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " พฤษภาคม "+year);
                }
                if (month == 6) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " มิถุนายน "+year);
                }
                if (month == 7) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " กรกฎาคม "+year);
                }
                if (month == 8) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " สิงหาคม "+year);
                }
                if (month == 9) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " กันยายน "+year);
                }
                if (month == 10) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " ตุลาคม "+year);
                }
                if (month == 11) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " พฤศจิกายน "+year);
                }
                if (month == 12) {
                    tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + " ธันวาคม " + year);
                }

            }
        if(inc_date != null && exp_date != null && !min_rs.equals(max_rs) ){
            if (month == 1) {
                tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " มกราคม "+year);
            }
            if (month == 2) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " กุมภาพันธ์ "+year);
            }
            if (month == 3) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " มีนาคม "+ year);
            }
            if (month == 4) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " เมษายน "+year);
            }
            if (month == 5) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " พฤษภาคม "+year);
            }
            if (month == 6) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " มิถุนายน "+year);
            }
            if (month == 7) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " กรกฎาคม "+year);
            }
            if (month == 8) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " สิงหาคม "+year);
            }
            if (month == 9) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " กันยายน "+year);
            }
            if (month == 10) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " ตุลาคม "+year);
            }
            if (month == 11) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " พฤศจิกายน "+year);
            }
            if (month == 12) {
            tv_thisMonth.setText("สถานะทางการเงินวันที่ " + min_rs + "- " + max_rs + " ธันวาคม "+year);
            }
          }
        if(inc_date == null && exp_date != null && min_rs.equals(max_rs) ){
            if (month == 1) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " January "+year);
            }
            if (month == 2) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " Febuary "+year);
            }
            if (month == 3) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " March "+year);
            }
            if (month == 4) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " April "+year);
            }
            if (month == 5) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " May "+year);
            }
            if (month == 6) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " June "+year);
            }
            if (month == 7) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " July "+year);
            }
            if (month == 8) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " August "+year);
            }
            if (month == 9) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " September "+year);
            }
            if (month == 10) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " October "+year);
            }
            if (month == 11) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " November "+year);
            }
            if (month == 12) {
                tv_thisMonth.setText("Financial Status on " + min_edate + " December "+year);
            }
        }
        if(inc_date == null && exp_date != null && !min_rs.equals(max_rs) ){
            if (month == 1) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " January "+year);
            }
            if (month == 2) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " Febuary "+year);
            }
            if (month == 3) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " March "+year);
            }
            if (month == 4) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " April "+year);
            }
            if (month == 5) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " May "+year);
            }
            if (month == 6) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " June "+year);
            }
            if (month == 7) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " July "+year);
            }
            if (month == 8) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " August "+year);
            }
            if (month == 9) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " September "+year);
            }
            if (month == 10) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " October "+year);
            }
            if (month == 11) {
                tv_thisMonth.setText("Financial Status on " +min_edate + "- " + max_edate+ " November "+year);
            }
            if (month == 12) {
                tv_thisMonth.setText("Financial Status on " + min_edate + "- " + max_edate + " December "+year);
            }
        }
        if(inc_date != null && exp_date == null && min_rs.equals(max_rs) ){
            if (month == 1) {
                tv_thisMonth.setText("Financial Status on " + min_date + " January "+year);
            }
            if (month == 2) {
                tv_thisMonth.setText("Financial Status on " + min_date + " Febuary "+year);
            }
            if (month == 3) {
                tv_thisMonth.setText("Financial Status on " + min_date + " March "+year);
            }
            if (month == 4) {
                tv_thisMonth.setText("Financial Status on " + min_date + " April "+year);
            }
            if (month == 5) {
                tv_thisMonth.setText("Financial Status on " + min_date + " May "+year);
            }
            if (month == 6) {
                tv_thisMonth.setText("Financial Status on " + min_date + " June "+year);
            }
            if (month == 7) {
                tv_thisMonth.setText("Financial Status on " + min_date + " July "+year);
            }
            if (month == 8) {
                tv_thisMonth.setText("Financial Status on " + min_date + " August "+year);
            }
            if (month == 9) {
                tv_thisMonth.setText("Financial Status on " + min_date + " September "+year);
            }
            if (month == 10) {
                tv_thisMonth.setText("Financial Status on " + min_date + " October "+year);
            }
            if (month == 11) {
                tv_thisMonth.setText("Financial Status on " + min_date + " November "+year);
            }
            if (month == 12) {
                tv_thisMonth.setText("Financial Status on " + min_date + " December "+year);
            }
        }
        if(inc_date != null && exp_date == null && !min_rs.equals(max_rs) ){
            if (month == 1) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date + " January "+year);
            }
            if (month == 2) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date + " Febuary "+year);
            }
            if (month == 3) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date + " March "+year);
            }
            if (month == 4) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date + " April "+year);
            }
            if (month == 5) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date + " May "+year);
            }
            if (month == 6) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date + " June "+year);
            }
            if (month == 7) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date + " July "+year);
            }
            if (month == 8) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date + " August "+year);
            }
            if (month == 9) {
                tv_thisMonth.setText("Financial Status on " +min_date + "- " + max_date+ " September "+year);
            }
            if (month == 10) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date+ " October "+year);
            }
            if (month == 11) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date+ " November "+year);
            }
            if (month == 12) {
                tv_thisMonth.setText("Financial Status on " + min_date + "- " + max_date + " December "+year);
            }
        }


        double goal;
        double result, result2;
        double income_am,expense;
        double e_amount, i_amount;
        double balance;
        String sumincome = dbHelper.getSumInc(username);
        String sumexpense = dbHelper.getSumExp(username);

        SharedPreferences sp1 = this.getSharedPreferences("setGoal" , MODE_PRIVATE);
        String tv = sp1.getString("set",null);
        et_saveGoal.setText(tv);

        if(et_saveGoal.getText().length() > 0){
            et_saveGoal.setSelection(et_saveGoal.getText().length());
        }
        if(sumincome != null && sumexpense != null  ){
            try {
                goal = Double.parseDouble(String.valueOf(et_saveGoal.getText().toString()));
            } catch (NumberFormatException e) {
               goal = 0;
            }

            income_am = Double.parseDouble(String.valueOf(sumincome));
            expense = Double.parseDouble(String.valueOf(sumexpense));

            balance = income_am - expense;
            result = (income_am * goal) / 100;
            result = Double.parseDouble(String.valueOf(result));
            result2 = Double.parseDouble(String.valueOf(balance - result));
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            tv_sumIncome.setText(formatter.format(income_am));
            tv_sumExpense.setText(formatter.format(expense));
            tv_moneyLeft.setText(formatter.format(balance));
            tv_goal.setText(formatter.format(result));
            tv_sumAll.setText(formatter.format(result2));
            et_saveGoal.setText(et_saveGoal.getText().toString());

            if(et_saveGoal.getText().length() > 0){
                et_saveGoal.setSelection(et_saveGoal.getText().length());
            }
            if(result == 0){
                tv_goal.setText("0.00");
            }
        }
        if (sumexpense == null && sumincome != null) {
            try {
                goal = Double.parseDouble(String.valueOf(et_saveGoal.getText().toString()));
            } catch (NumberFormatException e) {
                goal = 0;
            }

            income_am = Double.parseDouble(String.valueOf(sumincome));

            try {
                expense = Double.parseDouble(String.valueOf(sumexpense));
            } catch (NumberFormatException e) {
                expense = 0;
            }

            balance = income_am - expense;
            result = (income_am * goal) / 100;
            result = Double.parseDouble(String.valueOf(result));
            result2 = Double.parseDouble(String.valueOf(balance - result));
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            i_amount = Double.parseDouble(String.valueOf(sumincome));
            tv_sumIncome.setText(formatter.format(income_am));
            tv_sumExpense.setText("0.00");
            tv_moneyLeft.setText(formatter.format(i_amount));
            tv_goal.setText(formatter.format(result));
            tv_sumAll.setText(formatter.format(result2));
            et_saveGoal.setText(et_saveGoal.getText().toString());

            if(et_saveGoal.getText().length() > 0){
                et_saveGoal.setSelection(et_saveGoal.getText().length());
            }
            if(result == 0){
                tv_goal.setText("0.00");
            }
        }
        if (sumincome == null && sumexpense != null) {
            try {
                goal = Double.parseDouble(String.valueOf(et_saveGoal.getText().toString()));
            } catch (NumberFormatException e) {
                goal = 0;
            }

            try {
                income_am = Double.parseDouble(String.valueOf(sumincome));
            } catch (NumberFormatException e) {
                income_am = 0;
            }

            expense = Double.parseDouble(String.valueOf(sumexpense));
            balance = income_am - expense;
            result = (income_am * goal) / 100;
            result = Double.parseDouble(String.valueOf(result));
            result2 = Double.parseDouble(String.valueOf(balance - result));
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            e_amount = Double.parseDouble(String.valueOf(sumexpense));

            tv_sumIncome.setText("0.00");
            tv_sumExpense.setText(formatter.format(expense));
            tv_moneyLeft.setText("-" + formatter.format(e_amount));
            tv_goal.setText(formatter.format(result));
            tv_sumAll.setText(formatter.format(result2));
            et_saveGoal.setText(et_saveGoal.getText().toString());

            if(et_saveGoal.getText().length() > 0){
                et_saveGoal.setSelection(et_saveGoal.getText().length());
            }
            if(result == 0){
                tv_goal.setText("0.00");
            }
        }
        if(sumexpense == null && sumincome == null){

            tv_sumExpense.setText("0.00");
            tv_sumIncome.setText("0.00");
            tv_moneyLeft.setText("0.00");
            tv_goal.setText("0.00");
            tv_sumAll.setText("0.00");
            et_saveGoal.setText(et_saveGoal.getText().toString());

            if(et_saveGoal.getText().length() > 0){
                et_saveGoal.setSelection(et_saveGoal.getText().length());
            }


        }

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double goal;
                double result, result2;
                double income_am,expense;
                double balance;
                String username = getIntent().getStringExtra("username");
                String sumincome = dbHelper.getSumInc(username);
                String sumexpense = dbHelper.getSumExp(username);

                et_goal = et_saveGoal.getText().toString();
                et_saveGoal.setText(et_goal);

                if(et_saveGoal.getText().length() > 0){
                    et_saveGoal.setSelection(et_saveGoal.getText().length());
                }

                SharedPreferences sp = getSharedPreferences("setGoal" , MODE_PRIVATE);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("set" , et_saveGoal.getText().toString());
                Ed.commit();

                //Statement conditions
                if(sumincome != null && sumexpense !=null ){
                    try {
                        goal = Double.parseDouble(String.valueOf(et_saveGoal.getText().toString()));
                    } catch (NumberFormatException e) {
                        goal = 0;
                    }

                    income_am = Double.parseDouble(String.valueOf(sumincome));
                    expense = Double.parseDouble(String.valueOf(sumexpense));

                    balance = income_am - expense;
                    result = (income_am * goal) / 100;
                    result = Double.parseDouble(String.valueOf(result));
                    result2 = Double.parseDouble(String.valueOf(balance - result));
                    DecimalFormat formatter = new DecimalFormat("#,###.00");

                    tv_goal.setText(formatter.format(result));
                    tv_sumAll.setText(formatter.format(result2));
                    et_saveGoal.setText(et_goal);

                    if(et_saveGoal.getText().length() > 0){

                        et_saveGoal.setSelection(et_saveGoal.getText().length());
                    }
                    if(result == 0){

                        tv_goal.setText("0.00");
                    }
                }
                if(sumincome == null && sumexpense !=null ){

                    try {

                        goal = Double.parseDouble(String.valueOf(et_saveGoal.getText().toString()));

                    } catch (NumberFormatException e) {

                        goal = 0;

                    }
                    try {

                        income_am = Double.parseDouble(String.valueOf(sumincome));

                    } catch (NumberFormatException e) {

                        income_am = 0;

                    }

                    expense = Double.parseDouble(String.valueOf(sumexpense));
                    balance = income_am - expense;
                    result = (income_am * goal) / 100;
                    result = Double.parseDouble(String.valueOf(result));
                    result2 = Double.parseDouble(String.valueOf(balance - result));
                    DecimalFormat formatter = new DecimalFormat("#,###.00");

                    tv_goal.setText(formatter.format(result));
                    tv_sumAll.setText(formatter.format(result2));
                    et_saveGoal.setText(et_goal);

                    if(et_saveGoal.getText().length() > 0){
                        et_saveGoal.setSelection(et_saveGoal.getText().length());
                    }
                    if(result == 0){
                        tv_goal.setText("0.00");
                    }
                }
                if(sumincome != null && sumexpense ==null ){
                    try {
                        goal = Double.parseDouble(String.valueOf(et_saveGoal.getText().toString()));
                    } catch (NumberFormatException e) {
                        goal = 0;
                    }

                    income_am = Double.parseDouble(String.valueOf(sumincome));
                    try {
                        expense = Double.parseDouble(String.valueOf(sumexpense));
                    } catch (NumberFormatException e) {
                        expense = 0;
                    }
                    balance = income_am - expense;
                    result = (income_am * goal) / 100;
                    result = Double.parseDouble(String.valueOf(result));
                    result2 = Double.parseDouble(String.valueOf(balance - result));
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    tv_goal.setText(formatter.format(result));
                    tv_sumAll.setText(formatter.format(result2));
                    et_saveGoal.setText(et_goal);
                    if(et_saveGoal.getText().length() > 0){
                        et_saveGoal.setSelection(et_saveGoal.getText().length());
                    }
                    if(result == 0){
                        tv_goal.setText("0.00");
                    }
                }
                if(sumincome == null && sumexpense ==null ){
                    try {
                        goal = Double.parseDouble(String.valueOf(et_saveGoal.getText().toString()));
                    } catch (NumberFormatException e) {
                        goal = 0;
                    }

                    income_am = Double.parseDouble(String.valueOf(sumincome));
                    expense = Double.parseDouble(String.valueOf(sumexpense));

                    result = (income_am * goal) / 100;
                    result = Double.parseDouble(String.valueOf(result));
                    tv_goal.setText("0.00");
                    tv_sumAll.setText("0.00");
                    et_saveGoal.setText(et_goal);
                    if(et_saveGoal.getText().length() > 0){
                        et_saveGoal.setSelection(et_saveGoal.getText().length());
                    }
                    if(result == 0){
                        tv_goal.setText("0.00");
                    }
                }
            }
        });
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
            case R.id.Home:

                Intent i = new Intent(MainActivity.this,MainActivity.class);
                i.putExtra("username",username);

                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(MainActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);

                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(MainActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);


                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(MainActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);

                startActivity(i3);

                return true;

            case R.id.Forecast:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("key",et_saveGoal.getText().toString());
                editor.commit();
                Intent i4 = new Intent(MainActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(MainActivity.this,SettingActivity.class);
                i5.putExtra("username",username);

                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
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
