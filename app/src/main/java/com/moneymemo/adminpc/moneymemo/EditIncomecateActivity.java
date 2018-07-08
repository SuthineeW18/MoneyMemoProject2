package com.moneymemo.adminpc.moneymemo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditIncomecateActivity extends AppCompatActivity {

    private EditText txtIncCname;
    private Button btnEditIncCate;
    private int income_cid;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_incomecate);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtIncCname = (EditText)findViewById(R.id.txtIncCname);
        btnEditIncCate = (Button)findViewById(R.id.btnEditIncCate);

        this.income_cid = getIntent().getExtras().getInt("keyIncome_cid");
        txtIncCname.setText(getIntent().getExtras().getString("keyIncome_cname"));

        btnEditIncCate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();

                setResult(RESULT_OK,i);

                i.putExtra("keyIncome_cid", income_cid);
                i.putExtra("keyIncome_cname", txtIncCname.getText().toString());

                finish();
            }
        });

        if(txtIncCname.getText().length() > 0){
            txtIncCname.setSelection(txtIncCname.getText().length());
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
                Intent i = new Intent(EditIncomecateActivity.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(EditIncomecateActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(EditIncomecateActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(EditIncomecateActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(EditIncomecateActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(EditIncomecateActivity.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditIncomecateActivity.this,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(EditIncomecateActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(EditIncomecateActivity.this,LoginActivity.class);
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
