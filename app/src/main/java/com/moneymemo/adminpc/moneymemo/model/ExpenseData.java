package com.moneymemo.adminpc.moneymemo.model;

/**
 * Created by Admin PC on 10/1/2561.
 */

public class ExpenseData {
    private int exp_id;
    private int exp_cid;
    private String exp_cname;
    private String  exp_amount;
    private String exp_date;
    private  String exp_memo;

    public ExpenseData(int exp_id, int exp_cid, String exp_cname, String exp_amount, String exp_date, String exp_memo) {
        this.exp_id = exp_id;
        this.exp_cid = exp_cid;
        this.exp_cname = exp_cname;
        this.exp_amount = exp_amount;
        this.exp_date = exp_date;
        this.exp_memo = exp_memo;
    }



    public int getExp_id(){
        return this.exp_id;
    }

    public int getExp_cid(){
        return this.exp_cid;
    }

    public String getExp_cname() { return  this.exp_cname;}


    public String getExp_amount(){
        return this.exp_amount;
    }
    public String getExp_date(){
        return this.exp_date;
    }
    public String getExp_memo(){
        return this.exp_memo;
    }

    @Override
    public String toString(){
        return exp_cname;
    }
}
