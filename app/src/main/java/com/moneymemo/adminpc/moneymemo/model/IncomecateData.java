package com.moneymemo.adminpc.moneymemo.model;

/**
 * Created by Admin PC on 10/1/2561.
 */

public class IncomecateData {
    private int income_cid;
    private String income_cname;


    public IncomecateData(int income_cid, String income_cname){
        this.income_cid = income_cid;
        this.income_cname = income_cname;

    }

    public int getIncome_cid(){
        return this.income_cid;
    }

    public String getIncome_cname(){
        return this.income_cname;
    }


}
