package com.moneymemo.adminpc.moneymemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.moneymemo.adminpc.moneymemo.model.MonthYearInc;
import com.moneymemo.adminpc.moneymemo.model.ReportData;
import com.moneymemo.adminpc.moneymemo.model.SpinnerIncome;
import com.moneymemo.adminpc.moneymemo.model.SpinnerObject;
import com.moneymemo.adminpc.moneymemo.model.User;
import com.moneymemo.adminpc.moneymemo.model.Year;
import com.moneymemo.adminpc.moneymemo.model.monthYearData;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper  {

    private static final String DATABASE_NAME = "moneymemo3";
    private static final int DATABASE_VERSION = 3;

    public static final String Expense_Table = "expensedata";
    public static final String income_Table = "incomedata";
    public static final String ExpenseCate_Table = "expensecate";
    public static final String IncomeCate_Table = "incomecate";
    public static final String User_Table = "userdata";




    //คำสั่งในการสร้าง ตารางสำหรับเก็บข้อมูล
    private static final String User = "" +
            "CREATE TABLE userdata (" +"username TEXT(30) PRIMARY KEY , " + "password TEXT(20) NOT NULL , " + "email TEXT(40) NOT NULL);";
    private static final String ExpenseCate = "" +
            "CREATE TABLE expensecate (" + "exp_cid INTEGER PRIMARY KEY AUTOINCREMENT, " +"username TEXT(30) , "+ "exp_cname TEXT(20)  NOT NULL );";
    private static final String IncomeCate = "" +
            "CREATE TABLE incomecate (" + "income_cid INTEGER PRIMARY KEY AUTOINCREMENT, " +"username TEXT(30) , "+ "income_cname TEXT(20)  NOT NULL );";

    private static final String ExpenseData = "" +
            "CREATE TABLE expensedata (" + "exp_id INTEGER PRIMARY KEY AUTOINCREMENT, " +"username TEXT(30),"+ "exp_cid INTEGER , " +
            "exp_amount REAL(15) NOT NULL," + "exp_memo TEXT(30) ," + "exp_date TEXT(10) NOT NULL);";

    private static final String IncomeData = "" +
            "CREATE TABLE incomedata (" + "income_id INTEGER PRIMARY KEY AUTOINCREMENT, " +"username TEXT(30),"+ "income_cid INTEGER , " +
            "income_amount REAL(15) NOT NULL," + "income_memo TEXT(30)," + "income_date TEXT(10) NOT NULL);";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ExpenseCate);
        db.execSQL(IncomeCate);
        db.execSQL(ExpenseData);
        db.execSQL(IncomeData);
        db.execSQL(User);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //พิมพ์ Log เพื่อให้เห็นว่ามีการ Upgrade Database
        Log.w(DBHelper.class.getName(),
                "Upgread database version from version" + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        //ลบตาราง member ของเก่าทิ้ง
        db.execSQL("DROP TABLE IF EXISTS expensecate");
        db.execSQL("DROP TABLE IF EXISTS incomecate");
        db.execSQL("DROP TABLE IF EXISTS expensedata");
        db.execSQL("DROP TABLE IF EXISTS incomedata");
        db.execSQL("DROP TABLE IF EXISTS userdata");

        onCreate(db);
    }

    public long insertUser(String username,String password,String email){
        SQLiteDatabase db = this.getWritableDatabase();
        //long rows = 0;
        ContentValues values = new ContentValues();


       /* String query = "select * from userdata";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();*/

        values.put("username" ,username);
        values.put("password",password);
        values.put("email" , email);

       return  db.insertWithOnConflict("userdata",null, values ,SQLiteDatabase.CONFLICT_IGNORE);


    }
    public void insertIncomeCate(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("income_cname","Others");
        values.put("username",username);

        db.insert("incomecate",null, values);

        values.put("income_cname","Salary");
        values.put("username",username);

        db.insert("incomecate",null, values);


    }
    public void insertExpenseCate(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("exp_cname","Others");
        values.put("username",username);

        db.insert("expensecate",null, values);

        values.put("exp_cname","Food");
        values.put("username",username);

        db.insert("expensecate",null, values);

        values.put("exp_cname","Daily goods");
        values.put("username",username);

        db.insert("expensecate",null, values);

        values.put("exp_cname","Rent");
        values.put("username",username);

        db.insert("expensecate",null, values);


    }

    public void addExpenseCate(String exp_cname,String username){
        SQLiteDatabase db = this.getWritableDatabase();
        long rows = 0;
        ContentValues values = new ContentValues();

        values.put("exp_cname",exp_cname);
        values.put("username",username);

        db.insert("expensecate",null, values);

    }
    public void addIncomeCate(String txtincome_cname,String username){

        SQLiteDatabase db = this.getWritableDatabase();
        long rows = 0;
        ContentValues values = new ContentValues();
        values.put("income_cname",txtincome_cname);
        values.put("username",username);

        db.insert("incomecate",null, values );



    }

    public void editIncomeCategory(int income_cid,String income_cname){
        //เตรียมค่าต่างๆ เพื่อทำการแก้ไข
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("income_cid",income_cid);
        values.put("income_cname", income_cname);

        //ให้ Database ทำการแก้ไขข้อมูลที่ id ที่กำหนด
       db.update("incomecate", values, "income_cid = ?", new String[] { ""+ income_cid });


    }
    public void editExpenseCategory(int exp_cid,String exp_cname){
        //เตรียมค่าต่างๆ เพื่อทำการแก้ไข
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("exp_cid",exp_cid);
        values.put("exp_cname", exp_cname);

        //ให้ Database ทำการแก้ไขข้อมูลที่ id ที่กำหนด
        db.update("expensecate", values, "exp_cid = ?", new String[] { ""+ exp_cid });


    }


    public String searchPass(String username){
       SQLiteDatabase db = this.getReadableDatabase();
       String query = "select * from "+User_Table;
       Cursor cursor = db.rawQuery(query,null);
       String a , b;
       b ="not found";
       if(cursor.moveToNext())
       {
           do{
               a = cursor.getString(0);

               if(a.equals(username)){
                   b = cursor.getString(1);
                   break;
               }
           }
           while (cursor.moveToNext());
       }

       return b;

    }

    // Count Current Month Income

    public List<SpinnerObject> getAllLabels(String username){
        List<SpinnerObject> labels = new ArrayList<SpinnerObject>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + ExpenseCate_Table +" WHERE username = ? ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {""+ username});//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(new SpinnerObject( cursor.getInt(0), cursor.getString(2)));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<SpinnerIncome> getAllLabelsIncome(String username){
        List<SpinnerIncome> labels = new ArrayList<SpinnerIncome>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + IncomeCate_Table + " WHERE username = ? " ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {""+ username});//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                labels.add(new SpinnerIncome( cursor.getInt(0), cursor.getString(2)));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public List<monthYearData> getMonth(String username){
        List<monthYearData> labels = new ArrayList<monthYearData>();


        // Select All Query
        String selectQuery = "SELECT  strftime('%m' ,exp_date ) AS exp_date FROM "+ Expense_Table + " WHERE username = ? GROUP BY strftime('%m' ,exp_date )  ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {""+ username});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do{
                labels.add(new monthYearData( cursor.getString(0)));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public List<monthYearData> getYear(String username){
        List<monthYearData> labels = new ArrayList<monthYearData>();


        // Select All Query
        String selectQuery = "SELECT  strftime('%Y' ,exp_date ) AS exp_date FROM "+ Expense_Table + " WHERE username = ? GROUP BY strftime('%Y' ,exp_date )  ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {""+ username});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do{
                labels.add(new monthYearData( cursor.getString(0)));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public List<MonthYearInc> getMonthInc(String username){
        List<MonthYearInc> labels = new ArrayList<MonthYearInc>();


        // Select All Query
        String selectQuery = "SELECT  strftime('%m' ,income_date ) AS income_date FROM "+ income_Table + " WHERE username = ? GROUP BY strftime('%m' ,income_date )  ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {""+ username});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do{
                labels.add(new MonthYearInc( cursor.getString(0)));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public List<MonthYearInc> getYearInc(String username){
        List<MonthYearInc> labels = new ArrayList<MonthYearInc>();


        // Select All Query
        String selectQuery = "SELECT  strftime('%Y' ,income_date ) AS income_date FROM "+ income_Table + " WHERE username = ? GROUP BY strftime('%Y' ,income_date )  ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {""+ username});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do{
                labels.add(new MonthYearInc( cursor.getString(0)));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public List<MonthYearInc> getYearData(String username){
        List<MonthYearInc> labels = new ArrayList<MonthYearInc>();


        // Select All Query
        String selectQuery = "SELECT  strftime('%Y' ,income_date )  FROM "+income_Table+" WHERE username = ? GROUP BY strftime('%Y' ,income_date )  ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {""+ username});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do{
                labels.add(new MonthYearInc( cursor.getString(0)));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }



    public String getSumExp(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sum(exp_amount) " +
                "FROM expensedata " +
                "WHERE  expensedata.username = ? " +
                "AND strftime('%Y-%m', exp_date) = strftime('%Y-%m','now') ";
        Cursor cursor = db.rawQuery(query, new String [] {"" + username });
        String sum_expense = null;
        if(cursor.moveToNext())
        {
            do{
                sum_expense = cursor.getString(0);


            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sum_expense;

    }
    public String getSumInc(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sum(income_amount) " +
                "FROM incomedata  " +
                "WHERE incomedata.username = ?  " +
               "AND strftime('%Y-%m', income_date) = strftime('%Y-%m','now')";
        Cursor cursor = db.rawQuery(query, new String [] {"" + username} );
        String sum_income = null;
        if(cursor.moveToNext())
        {
            do{
                sum_income = cursor.getString(0);


            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sum_income;

    }


}

