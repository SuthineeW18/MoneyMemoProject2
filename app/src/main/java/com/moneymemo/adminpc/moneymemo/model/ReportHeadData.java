package com.moneymemo.adminpc.moneymemo.model;

import java.util.ArrayList;

/**
 * Created by Admin PC on 25/1/2561.
 */

public class ReportHeadData {



    private String exp_cname;



    public  ReportHeadData(String exp_cname){

        this.exp_cname = exp_cname;

    }


    public String getValue(){
        return exp_cname;
    }

    @Override
    public String toString(){
        return exp_cname;
    }



}
