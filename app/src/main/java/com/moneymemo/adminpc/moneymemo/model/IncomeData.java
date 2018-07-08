package com.moneymemo.adminpc.moneymemo.model;

/**
 * Created by Admin PC on 15/1/2561.
 */

public class IncomeData {
    private int income_id;
    private int income_cid;
    private String income_cname;
    private String income_amount;
    private String income_date;
    private  String income_memo;

    public IncomeData(int income_id, int income_cid, String income_cname,  String income_amount,String income_date,String income_memo){

        this.income_id = income_id;
        this.income_cid = income_cid;
        this.income_cname = income_cname;
        this.income_amount = income_amount;
        this.income_date = income_date;
        this.income_memo = income_memo;

    }



    public int getIncome_id(){
        return this.income_id;
    }

    public int getIncome_cid(){
        return this.income_cid;
    }
    public String getIncome_cname() { return  this.income_cname;}

    public String getIncome_amount(){
        return this.income_amount;
    }
    public String getIncome_date(){
        return this.income_date;
    }
    public String getIncome_memo(){
        return this.income_memo;
    }

    @Override
    public String toString(){
        return income_cname;
    }
}
