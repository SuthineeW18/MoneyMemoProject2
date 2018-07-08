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
import com.moneymemo.adminpc.moneymemo.model.ExpensecateData;

public class AdapterListViewDataExpcate extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private ExpensecateActivity control;
    private ArrayList<ExpensecateData> listExpenseData = new ArrayList<ExpensecateData>();

    public AdapterListViewDataExpcate  (ExpensecateActivity control,ArrayList<ExpensecateData> listExpenseData) {
        this.control = control;
        this.context = control.getBaseContext();
        this.mInflater = LayoutInflater.from(context);
        this.listExpenseData = listExpenseData;
    }

    public int getCount() {
        //ส่งขนาดของ List ที่เก็บข้อมุลอยู่
        return listExpenseData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderListAdapterExpcate holderListAdapterExpcate; //เก็บส่วนประกอบของ List แต่ละอัน

        if(convertView == null)
        {

            convertView = mInflater.inflate(R.layout.adapter_listview_expcate, null);

            //สร้างตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapterExpcate = new  HolderListAdapterExpcate();

            //เชื่อมส่วนประกอบต่างๆ ของ List เข้ากับ View
            holderListAdapterExpcate.txtExpCname = (TextView) convertView.findViewById(R.id.txtExpCname);
            holderListAdapterExpcate.btnEditExpCate = (Button) convertView.findViewById(R.id.btnEditExpCate);
            holderListAdapterExpcate.btnDeleteExpCate = (Button) convertView.findViewById(R.id.btnDeleteExpCate);

            convertView.setTag( holderListAdapterExpcate);
        }else{

            holderListAdapterExpcate = (HolderListAdapterExpcate) convertView.getTag();

        }

        //ดึงข้อมูลจาก listData มาแสดงทีละ position
        final int exp_cid = listExpenseData.get(position).getExp_cid();
        final String exp_cname = listExpenseData.get(position).getExp_cname();
        holderListAdapterExpcate.txtExpCname.setText(exp_cname);

        //สร้าง Event ให้ปุ่ม Delete
        holderListAdapterExpcate.btnDeleteExpCate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(control,R.style.AppTheme_Dark_Dialog);
                    builder.setTitle("Info");
                    builder.setMessage("Do you want to delete your expense category ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            control.deleteExpenseCategory(exp_cid);

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
        holderListAdapterExpcate.btnEditExpCate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                control.showEdit(exp_cid, exp_cname);
            }
        });

        return convertView;
    }
}
