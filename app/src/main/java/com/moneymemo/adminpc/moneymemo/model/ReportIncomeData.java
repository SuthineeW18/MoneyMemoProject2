package com.moneymemo.adminpc.moneymemo.model;

import java.util.ArrayList;

/**
 * Created by Admin PC on 25/1/2561.
 */

public class ReportIncomeData {
    private int income_id;
    private int income_cid;
    private  String income_cname;
    private Double percent;
    private Double sum_inc;


    public ReportIncomeData(int income_id,int income_cid,String income_cname,Double percent,Double sum_inc) {


        this.income_id = income_id;
        this.income_cid = income_cid;
        this.income_cname = income_cname;
        this.percent = percent;
        this.sum_inc = sum_inc;


    }


    public  int getIncome_id() {return  income_id;}    public int getIncome_cid() {return income_cid;}
    public String getIncome_cname() {
        return income_cname;
    }

    public void setIncome_cname(String income_cname) {
        this.income_cname = income_cname;
    }



    public Double getSum_inc() {return  this.sum_inc;}
    public Double getPercent() {return  this.percent;}

    @Override
    public String toString(){
        return income_cname;
    }




}
