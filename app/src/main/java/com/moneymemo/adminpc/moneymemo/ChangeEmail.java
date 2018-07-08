package com.moneymemo.adminpc.moneymemo;

import android.content.ContentValues;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChangeEmail extends AppCompatActivity implements View.OnClickListener{
    private DBHelper dbHelper;
    private EditText et_mail ;
    private Button bt_editMail;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        et_mail = (EditText) findViewById(R.id.et_mail);
        bt_editMail = (Button) findViewById(R.id.bt_editMail);

        dbHelper = new DBHelper(this);
        bt_editMail.setOnClickListener(this);
        et_mail.setText(getEmail());

        if(et_mail.getText().length() > 0){
            et_mail.setSelection(et_mail.getText().length());
        }
    }

    public String getEmail(){
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String username = getIntent().getStringExtra("username");
        String query = "SELECT email " +
                "FROM userdata " +
                "WHERE  username = ? " ;

        Cursor cursor = database.rawQuery(query, new String [] {"" + username });
        String s_email = null;
        if(cursor.moveToNext())
        {
            do{
                s_email = cursor.getString(0);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return s_email;

    }

    @Override
    public void onClick(View v){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

       String editMail =  et_mail.getText().toString();
       final String emailPattern = "^[_A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*@[A-Za-z0-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if(!editMail.matches(emailPattern)){
            Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();
        }
        if(editMail == null){
            Toast.makeText(getApplicationContext(), "please input email", Toast.LENGTH_LONG).show();
        }
        if(editMail.matches(emailPattern) && editMail != null) {

            ContentValues values = new ContentValues();
            values.put("email", editMail);
            final String username = getIntent().getStringExtra("username");

            db.update("userdata", values, "username = ? ", new String[]{"" + username});
            Toast.makeText(getApplicationContext(), "Change email successful", Toast.LENGTH_LONG).show();
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
                Intent i = new Intent(ChangeEmail.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(ChangeEmail.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(ChangeEmail.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(ChangeEmail.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(ChangeEmail.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(ChangeEmail.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;
            case R.id.Logout:

                final AlertDialog.Builder builder = new AlertDialog.Builder(ChangeEmail.this, R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(ChangeEmail.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(ChangeEmail.this,LoginActivity.class);
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
