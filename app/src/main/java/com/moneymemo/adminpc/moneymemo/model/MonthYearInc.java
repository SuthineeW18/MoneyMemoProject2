package com.moneymemo.adminpc.moneymemo.model;

/**
 * Created by Admin PC on 21/3/2561.
 */

public class MonthYearInc {


    private String income_date;

    public MonthYearInc(String income_date){

        this.income_date = income_date;

    }


    public String toString(){
        return this.income_date;
    }
}
