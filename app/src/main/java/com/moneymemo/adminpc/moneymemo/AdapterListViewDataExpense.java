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
import com.moneymemo.adminpc.moneymemo.model.ExpenseData;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterListViewDataExpense extends BaseAdapter{

    private LayoutInflater mInflater;
    private Context context;
    private ExpenseActivity control;
    private ArrayList<ExpenseData> listExpenseData = new ArrayList<ExpenseData>();

    public AdapterListViewDataExpense (ExpenseActivity control, ArrayList<ExpenseData> listExpenseData) {
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
        HolderListAdapterExpense holderListAdapterExpense; //เก็บส่วนประกอบของ List แต่ละอัน

        if(convertView == null)
        {
            //ใช้ Layout ของ List เราเราสร้างขึ้นเอง
            convertView = mInflater.inflate(R.layout.adapter_listview_expense, null);

            //สร้างตัวเก็บส่วนประกอบของ List แต่ละอัน
            holderListAdapterExpense = new  HolderListAdapterExpense ();

            //เชื่อมส่วนประกอบต่างๆ ของ List เข้ากับ View
            holderListAdapterExpense.txtExpCate = (TextView) convertView.findViewById(R.id.txtExpCate);
            holderListAdapterExpense.txtExpDate = (TextView) convertView.findViewById(R.id.txtExpDate);
            holderListAdapterExpense.txtExpAmount = (TextView) convertView.findViewById(R.id.txtExpAmount);
            holderListAdapterExpense.txtExpMemo = (TextView) convertView.findViewById(R.id.txtExpMemo);
            holderListAdapterExpense.btnEditExpense = (Button) convertView.findViewById(R.id.btnEditExpense);
            holderListAdapterExpense.btnDeleteExpense = (Button) convertView.findViewById(R.id.btnDeleteExpense);

            convertView.setTag( holderListAdapterExpense);
        }else{
            holderListAdapterExpense = ( HolderListAdapterExpense) convertView.getTag();
        }

        //ดึงข้อมูลจาก listData มาแสดงทีละ position
        final int exp_id = listExpenseData.get(position).getExp_id();
        final int exp_cid = listExpenseData.get(position).getExp_cid();
        final String exp_cname = listExpenseData.get(position).getExp_cname();
        final String exp_amount = listExpenseData.get(position).getExp_amount();
        final String exp_date = listExpenseData.get(position).getExp_date();
        final String exp_memo = listExpenseData.get(position).getExp_memo();

        if(exp_amount != null) {
            final double ex_amount = Double.parseDouble(exp_amount);
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            holderListAdapterExpense.txtExpAmount.setText(formatter.format(ex_amount)+ "฿");
        }else{

        }

        holderListAdapterExpense.txtExpCate.setText(exp_cname);

        holderListAdapterExpense.txtExpDate.setText(exp_date);
        holderListAdapterExpense.txtExpMemo.setText(exp_memo);


        //สร้าง Event ให้ปุ่ม Delete
        holderListAdapterExpense.btnDeleteExpense.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(control,R.style.AppTheme_Dark_Dialog);
                    builder.setTitle("Info");
                    builder.setMessage("Do you want to delete your expense ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            control.deleteExpense(exp_id);



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
        holderListAdapterExpense.btnEditExpense.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                control.showEdit(exp_id,exp_cid,exp_cname, exp_amount,exp_date,exp_memo);
            }
        });

        return convertView;
    }
}
