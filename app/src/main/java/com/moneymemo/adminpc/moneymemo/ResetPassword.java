package com.moneymemo.adminpc.moneymemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moneymemo.adminpc.moneymemo.utils.DBHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText et_email ;
    private Button bt_reset,bt_login;
    private TextView tv_back;
    String subject,textMessage,email;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reset_password);

        et_email = (EditText) findViewById(R.id.et_email);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_reset = (Button) findViewById(R.id.bt_reset);
        tv_back = (TextView) findViewById(R.id.tv_back);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        bt_reset.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        tv_back.setOnClickListener(this);

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
        db.close();
        return s_email;

    }

    public String getPassword() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String username = getIntent().getStringExtra("username");
        String query = "SELECT password " +
                "FROM userdata " +
                "WHERE  username = ? ";

        Cursor cursor = database.rawQuery(query, new String[]{"" + username});
        String s_pass = null;
        if (cursor.moveToNext()) {
            do {
                s_pass = cursor.getString(0);


            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return s_pass;

    }

    private void sendEmail() {

        String username = getIntent().getStringExtra("username");
         email = et_email.getText().toString().trim();
         subject = "Forget Password - Money Memo";
        textMessage = "Dear " + username + "\n\n\n You may now log-in to the Money Memo application with the password below:\n\n" +
                "Your password: " + getPassword() +"\n\n Thank you for using the Money Memo application. \n\n Please do not reply this email.";

        SendMail sm = new SendMail(this, email, subject, textMessage);

        sm.execute();
    }

    @Override
    public void onClick(View v){

        email = et_email.getText().toString().trim();

        if (v.getId() == R.id.bt_reset) {
            if (email == null || "".equalsIgnoreCase(email)) {
                Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_LONG).show();
            }
            if(!email.matches(getEmail())){
                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
            }
            if(email.matches(getEmail()) && email != null){

                sendEmail();

            }
        }
        if(v.getId() == R.id.tv_back){
            Intent i = new Intent(ResetPassword.this, ForgetPassword.class);

            startActivity(i);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        if(v.getId() == R.id.bt_login){
            Intent i = new Intent(ResetPassword.this,LoginActivity.class);

            startActivity(i);

        }

    }

}
