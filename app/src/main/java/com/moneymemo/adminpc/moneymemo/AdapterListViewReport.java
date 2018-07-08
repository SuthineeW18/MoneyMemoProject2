package com.moneymemo.adminpc.moneymemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.moneymemo.adminpc.moneymemo.model.ReportData;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterListViewReport extends BaseAdapter{

    private LayoutInflater mInflater;
    private Context context;
    private ReportActivity control;
    private ArrayList<ReportData> listReportData = new ArrayList<ReportData>();

    public AdapterListViewReport(ReportActivity control, ArrayList<ReportData> listReportData) {
        this.control = control;
        this.context = control.getBaseContext();
        this.mInflater = LayoutInflater.from(context);
        this.listReportData = listReportData;
    }

    public int getCount() {
        //ส่งขนาดของ List ที่เก็บข้อมุลอยู่
        return listReportData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderListAdapterReport holderListAdapterReport; //เก็บส่วนประกอบของ List แต่ละอัน

        if(convertView == null)
        {

            convertView = mInflater.inflate(R.layout.adapter_listview_report, null);

            //สร้างตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapterReport = new  HolderListAdapterReport ();

            //เชื่อมส่วนประกอบต่างๆ ของ List เข้ากับ View
            holderListAdapterReport.txtExpCate = (TextView) convertView.findViewById(R.id.txtExpCate);
            holderListAdapterReport.txtPercent = (TextView) convertView.findViewById(R.id.txtPercent);
            holderListAdapterReport.txtSum = (TextView) convertView.findViewById(R.id.txtSum);

            convertView.setTag( holderListAdapterReport);

        }else{
            holderListAdapterReport = (HolderListAdapterReport) convertView.getTag();
        }

        //ดึงข้อมูลจาก listData มาแสดงทีละ position
        final int exp_id = listReportData.get(position).getExp_id();
        final int exp_cid = listReportData.get(position).getExp_cid ();
        final String exp_cname = listReportData.get(position).getExp_cname();
        final Double percent = listReportData.get(position).getPercent();
        final Double sum_expense = listReportData.get(position).getSum_expense();

        DecimalFormat formatter = new DecimalFormat("#,###.00");

        if( sum_expense != null ) {

            holderListAdapterReport.txtSum.setText( formatter.format( sum_expense)+"฿");

        }else{

        }

        holderListAdapterReport.txtExpCate.setText(exp_cname);
        holderListAdapterReport.txtPercent.setText(String.format("%.1f",percent) +"%");

        return convertView;
    }

}
