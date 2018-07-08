package com.moneymemo.adminpc.moneymemo;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.moneymemo.adminpc.moneymemo.model.MonthYearInc;
import com.moneymemo.adminpc.moneymemo.model.ReportIncomeData;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReportIncomeActivity extends AppCompatActivity {

    private ListView listIncomeReport;
    private PieChart chart;
    private Spinner sp_month , sp_year;
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ArrayList<ReportIncomeData> listReportIncomeData = new ArrayList<ReportIncomeData>();
    String selectedM , selectedY;
    Button bt_show;
    TextView tv_sumall;
    String sum_income ;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_income);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        sp_month = (Spinner) findViewById(R.id.sp_month);
        sp_year = (Spinner) findViewById(R.id.sp_year);
        listIncomeReport = (ListView) findViewById(R.id.listIncomeReport);
        bt_show = (Button) findViewById(R.id.bt_show);
        tv_sumall = (TextView) findViewById(R.id.tv_sumAll);
        chart = (PieChart) findViewById(R.id.pie_chart);

        String username = getIntent().getStringExtra("username");

        loadSpinnerData(username);
        loadSpinnerData2(username);

        bt_show.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // TODO Auto-generated method stub

                int pos = sp_month.getSelectedItemPosition();
                int pos2 = sp_year.getSelectedItemPosition();

                if(pos != 0 && pos2 != 0){
                    //customize legends
                    listReportIncomeData.clear();
                    chart.clear();
                    selectedM = sp_month.getSelectedItem().toString();
                    selectedY = sp_year.getSelectedItem().toString();

                    addData();
                    getReportData();
                    showList();

                }
                else{
                    Toast.makeText(ReportIncomeActivity.this , "Please select year and month" , Toast.LENGTH_SHORT).show();
                }

            }
        });


        setListViewHeightBasedOnChildren(listIncomeReport);

    }

    public ArrayList<String> queryXData(String selectedM , String selectedY){

        String username = getIntent().getStringExtra("username");

        ArrayList<String> xNewData = new ArrayList<>();
        String query="SELECT incomedata.income_cid,income_cname FROM incomedata,incomecate" +
                " WHERE strftime('%m' ,income_date ) = ? AND strftime('%Y', income_date) = ? " +
                " AND incomedata.income_cid = incomecate.income_cid AND incomedata.username = ? " +
                "GROUP BY incomedata.income_cid ORDER BY sum(income_amount) DESC ";
        Cursor cursor = database.rawQuery(query,new String[] {""+selectedM,"" + selectedY , ""+ username});
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            xNewData.add(new String( cursor.getString(1)));

        }
        cursor.close();
        return xNewData;
    }

    public ArrayList<Float> queryYData(String selectedM , String selectedY ,String sum_income){
        String username = getIntent().getStringExtra("username");

        Double sum =0.0;
        if( sum_income  != null ) {
            sum = Double.parseDouble(String.valueOf(sum_income));

        }else{ }

        ArrayList<Float> yNewData= new ArrayList<Float>();
        String query="SELECT (sum(income_amount)/?)*100 as percent FROM incomedata" +
                " WHERE strftime('%m' ,income_date ) = ? AND strftime('%Y' ,income_date ) = ? AND username = ? " +
                "GROUP BY income_cid ORDER BY sum(income_amount) DESC ";

        Cursor cursor=database.rawQuery(query,new String[] {""+sum,""+ selectedM , "" + selectedY , ""+ username});
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){

            Float percent = (cursor.getFloat(0));

            yNewData.add(percent);

        }
        cursor.close();
        return yNewData;
    }

    private void  getReportData(){
        selectedM = sp_month.getSelectedItem().toString();
        selectedY = sp_year.getSelectedItem().toString();

        String username = getIntent().getStringExtra("username");
        database = dbHelper.getReadableDatabase();

        String query = "SELECT  sum(income_amount) as sum_income " +
                " FROM  incomedata  " +
                "  WHERE  strftime('%m' ,income_date ) = ? AND strftime('%Y' ,income_date ) = ? AND username = ?   " ;

        Cursor c = database.rawQuery(query,new String[] {""+selectedM, ""+ selectedY, ""+ username});

        if (c.moveToFirst()) {
            do {


                sum_income = c.getString(0);

                DecimalFormat formatter = new DecimalFormat("#,###.00");

                if( sum_income  != null ) {
                    Double sum = Double.parseDouble(String.valueOf(sum_income));
                    chart.setCenterText("Total\n"+ formatter.format(sum));



                }else{

                    chart.setCenterText("Total\n"+ "     0.00");
                }

            } while (c.moveToNext());
        }

    }

    private void addData() {

        ArrayList<PieEntry> yVals = new ArrayList<PieEntry>();

        for (int i = 0; i < queryYData(selectedM,selectedY,sum_income).size() && i < queryXData(selectedM,selectedY).size() ; i++)
            yVals.add(new PieEntry(queryYData(selectedM ,selectedY,sum_income).get(i),queryXData(selectedM,selectedY).get(i)));

        PieDataSet dataSet=new PieDataSet(yVals,"");

        dataSet.setSelectionShift(15);
        dataSet.setValueTextSize(12);
        dataSet.setValueTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));

        ArrayList<Integer> colors=new ArrayList<Integer>();

        for(int c: ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for(int c: ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for(int c: ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueLinePart1Length(0.5f);
        dataSet.setValueLinePart2Length(0.3f);
        dataSet.setValueTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));

        PieData data=new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));

        chart.setData(data);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(45);
        chart.setTransparentCircleRadius(55);
        chart.setCenterTextSize(14);
        chart.setCenterTextColor(Color.rgb(101,208,0));
        chart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));
        //undo all highlights
        chart.highlightValues(null);
        chart.getOnTouchListener().setLastHighlighted(null);
        chart.getDescription().setEnabled(false);
        //update pie chart
        chart.invalidate();
        chart.animateY(3000, Easing.EasingOption.EaseOutBounce);

        Legend l=chart.getLegend();
        l.setEnabled(true);
        l.setPosition((Legend.LegendPosition.BELOW_CHART_CENTER));
        l.setXEntrySpace(7);
        l.setYEntrySpace(7);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));

    }

    public void getReport(String selectedM , String selectedY , String sum_income){

        database = dbHelper.getReadableDatabase();
        String username = getIntent().getStringExtra("username");
        Double sum =0.0;
        if( sum_income  != null ) {
            sum = Double.parseDouble(String.valueOf(sum_income));

        }else{ }

        String query = "SELECT  income_id,incomedata.income_cid,income_cname , (sum(income_amount)/?)*100 as percent , sum(income_amount) as sum_inc " +
                "FROM  incomedata ,incomecate " +
                "WHERE  strftime('%Y' ,income_date ) = ? AND strftime('%m' ,income_date ) = ? AND incomedata.username = ? AND incomedata.income_cid = incomecate.income_cid " +
                "GROUP BY  incomedata.income_cid ORDER BY  sum_inc DESC";

        Cursor mCursor = database.rawQuery(query, new String[]{""+sum ,""+ selectedY ,"" + selectedM, "" + username});

        if (mCursor != null) {
            mCursor.moveToFirst();

            if (mCursor.getCount() > 0) {
                do {

                    int income_id = mCursor.getInt(mCursor.getColumnIndex("income_id"));
                    int income_cid = mCursor.getInt(mCursor.getColumnIndex("income_cid"));
                    String income_cname = mCursor.getString(mCursor.getColumnIndex("income_cname"));
                    Double percent = (mCursor.getDouble(mCursor.getColumnIndex("percent")));
                    Double sum_inc = mCursor.getDouble(mCursor.getColumnIndex("sum_inc"));

                    listReportIncomeData .add(new ReportIncomeData(income_id,income_cid,income_cname,percent,sum_inc));

                } while (mCursor.moveToNext());
            }
        }
    }

    private void showList() {

        getReport(selectedM,selectedY,sum_income);

        listIncomeReport.setAdapter(new AdapterListViewReportIncome(this, listReportIncomeData ));

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void loadSpinnerData(String username) {
        DBHelper db = new DBHelper(getApplicationContext());
        List<MonthYearInc> labels = db.getMonthInc(username);

        ArrayAdapter<MonthYearInc> dataAdapter = new ArrayAdapter<MonthYearInc>(this, R.layout.spinner_item, labels);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_month.setAdapter(new NothingSelectedSpinnerAdapter2(dataAdapter,R.layout.contact_nothing_select_exp_report,this));
        dataAdapter.notifyDataSetChanged();

    }

    private void loadSpinnerData2(String username) {
        DBHelper db = new DBHelper(getApplicationContext());
        List<MonthYearInc> labels = db.getYearInc(username);

        ArrayAdapter<MonthYearInc> dataAdapter = new ArrayAdapter<MonthYearInc>(this, R.layout.spinner_item, labels);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_year.setAdapter(new NothingSelectedSpinnerAdapter2(dataAdapter,R.layout.contact_spinner_nothing_selected_2,this));
        dataAdapter.notifyDataSetChanged();

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
                Intent i = new Intent(ReportIncomeActivity.this, MainActivity.class);
                i.putExtra("username", username);
                startActivity(i);
                return true;

            case R.id.Income:
                Intent i1 = new Intent(ReportIncomeActivity.this, IncomeActivity.class);
                i1.putExtra("username", username);
                startActivity(i1);
                return true;

            case R.id.Expense:
                Intent i2 = new Intent(ReportIncomeActivity.this, ExpenseActivity.class);
                i2.putExtra("username", username);
                startActivity(i2);
                return true;

            case R.id.Report:
                Intent i3 = new Intent(ReportIncomeActivity.this, OptionReportActivity.class);
                i3.putExtra("username", username);
                startActivity(i3);
                return true;

            case R.id.Forecast:
                Intent i4 = new Intent(ReportIncomeActivity.this, ForecastActivity.class);
                i4.putExtra("username", username);
                startActivity(i4);
                return true;
            case R.id.Setting:
                Intent i5 = new Intent(ReportIncomeActivity.this, SettingActivity.class);
                i5.putExtra("username", username);
                startActivity(i5);
                return true;
            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ReportIncomeActivity.this,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(ReportIncomeActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(ReportIncomeActivity.this,LoginActivity.class);
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