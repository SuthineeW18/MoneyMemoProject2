package com.moneymemo.adminpc.moneymemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener{

    private EditText et_username;
    private Button bt_reset;
    private TextView tv_back;
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
        setContentView(R.layout.activity_forget_password);

        dbHelper = new DBHelper(this);

        et_username = (EditText) findViewById(R.id.et_username);
        tv_back = (TextView) findViewById(R.id.tv_back);
        bt_reset = (Button) findViewById(R.id.bt_reset);
        tv_back.setOnClickListener(this);
        bt_reset.setOnClickListener(this);

    }

    public int getCount() {
        String username = et_username.getText().toString();

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

    @Override
    public void onClick(View v){

        String username = et_username.getText().toString();

        if (v.getId() == R.id.bt_reset) {
            if(et_username == null){
                Toast.makeText(ForgetPassword.this , "Please input data" , Toast.LENGTH_SHORT).show();
            }
            if(getCount() == 0 ){
                Toast.makeText(ForgetPassword.this , "Username doesn't exist" , Toast.LENGTH_SHORT).show();

            }
            else{
                Intent i = new Intent(ForgetPassword.this,ResetPassword.class);
                i.putExtra("username",username);
                startActivity(i);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

        }
        if (v.getId() == R.id.tv_back){
            Intent i = new Intent(ForgetPassword.this,LoginActivity.class);

            startActivity(i);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }
}
