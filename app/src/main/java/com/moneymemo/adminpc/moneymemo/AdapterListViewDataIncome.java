package com.moneymemo.adminpc.moneymemo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.moneymemo.adminpc.moneymemo.model.IncomeData;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterListViewDataIncome extends BaseAdapter{

    private LayoutInflater mInflater;
    private Context context;
    private IncomeActivity control;
    private ArrayList<IncomeData> listIncomeData = new ArrayList<IncomeData>();

    public AdapterListViewDataIncome (IncomeActivity control, ArrayList<IncomeData> listIncomeData) {
        this.control = control;
        this.context = control.getBaseContext();
        this.mInflater = LayoutInflater.from(context);
        this.listIncomeData = listIncomeData;
    }

    public int getCount() {
        //ส่งขนาดของ List ที่เก็บข้อมุลอยู่
        return listIncomeData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderListAdapterIncome holderListAdapterIncome; //เก็บส่วนประกอบของ List แต่ละอัน

        if(convertView == null)
        {

            convertView = mInflater.inflate(R.layout.activity_listview_income, null);

            //สร้างตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapterIncome = new  HolderListAdapterIncome ();
            //เชื่อมส่วนประกอบต่างๆ ของ List เข้ากับ View
            holderListAdapterIncome.txtIncomeCate = (TextView) convertView.findViewById(R.id.txtIncomeCate);
            holderListAdapterIncome.txtIncomeDate = (TextView) convertView.findViewById(R.id.txtIncomeDate);
            holderListAdapterIncome.txtIncomeAmount = (TextView) convertView.findViewById(R.id.txtIncomeAmount);
            holderListAdapterIncome.txtIncomeMemo = (TextView) convertView.findViewById(R.id.txtIncomeMemo);
            holderListAdapterIncome.btnEditIncome = (Button) convertView.findViewById(R.id.btnEditIncome);
            holderListAdapterIncome.btnDeleteIncome = (Button) convertView.findViewById(R.id.btnDeleteIncome);

            convertView.setTag( holderListAdapterIncome);
        }else{
            holderListAdapterIncome = ( HolderListAdapterIncome) convertView.getTag();
        }

        //ดึงข้อมูลจาก listData มาแสดงทีละ position
        final int income_id = listIncomeData.get(position).getIncome_id();
        final int income_cid = listIncomeData.get(position).getIncome_cid();
        final String income_cname = listIncomeData.get(position).getIncome_cname();
        final String income_amount = listIncomeData.get(position).getIncome_amount();
        final String income_date = listIncomeData.get(position).getIncome_date();
        final String income_memo = listIncomeData.get(position).getIncome_memo();

        if(income_amount != null) {
            final double inc_amount = Double.parseDouble(income_amount);
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            holderListAdapterIncome.txtIncomeAmount.setText( formatter.format(inc_amount)+ "฿");
        }else{

        }

        holderListAdapterIncome.txtIncomeCate.setText(income_cname);
        holderListAdapterIncome.txtIncomeDate.setText(income_date);
        holderListAdapterIncome.txtIncomeMemo.setText(income_memo);

        //สร้าง Event ให้ปุ่ม Delete
        holderListAdapterIncome.btnDeleteIncome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(control,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to delete your income ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        control.deleteIncome(income_id);



                    }


                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        //สร้าง Event ให้ปุ่ม Edit
        holderListAdapterIncome.btnEditIncome.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                control.showEdit(income_id,income_cid,income_cname, income_amount,income_date,income_memo);
            }
        });

        return convertView;
    }
}
