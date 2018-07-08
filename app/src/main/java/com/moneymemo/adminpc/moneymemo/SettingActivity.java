package com.moneymemo.adminpc.moneymemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.moneymemo.adminpc.moneymemo.utils.DBHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SettingActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private SQLiteDatabase db;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting );

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

    }

    public void onClickNext(View view){
        String username = getIntent().getStringExtra("username");

        Intent intent = new Intent(SettingActivity.this, ExpensecateActivity.class);
        intent.putExtra("username",username);

        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


    }

    public void onClickNext5(View view){
        String username = getIntent().getStringExtra("username");


        Intent intent = new Intent(SettingActivity.this, ChangeEmail.class);
        intent.putExtra("username",username);

        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    public void onClickNext2(View view){

        String username = getIntent().getStringExtra("username");

        Intent intent2 = new Intent(SettingActivity.this, IncomecateActivity.class);
        intent2.putExtra("username",username);
        startActivity(intent2);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    public void onClickNext3(View view){

        String username = getIntent().getStringExtra("username");
        Intent intent3 = new Intent(SettingActivity.this, ChangePassActivity.class);
        intent3.putExtra("username",username);
        startActivity(intent3);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    public int getCountExpCate() {
        String username = getIntent().getStringExtra("username");

        Cursor c = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "select count(*) from expensecate where username = ? ";
            c = db.rawQuery(query, new String[]{username});

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

    public int getCountIncomeCate() {
         String username = getIntent().getStringExtra("username");

        Cursor c = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "select count(*) from incomecate where username = ? ";
            c = db.rawQuery(query, new String[]{username});

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

    public int getCountExpense() {
         String username = getIntent().getStringExtra("username");

        Cursor c = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "select count(*) from expensedata where username = ? ";
            c = db.rawQuery(query, new String[]{username});

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

    public int getCountIncome() {
         String username = getIntent().getStringExtra("username");

        Cursor c = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "select count(*) from incomedata where username = ? ";
            c = db.rawQuery(query, new String[]{username});

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

    public int getCountUsername(){
        String username = getIntent().getStringExtra("username");

        Cursor c = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "select count(*) from userdata where username = ? ";
            c = db.rawQuery(query, new String[]{username});

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

    public void onClickNext4(View view){

        final String username = getIntent().getStringExtra("username");

        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this,R.style.AppTheme_Dark_Dialog);
        builder.setTitle("Info");
        builder.setMessage("Do you want to delete your account ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(getCountUsername() > 0 ) {
                    database.delete("userdata" , "username = '" + username +"'" , null);

                    if(getCountExpense() > 0 ) {
                        database.delete("expensedata" , "username = '" + username +"'"  , null);
                    }
                    if(getCountIncome() > 0 ) {
                        database.delete("incomedata" , "username = '" + username +"'"  , null);
                    }
                    if(getCountExpCate() > 0 ) {
                        database.delete("expensecate" , "username = '" + username +"'"  , null);
                    }
                    if(getCountIncomeCate() > 0) {
                        database.delete("incomecate" , "username = '" + username +"'"  , null);
                    }
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingActivity.this , R.style.AppTheme_Dark_Dialog);
                    builder2.setMessage("Delete successfully");
                    AlertDialog dialog2 = builder2.create();
                    dialog2.show();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {


                                }
                            }, 8000);


                    Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                    startActivity(intent);

                    finish();
                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

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
                Intent i = new Intent(SettingActivity.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(SettingActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(SettingActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(SettingActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(SettingActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(SettingActivity.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
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

