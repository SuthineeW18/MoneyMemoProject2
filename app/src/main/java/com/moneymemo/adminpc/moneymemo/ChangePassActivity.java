package com.moneymemo.adminpc.moneymemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ChangePassActivity extends AppCompatActivity {
    private EditText cur_pass;
    private EditText new_pass;
    private EditText confirm_newpass;
    private Button btnChangepass;
    private String curPass;
    private String newPass;
    private String cfnewPass;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        cur_pass = (EditText) findViewById(R.id.cur_pass);
        new_pass = (EditText) findViewById(R.id.new_pass);
        confirm_newpass = (EditText) findViewById(R.id.confirm_newpass);
        btnChangepass = (Button)findViewById(R.id.btnChangepass);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        btnChangepass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                curPass = cur_pass.getText().toString().trim();
                newPass = new_pass.getText().toString().trim();
                cfnewPass = confirm_newpass.getText().toString().trim();

                if( curPass ==null||"".equalsIgnoreCase( curPass)){
                    Toast.makeText(getApplicationContext(),"กรุณากรอกรหัสผ่านปัจจุบัน",Toast.LENGTH_LONG).show();
                }
                else if(newPass==null ||"".equalsIgnoreCase(newPass)){

                    Toast.makeText(getApplicationContext(),"กรุณากรอกรหัสผ่านใหม่",Toast.LENGTH_LONG).show();
                }
                else if(cfnewPass==null ||"".equalsIgnoreCase(cfnewPass)){

                    Toast.makeText(getApplicationContext(),"กรุณากรอกยืนยันรหัสผ่านใหม่",Toast.LENGTH_LONG).show();
                }
                else if(!newPass.equalsIgnoreCase(cfnewPass)){

                    Toast.makeText(getApplicationContext(),"รหัสผ่านใหม่ไม่ตรงกัน",Toast.LENGTH_LONG).show();
                }
                else{
                    ContentValues values = new ContentValues();
                    values.put("password",newPass);
                    final String username = getIntent().getStringExtra("username");

                    db.update("userdata", values, "username = ? ", new String[]{"" + username});
                    Toast.makeText(getApplicationContext(),"Change password successful",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangePassActivity.this, MainActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
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
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                return true;
            case R.id.Home:
                Intent i = new Intent(ChangePassActivity.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(ChangePassActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(ChangePassActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(ChangePassActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(ChangePassActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(ChangePassActivity.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassActivity.this, R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(ChangePassActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(ChangePassActivity.this,LoginActivity.class);
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
