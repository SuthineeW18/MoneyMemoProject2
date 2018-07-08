package com.moneymemo.adminpc.moneymemo.model;

public class ExpensecateData {
    private int exp_cid;
    private String exp_cname;


    public ExpensecateData(int exp_cid, String exp_cname){
        this.exp_cid = exp_cid;
        this.exp_cname = exp_cname;

    }

    public int getExp_cid(){
        return this.exp_cid;
    }
    public String getExp_cname(){
        return this.exp_cname;
    }
}
