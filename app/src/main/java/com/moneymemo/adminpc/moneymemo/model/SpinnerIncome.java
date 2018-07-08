package com.moneymemo.adminpc.moneymemo.model;

/**
 * Created by Admin PC on 15/1/2561.
 */

public class SpinnerIncome {
    private int income_cid;
    private String income_cname;

    public SpinnerIncome(int income_cid, String income_cname){
        this.income_cid = income_cid;
        this.income_cname = income_cname;

    }

    public int getId(){
        return income_cid;
    }
    public String getValue(){
        return income_cname;
    }

    @Override
    public String toString(){
        return income_cname;
    }
}
