package com.moneymemo.adminpc.moneymemo.model;

import java.util.ArrayList;

/**
 * Created by Admin PC on 25/1/2561.
 */

public class ReportData {
    private int exp_id;
    private int exp_cid;
    private  String exp_cname;
    private Double percent;
    private Double sum_expense;


    public ReportData(int exp_id,int exp_cid,String exp_cname,Double percent,Double sum_expense) {


        this.exp_id = exp_id;
        this.exp_cid = exp_cid;
        this.exp_cname = exp_cname;
        this.percent = percent;
        this.sum_expense = sum_expense;


    }


    public  int getExp_id() {return  exp_id;}    public int getExp_cid() {return exp_cid;}
    public String getExp_cname() {
        return exp_cname;
    }

    public void setExp_cname(String exp_cname) {
        this.exp_cname = exp_cname;
    }



    public Double getSum_expense() {return  this.sum_expense;}
    public Double getPercent() {return  this.percent;}

    @Override
    public String toString(){
        return exp_cname;
    }




}
