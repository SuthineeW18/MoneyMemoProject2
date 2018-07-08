package com.moneymemo.adminpc.moneymemo.model;

/**
 * Created by Admin PC on 16/1/2561.
 */

public class MainData {

    private int exp_id;
    private String exp_amount;
    private String exp_date;


    private int income_id;
    private String income_amount;
    private String income_date;


    public MainData(int exp_id, String exp_amount,String exp_date,int income_id,String income_amount,String income_date){

        this.exp_id = exp_id;
        this.exp_amount = exp_amount;
        this.exp_date = exp_date;

        this.income_id = income_id;
        this.income_amount = income_amount;
        this.income_date = income_date;

    }
    public int getExp_id(){
        return this.exp_id;
    }
    public String getExp_amount(){
        return this.exp_amount;
    }
    public String getExp_date(){return this.exp_date;}

    public int getIncome_id(){
        return this.income_id;
    }
    public String getIncome_amount(){
        return this.income_amount;
    }
    public String getIncome_date(){return this.income_date;}



}
