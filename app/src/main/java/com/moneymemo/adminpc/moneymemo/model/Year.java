package com.moneymemo.adminpc.moneymemo.model;

/**
 * Created by Admin PC on 1/4/2561.
 */

public class Year {

    private String income_date;

    public Year(String income_date){

        this.income_date = income_date;

    }
    public String getIncome_date(){
        return this.income_date;
    }

    public String toString(){
        return this.income_date;
    }
}
