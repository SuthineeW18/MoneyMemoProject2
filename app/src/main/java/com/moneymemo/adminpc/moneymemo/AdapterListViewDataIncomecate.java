package com.moneymemo.adminpc.moneymemo;

import java.util.ArrayList;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Button;
import com.moneymemo.adminpc.moneymemo.model.IncomecateData;

public class AdapterListViewDataIncomecate extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private IncomecateActivity control;
    private ArrayList<IncomecateData> listIncomeData = new ArrayList<IncomecateData>();

    public AdapterListViewDataIncomecate  (IncomecateActivity control,ArrayList<IncomecateData> listIncomeData) {
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
        HolderListAdapterInccate holderListAdapterInccate; //เก็บส่วนประกอบของ List แต่ละอัน

        if(convertView == null)
        {

            convertView = mInflater.inflate(R.layout.adapter_listview_incomecate, null);

            //สร้างตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapterInccate = new  HolderListAdapterInccate();

            //เชื่อมส่วนประกอบต่างๆ ของ List เข้ากับ View
            holderListAdapterInccate.txtIncCname = (TextView) convertView.findViewById(R.id.txtIncCname);
            holderListAdapterInccate.btnEditIncCate = (Button) convertView.findViewById(R.id.btnEditIncCate);
            holderListAdapterInccate.btnDeleteIncCate = (Button) convertView.findViewById(R.id.btnDeleteIncCate);

            convertView.setTag(  holderListAdapterInccate );
        }else{
            holderListAdapterInccate  = ( HolderListAdapterInccate ) convertView.getTag();
        }

        //ดึงข้อมูลจาก listData มาแสดงทีละ position
        final int income_cid = listIncomeData.get(position).getIncome_cid();
        final String income_cname = listIncomeData.get(position).getIncome_cname();
        holderListAdapterInccate.txtIncCname.setText(income_cname);

        //สร้าง Event ให้ปุ่ม Delete
        holderListAdapterInccate.btnDeleteIncCate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(control,R.style.AppTheme_Dark_Dialog);
                builder.setTitle("Info");
                builder.setMessage("Do you want to delete your income category ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        control.deleteIncomeCategory(income_cid);

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
        holderListAdapterInccate.btnEditIncCate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                control.showEdit(income_cid, income_cname);
            }
        });

        return convertView;
    }

}
