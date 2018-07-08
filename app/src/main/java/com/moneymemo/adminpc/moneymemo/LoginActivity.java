package com.moneymemo.adminpc.moneymemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button bt_login;
    EditText et_username,et_password;
    TextView tv_regislink,tv_forgetpass;

    private DBHelper dbHelper;

    private SQLiteDatabase database;
    private static int TIME_OUT = 2000;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        tv_regislink = (TextView) findViewById(R.id.tv_regislink);
        tv_forgetpass = (TextView) findViewById(R.id.tv_forgetpass);

        dbHelper = new DBHelper(this);

        bt_login.setOnClickListener(this);
        tv_regislink.setOnClickListener(this);
        tv_forgetpass.setOnClickListener(this);

    }

    public int getCountUsername(){
        String username = et_username.getText().toString();
        Cursor c = null;
        try {
            database = dbHelper.getReadableDatabase();
            String query = "select count(*) from userdata where username = ? ";
            c = database.rawQuery(query, new String[]{username});

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

    @Override
    public void onClick(View v){
        if (v.getId() == R.id.bt_login){
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();

            String pass = dbHelper.searchPass(username);

            if(getCountUsername() > 0 && password.equals(pass)){

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loging in...");
                progressDialog.show();

                // TODO: Implement your own authentication logic here.

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        String username = et_username.getText().toString();
                        onLoginSuccess();
                        progressDialog.dismiss();
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        i.putExtra("username",username);
                        startActivity(i);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                        }
                        },TIME_OUT);
            }
            if(getCountUsername() == 0 ){
                 Toast.makeText(LoginActivity.this,"Username doesn't exist ",Toast.LENGTH_SHORT).show();

                 return;
            }
            if(getCountUsername() > 0 && !password.equals(pass) )
            {
                Toast text = Toast.makeText(LoginActivity.this,"User and password don't match",Toast.LENGTH_SHORT);
                text.show();
                return;
            }
        }
        if(v.getId() == R.id.tv_regislink){

            Intent i = new Intent(LoginActivity.this,AddNewAccountActivity.class);

            startActivity(i);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if(v.getId() == R.id.tv_forgetpass){

            Intent i = new Intent(LoginActivity.this,ForgetPassword.class);

            startActivity(i);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }
    public void onLoginSuccess() {
        bt_login.setEnabled(true);
        finish();
    }

}
