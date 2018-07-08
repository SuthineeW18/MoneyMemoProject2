package com.moneymemo.adminpc.moneymemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.moneymemo.adminpc.moneymemo.model.ReportIncomeData;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterListViewReportIncome extends BaseAdapter{

    private LayoutInflater mInflater;
    private Context context;
    private ReportIncomeActivity control;
    private ArrayList<ReportIncomeData> listReportIncomeData = new ArrayList<ReportIncomeData>();

    public AdapterListViewReportIncome(ReportIncomeActivity control, ArrayList<ReportIncomeData> listReportIncomeData) {
        this.control = control;
        this.context = control.getBaseContext();
        this.mInflater = LayoutInflater.from(context);
        this.listReportIncomeData = listReportIncomeData;
    }

    public int getCount() {
        //ส่งขนาดของ List ที่เก็บข้อมุลอยู่
        return listReportIncomeData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderListAdapterReportInc holderListAdapterReportInc; //เก็บส่วนประกอบของ List แต่ละอัน

        if(convertView == null)
        {

            convertView = mInflater.inflate(R.layout.activity_adapter_list_view_report_income, null);

            //สร้างตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapterReportInc = new  HolderListAdapterReportInc ();

            //เชื่อมส่วนประกอบต่างๆ ของ List เข้ากับ View
            holderListAdapterReportInc.txtIncCate = (TextView) convertView.findViewById(R.id.txtIncCate);
            holderListAdapterReportInc.txtPercent = (TextView) convertView.findViewById(R.id.txtPercent);
            holderListAdapterReportInc.txtSum = (TextView) convertView.findViewById(R.id.txtSum);

            convertView.setTag(  holderListAdapterReportInc);
        }else{
            holderListAdapterReportInc = (HolderListAdapterReportInc) convertView.getTag();
        }

        //ดึงข้อมูลจาก listData มาแสดงทีละ position
        final int income_id = listReportIncomeData.get(position).getIncome_id();
        final int income_cid = listReportIncomeData.get(position).getIncome_cid ();
        final String income_cname = listReportIncomeData.get(position).getIncome_cname();
        final Double percent = listReportIncomeData.get(position).getPercent();
        final Double sum_inc = listReportIncomeData.get(position).getSum_inc();


        DecimalFormat formatter = new DecimalFormat("#,###.00");

        if( sum_inc != null ) {

            holderListAdapterReportInc.txtSum.setText( formatter.format( sum_inc)+"฿");

        }else{

        }

        holderListAdapterReportInc.txtIncCate.setText(income_cname);
        holderListAdapterReportInc.txtPercent.setText(String.format("%.1f",percent) +"%");

        return convertView;
    }

}
