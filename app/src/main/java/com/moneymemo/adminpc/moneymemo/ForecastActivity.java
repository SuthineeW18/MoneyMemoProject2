package com.moneymemo.adminpc.moneymemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.moneymemo.adminpc.moneymemo.model.MonthYearInc;
import com.moneymemo.adminpc.moneymemo.utils.DBHelper;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import static android.view.View.TEXT_ALIGNMENT_CENTER;

public class ForecastActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Spinner sp_year;
    private String selectYear ;
    private Button bt_show;
    private TableLayout tb_layout;
    private TableLayout tb_forecast;
    private ArrayList<String> month = new ArrayList<String>();
    private ArrayList<Float> average = new ArrayList<Float>();
    private ArrayList<Float> fc_balace = new ArrayList<Float>();
    private ArrayList<Float> average_rs = new ArrayList<Float>();
    private ArrayList<String> forecast = new ArrayList<String>();
    private TextView tv_title;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();

        String username = getIntent().getStringExtra("username");

        tb_layout = (TableLayout) findViewById(R.id.tb_layout);
        sp_year = (Spinner) findViewById(R.id.sp_year);
        bt_show =(Button) findViewById(R.id.bt_show);
        tb_forecast = (TableLayout) findViewById(R.id.tb_forecast);
        tv_title = (TextView) findViewById(R.id.tv_title);

        loadSpinnerData(username);

        bt_show.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onClick(View v) {


                LineChart lineChart = (LineChart) findViewById(R.id.line_chart);

                int pos = sp_year.getSelectedItemPosition();

                if(pos == 0){
                    Toast.makeText(ForecastActivity.this, "Please select year" , Toast.LENGTH_SHORT).show();
                    return;
                }

                if( pos != 0 && getCount() >= 3 ){

                    lineChart.clear();
                    addData(selectYear);
                    Legend l = lineChart.getLegend();
                    l.setForm(Legend.LegendForm.LINE);
                    l.setTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));

                    tv_title.setText("พยากรณ์ยอดเงินคงเหลือในปี " + sp_year.getSelectedItem().toString());

                }
                if( pos != 0 && getCount() < 3 ){
                    Toast.makeText(ForecastActivity.this,"Not enough data" ,Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    public int getCount() {
        selectYear = sp_year.getSelectedItem().toString();

        String username = getIntent().getStringExtra("username");
        Cursor c = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "select count(distinct strftime('%m',income_date)) from incomedata where username = ?" +
                    " and strftime('%Y' , income_date) = ? ";
            c = db.rawQuery(query, new String[]{username , selectYear});

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

    private void loadSpinnerData(String username) {
        DBHelper db = new DBHelper(getApplicationContext());

        List<MonthYearInc> labels = db.getYearData(username);

        ArrayAdapter<MonthYearInc> dataAdapter = new ArrayAdapter<MonthYearInc>(this, R.layout.spinner_item, labels);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_year.setAdapter(new NothingSelectedSpinnerAdapter(dataAdapter,R.layout.contact_spinner_nothing_selected_2,this));
        dataAdapter.notifyDataSetChanged();
    }


    public List<Float> getAllLabelsIncomeSum(String selectYear){

        List<Float> labels = new ArrayList<Float>();
        String username = getIntent().getStringExtra("username");

        String selectQuery = "SELECT sum(income_amount) " +
                "FROM incomedata  " +
                "WHERE incomedata.username = ? AND strftime('%Y', income_date) = ?  " +
                "GROUP BY strftime('%Y-%m', income_date) ORDER BY strftime('%Y-%m', income_date)";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{"" + username, "" + selectYear});

        if (cursor.moveToFirst()) {
            do {

                labels.add(new Float(cursor.getFloat(0)));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return labels;

    }

    public List<Float> getAllLabelsExpSum(String selectYear){

        List<Float> labels = new ArrayList<Float>();
        String username = getIntent().getStringExtra("username");

        String selectQuery =  "SELECT sum(exp_amount) " +
                "FROM expensedata  " +
                "WHERE expensedata.username = ? AND strftime('%Y', exp_date) = ? " +
                "GROUP BY strftime('%Y-%m', exp_date) ORDER BY strftime('%Y-%m', exp_date)";

        Cursor cursor = db.rawQuery(selectQuery, new String[] {""+ username, "" + selectYear});

        if (cursor.moveToFirst()) {
            do {

                labels.add(new Float( cursor.getFloat(0)));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return labels;
    }

    public List<String> getMonth(String selectYear){

        List<String> months = new ArrayList<String>();
        String username = getIntent().getStringExtra("username");

        String selectQuery =  "SELECT strftime('%m' , income_date) " +
                "FROM incomedata  " +
                "WHERE incomedata.username = ? AND strftime('%Y', income_date) = ? " +
                "GROUP BY strftime('%Y-%m', income_date) ORDER BY strftime('%Y-%m', income_date) ASC";

        Cursor cursor = db.rawQuery(selectQuery, new String[] {""+ username, "" + selectYear});

        if (cursor.moveToFirst()) {
            do {

                months.add((new String( cursor.getString(0))));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return months;
    }

    public ArrayList<String> queryXData(){

        ArrayList<String> xNewData = new ArrayList<>();
        String username = getIntent().getStringExtra("username");

        xNewData.add("Jan");
        xNewData.add("Feb");
        xNewData.add("Mar");
        xNewData.add("Apr");
        xNewData.add("May");
        xNewData.add("Jun");
        xNewData.add("Jul");
        xNewData.add("Aug");
        xNewData.add("Sep");
        xNewData.add("Oct");
        xNewData.add("Nov");
        xNewData.add("Dec");
        return xNewData;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("WrongConstant")
    private void addData(String selectYear) {
        LineChart lineChart = (LineChart) findViewById(R.id.line_chart);
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<Float> balanceData = new ArrayList<Float>();
        ArrayList<Float> balanceData2 = new ArrayList<Float>();

        int income_size = getAllLabelsIncomeSum(selectYear).size();
        int expense_size = getAllLabelsExpSum(selectYear).size();

        if( getAllLabelsExpSum(selectYear).size() < getAllLabelsIncomeSum(selectYear).size() ){
            for (int k = 0; k < getAllLabelsIncomeSum(selectYear).size() && k < getAllLabelsExpSum(selectYear).size(); k++) {
                balanceData.add(new Float(getAllLabelsIncomeSum(selectYear).get((income_size - 2) + (k - 2)) - getAllLabelsExpSum(selectYear).get(k)));
            }
            balanceData.add((getAllLabelsIncomeSum(selectYear).get(income_size-1) - 0));

        }
        if(getAllLabelsIncomeSum(selectYear).size() < getAllLabelsExpSum(selectYear).size() ){
            for (int k = 0; k < getAllLabelsIncomeSum(selectYear).size() && k < getAllLabelsExpSum(selectYear).size(); k++) {
                balanceData.add(new Float(getAllLabelsIncomeSum(selectYear).get(k) - getAllLabelsExpSum(selectYear).get((expense_size - 2) + (k - 2))));
            }
            balanceData.add((getAllLabelsExpSum(selectYear).get(expense_size-1)));
        }
        if(getAllLabelsExpSum(selectYear).size() == getAllLabelsIncomeSum(selectYear).size()) {
            for (int k = 0; k < getAllLabelsIncomeSum(selectYear).size() && k < getAllLabelsExpSum(selectYear).size(); k++) {
                balanceData.add(new Float(getAllLabelsIncomeSum(selectYear).get(k) - getAllLabelsExpSum(selectYear).get(k)));
            }
        }

        if( getAllLabelsExpSum(selectYear).size() < getAllLabelsIncomeSum(selectYear).size() ){
            for (int k = 0; k < getAllLabelsIncomeSum(selectYear).size() && k < getAllLabelsExpSum(selectYear).size(); k++) {
                balanceData2.add(new Float(getAllLabelsIncomeSum(selectYear).get((income_size - 2) + (k - 2)) - getAllLabelsExpSum(selectYear).get(k)));
            }
            balanceData2.add((getAllLabelsIncomeSum(selectYear).get(income_size-1) - 0));

        }
        if(getAllLabelsIncomeSum(selectYear).size() < getAllLabelsExpSum(selectYear).size() ){
            for (int k = 0; k < getAllLabelsIncomeSum(selectYear).size() && k < getAllLabelsExpSum(selectYear).size(); k++) {
                balanceData2.add(new Float(getAllLabelsIncomeSum(selectYear).get(k) - getAllLabelsExpSum(selectYear).get((expense_size - 2) + (k - 2))));
            }
            balanceData2.add((getAllLabelsExpSum(selectYear).get(expense_size-1)));
        }
        if(getAllLabelsExpSum(selectYear).size() == getAllLabelsIncomeSum(selectYear).size()) {
            for (int k = 0; k < getAllLabelsIncomeSum(selectYear).size() && k < getAllLabelsExpSum(selectYear).size(); k++) {
                balanceData2.add(new Float(getAllLabelsIncomeSum(selectYear).get(k) - getAllLabelsExpSum(selectYear).get(k)));
            }
        }

        String min_n = getMonth(selectYear).get(0);
        int min = Integer.parseInt(min_n.replaceFirst("^0*",""));

        if (min == 1) {
            for (int i = 0; i < 3; i++) {

                average.add(balanceData.get(i));
            }
        }
        if (min == 2) {
            average.add((float) 0);
            for (int i = 0; i < 3; i++) {
                average.add(balanceData.get(i));
            }
        }
        if (min == 3) {
            average.add((float) 0);
            average.add((float) 0);
            for (int i = 0; i < 3; i++) {
                average.add(balanceData.get(i));
            }
        }
        if (min == 4) {
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            for (int i = 0; i < 3; i++) {
                average.add(balanceData.get(i));
            }
        }
        if (min == 5) {
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            for (int i = 0; i < 3; i++) {
                average.add(balanceData.get(i));
            }
        }
        if (min == 6) {
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            for (int i = 0; i < 3; i++) {
                average.add(balanceData.get(i));
            }
        }
        if (min == 7) {
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            for (int i = 0; i < 3; i++) {
                average.add(balanceData.get(i));
            }
        }
        if (min == 8) {
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            for (int i = 0; i < 3; i++) {
                average.add(balanceData.get(i));
            }
        }
        if (min == 9) {
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            average.add((float) 0);
            for (int i = 0; i < 3; i++) {
                average.add(balanceData.get(i));
            }
        }else {

        }

        //Forecasting
        for (int i = 0; i+3 <= balanceData.size(); i++) {
            float sum = 0;


            sum = balanceData.get(i)*1 + balanceData.get(i+1)*2 + balanceData.get(i+2)*3;

            average.add(sum / 6);

        }

        int count = getMonth(selectYear).size();
        String month_fc = getMonth(selectYear).get(count-1);
        int max = Integer.parseInt(month_fc.replaceFirst("^0*",""));

        if(max == 3 ){
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            if (average.get(3) != null) {
                if (average.get(3) != 0) {
                    forecast.add("ยอดเงินคงเหลือในเดือนเมษายน คือ  " + formatter.format(Math.round(average.get(3))) + "  บาท");
                } else {
                    forecast.add("ยอดเงินคงเหลือในเดือนเมษายน คือ  0.00  บาท");
                }
            }

        }
        if(max == 4  ) {
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            if (average.get(4) != null) {
                if (average.get(4) != 0) {
                    forecast.add("ยอดเงินคงเหลือในเดือนพฤษภาคม คือ  " + formatter.format(Math.round(average.get(4))) + "  บาท");

                } else {
                    forecast.add("ยอดเงินคงเหลือในเดือนพฤษภาคม คือ  0.00  บาท");

                }
            }
        }
        if(max == 5 ){
            DecimalFormat formatter = new DecimalFormat("#,###.00");


            if (average.get(5) != null) {
                if (average.get(5) != 0) {
                    forecast.add("ยอดเงินคงเหลือในเดือนมิถุนายน คือ  " + formatter.format(Math.round(average.get(5))) + "  บาท");

                } else {
                    forecast.add("ยอดเงินคงเหลือในเดือนมิถุนายน คือ  0.00  บาท");

                }
            }

        }
        if(max == 6 ){
            DecimalFormat formatter = new DecimalFormat("#,###.00");


            if (average.get(6) != null) {
                if (average.get(6) != 0) {
                    forecast.add("ยอดเงินคงเหลือในเดือนกรกฎาคม คือ  " + formatter.format(Math.round(average.get(6))) + "  บาท");

                } else {
                    forecast.add("ยอดเงินคงเหลือในเดือนกรกฎาคม คือ  0.00  บาท");

                }
            }

        }
        if(max == 7 ){
            DecimalFormat formatter = new DecimalFormat("#,###.00");


            if (average.get(7) != null) {
                if (average.get(7) != 0) {
                    forecast.add("ยอดเงินคงเหลือในเดือนสิงหาคม คือ  " + formatter.format(Math.round(average.get(7))) + "  บาท");

                } else {
                    forecast.add("ยอดเงินคงเหลือในเดือนสิงหาคม คือ  0.00  บาท");

                }
            }

        }
        if(max == 8 ) {
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            if (average.get(8) != null) {
                if (average.get(8) != 0) {
                    forecast.add("ยอดเงินคงเหลือในเดือนกันยายน คือ  " + formatter.format(Math.round(average.get(8))) + "  บาท");

                } else {
                    forecast.add("ยอดเงินคงเหลือในเดือนกันยายน คือ  0.00  บาท");

                }

            }

        }
        if(max == 9 ){
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            if (average.get(9) != null) {
                if (average.get(9) != 0) {
                    forecast.add("ยอดเงินคงเหลือในเดือนตุลาคม คือ  " + formatter.format(Math.round(average.get(9))) + "  บาท");

                } else {
                    forecast.add("ยอดเงินคงเหลือในเดือนตุลาคม คือ  0.00  บาท");

                }
            }

        }
        if(max == 10 ){
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            if (average.get(10) != null) {
                if (average.get(10) != 0) {
                    forecast.add("ยอดเงินคงเหลือในเดือนพฤศจิกายน คือ  " + formatter.format(Math.round(average.get(10))) + "  บาท");

                } else {
                    forecast.add("ยอดเงินคงเหลือในเดือนพฤศจิกายน คือ  0.00  บาท");

                }
            }
        }
        if(max == 11 ){
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            if (average.get(11) != null) {
                if (average.get(11) != 0) {
                    forecast.add("ยอดเงินคงเหลือในเดือนธันวาคม คือ  " + formatter.format(Math.round(average.get(11))) + "  บาท");

                } else {
                    forecast.add("ยอดเงินคงเหลือในเดือนธันวาคม คือ  0.00  บาท");

                }
            }

        }

        //Forecast result
        for(int i = 0 ; i < forecast.size() ; i++) {
            TableRow tb_row = new TableRow(this);
            TextView tvForecast = new TextView(this);
            tvForecast.setTextColor(Color.DKGRAY);
            String st_forecast;

            st_forecast = forecast.get(i);


            tvForecast.setTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));
            tvForecast.setText("" + st_forecast);
            tvForecast.setTextSize(10);
            //  tvForecast.setTextColor(Color.rgb(255,20,85));


            tb_row.addView(tvForecast);
            tb_forecast.addView(tb_row);

        }

        for (int i = 0; i+3 <= balanceData.size(); i++) {
            float sum = 0;


            sum = balanceData.get(i)*1 + balanceData.get(i+1)*2 + balanceData.get(i+2)*3;

            average_rs.add(sum / 6);

        }

        if (min == 1) {
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);

            for (int i = 0; i < average_rs.size(); i++) {
                fc_balace.add(average_rs.get(i));
            }
        }
        if (min == 2) {
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);

            for (int i = 0; i < average_rs.size(); i++) {
                fc_balace.add(average_rs.get(i));
            }
        }
        if (min == 3) {
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);


            for (int i = 0; i < average_rs.size(); i++) {
                fc_balace.add(average_rs.get(i));
            }
        }
        if (min == 4) {
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);

            for (int i = 0; i < average_rs.size(); i++) {
                fc_balace.add(average_rs.get(i));
            }
        }
        if (min == 5) {
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);

            for (int i = 0; i < average_rs.size(); i++) {
                fc_balace.add(average_rs.get(i));
            }
        }
        if (min == 6) {
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);


            for (int i = 0; i < average_rs.size(); i++) {
                fc_balace.add(average_rs.get(i));
            }
        }
        if (min == 7) {
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);


            for (int i = 0; i < average_rs.size(); i++) {
                fc_balace.add(average_rs.get(i));
            }
        }
        if (min == 8) {
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);


            for (int i = 0; i < average_rs.size(); i++) {
                fc_balace.add(average_rs.get(i));
            }
        }
        if (min == 9) {
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);
            fc_balace.add((float) 0);

            for (int i = 0; i < average_rs.size(); i++) {
                fc_balace.add(average_rs.get(i));
            }
        }

        ArrayList<Float> realData = new ArrayList<Float>();
        ArrayList<Float> Error = new ArrayList<Float>();
        if (min == 1) {
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        }
        if (min == 2) {
            realData.add((float) 0);
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        }
        if (min == 3) {
            realData.add((float) 0);
            realData.add((float) 0);
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        }
        if (min == 4) {
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        }
        if (min == 5) {
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        }
        if (min == 6) {
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        }
        if (min == 7) {
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        }
        if (min == 8) {
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        }
        if (min == 9) {
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        } if (min == 10) {
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        } if (min == 11) {
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            for (int i = 0; i < balanceData.size(); i++) {
                realData.add(balanceData.get(i));
            }
            for (int i = 0; i < realData.size(); i++) {
                Error.add((realData.get(i)-fc_balace.get(i)/realData.get(i)*100));
            }
        } if (min == 12) {
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add((float) 0);
            realData.add(balanceData.get(11));

        }
        for(int i = 0 ; i < realData.size() ; i++) {
            month.add(queryXData().get(i));

        }

        for(int i = 0 ; i < month.size() &&  i < realData.size() && i < fc_balace.size() && i < Error.size() ; i++){
            TableRow row = new TableRow(this);

            String monthData = month.get(i);
            double balance,actual,error;
            actual = realData.get(i);
            balance = fc_balace.get(i);
            error = Error.get(i);

            row.setPadding(0,5,0,5);

            TextView tvBalance = new TextView(this);
            TextView tvMonth = new TextView(this);
            TextView tvActual = new TextView(this);
            TextView tvError = new TextView(this);
            tvBalance.setTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));
            tvMonth.setTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));
            tvActual.setTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));
            tvError.setTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));

            tvMonth.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tvActual.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            tvBalance.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            tvError.setTextAlignment(TEXT_ALIGNMENT_CENTER);

            tvMonth.setGravity(Gravity.CENTER_HORIZONTAL);
            tvBalance.setGravity(Gravity.CENTER_HORIZONTAL);
            tvActual.setGravity(Gravity.CENTER_HORIZONTAL);
            tvError.setGravity(Gravity.CENTER_HORIZONTAL);

            tvMonth.setTextSize(12);
            tvActual.setTextSize(12);
            tvBalance.setTextSize(12);
            tvError.setTextSize(12);

            tvBalance.setTextColor(Color.rgb(255,20,85));
            tvActual.setTextColor(Color.DKGRAY);
            tvMonth.setTextColor(Color.DKGRAY);
            tvError.setTextColor(Color.RED);


            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0,0,0);
            tvBalance.setLayoutParams(lp);

            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.setMargins(0,0,0,0);
            tvActual.setLayoutParams(lp2);

            DecimalFormat formatter = new DecimalFormat("#,###.00");
            DecimalFormat formatter2 = new DecimalFormat("#,###.##");
            tvBalance.setText(""+ formatter.format(Math.round(balance)));
            tvActual.setText("" + formatter.format(Math.round(actual)));
            tvMonth.setText("" + monthData);
            tvError.setText(""+ formatter2.format(error));

            tvMonth.setLayoutParams(new TableRow.LayoutParams(0));
            tvBalance.setLayoutParams(new TableRow.LayoutParams(2));
            tvActual.setLayoutParams(new TableRow.LayoutParams(1));
            tvError.setLayoutParams(new TableRow.LayoutParams(3));

            if(balance == 0){
                tvBalance.setText("0.00");
            }

            if(actual == 0 ){
                tvActual.setText("0.00");
            }
            if(error == 0){
                tvError.setText("0");
            }
            row.addView(tvMonth);
            row.addView(tvActual);
            row.addView(tvBalance);
            row.addView(tvError);

            tb_layout.addView(row);
        }

        for (int i = 0; i < average.size(); i++) {
            yVals.add(new Entry(i, average.get(i)));

        }

        for(int i = 0 ; i < realData.size() ; i++){
            yVals2.add(new Entry(i , realData.get(i)));

        }

        LineDataSet set = new LineDataSet(yVals2, "ข้อมูลจริง");

        set.setCircleColor(Color.rgb(255,175,227));
        set.setLineWidth(3f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);
        set.setValueTextSize(10f);
        set.setColors(Color.rgb(255,175,227));
        set.setValueTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));
        set.setValueTextColor(Color.BLACK);

        LineDataSet set2 = new LineDataSet(yVals, "ข้อมูลพยากรณ์ปี" + selectYear );
        set2.setLineWidth(3f);
        set2.setCircleRadius(3f);
        set2.setDrawCircleHole(false);
        set2.setValueTextSize(10f);
        set2.setColors(Color.rgb( 184,249,123));
        set2.setCircleColor(Color.rgb(184,249,123));
        set2.setValueTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));
        set2.setValueTextColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set2);
        dataSets.add(set);

        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setLabelRotationAngle(-45);
        lineChart.animateY(1000);
        lineChart.setData(new LineData(dataSets));

        final String [] month1 = new String[] {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        IAxisValueFormatter formatter2 = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {

                return month1[(int) v];
            }

            public int getDecimalDigits(){ return 0;}
        };

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter2);
        xAxis.setTypeface(Typeface.createFromAsset(getAssets(), "font/Chococooky.ttf"));


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

                Intent i = new Intent(ForecastActivity.this,MainActivity.class);
                i.putExtra("username",username);
                startActivity(i);
                return true;

            case R.id.Income:

                Intent i1 = new Intent(ForecastActivity.this,IncomeActivity.class);
                i1.putExtra("username",username);
                startActivity(i1);
                return true;

            case R.id.Expense:

                Intent i2 = new Intent(ForecastActivity.this,ExpenseActivity.class);
                i2.putExtra("username",username);
                startActivity(i2);
                return true;

            case R.id.Report:

                Intent i3 = new Intent(ForecastActivity.this,OptionReportActivity.class);
                i3.putExtra("username",username);
                startActivity(i3);

                return true;

            case R.id.Forecast:

                Intent i4 = new Intent(ForecastActivity.this,ForecastActivity.class);
                i4.putExtra("username",username);
                startActivity(i4);
                return true;

            case R.id.Setting:

                Intent i5 = new Intent(ForecastActivity.this,SettingActivity.class);
                i5.putExtra("username",username);
                startActivity(i5);
                return true;

            case R.id.Logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(ForecastActivity.this , R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(ForecastActivity.this , R.style.AppTheme_Dark2_Dialog);
                        builder2.setMessage("Log out successfully");
                        AlertDialog dialog2 = builder2.create();
                        dialog2.show();
                        new Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(ForecastActivity.this,LoginActivity.class);
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

