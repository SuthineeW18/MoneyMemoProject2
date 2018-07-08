package com.moneymemo.adminpc.moneymemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddNewAccountActivity extends AppCompatActivity implements View.OnClickListener  {

    private EditText et_username, et_password, et_confirmpassword,et_email;
    private Button bt_submit;
    private TextView link_login;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_new_account);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirmpassword = (EditText) findViewById(R.id.et_confirmpassword);
        et_email = (EditText) findViewById(R.id.et_email);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        link_login = (TextView) findViewById(R.id.link_login);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addUser();
            }
        });

        link_login.setOnClickListener( this);

    }


    private void addUser() {
        // TODO Auto-generated method stub

        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        String comfirm_pass = et_confirmpassword.getText().toString();
        final String email = et_email.getText().toString().trim();
        final String emailPattern = "^[_A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*@[A-Za-z0-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        long rows = dbHelper.insertUser(username, password, email);


        if (!password.equals(comfirm_pass)) {

            Toast pass = Toast.makeText(getApplication(), "Passwords don't match! ", Toast.LENGTH_SHORT);
            pass.show();
            return;
        }
        if (rows == -1) {
            Toast.makeText(getApplicationContext(), "Username already exist ",
                    Toast.LENGTH_LONG).show();
            return;

        }
        if (et_username.length() == 0 || et_password.length() == 0 || et_email.length() == 0) {
            Toast.makeText(this, "Please Input Data", Toast.LENGTH_SHORT).show();
            return;

        }
        if (!email.matches(emailPattern) && et_email.length() != 0) {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();

            return;

        }
        if(rows == 0){

            dbHelper.insertIncomeCate(username);
            dbHelper.insertExpenseCate(username);

            bt_submit.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(AddNewAccountActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onSignupSuccess();
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
            startActivity(new Intent(AddNewAccountActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.link_login) {
            Intent intent = new Intent(AddNewAccountActivity.this,LoginActivity.class);
            startActivity(intent);

            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        }
    }
    public void onSignupSuccess() {
        bt_submit.setEnabled(true);
        finish();
    }
}

