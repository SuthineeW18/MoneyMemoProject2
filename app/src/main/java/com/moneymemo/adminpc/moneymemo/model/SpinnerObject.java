package com.moneymemo.adminpc.moneymemo.model;

/**
 * Created by Admin PC on 10/1/2561.
 */

public class SpinnerObject {

    private int exp_cid;
    private String exp_cname;



    public SpinnerObject(int exp_cid , String exp_cname){
        this.exp_cid = exp_cid;
        this.exp_cname = exp_cname;

    }

    public int getId(){
        return exp_cid;
    }
    public String getValue(){
        return exp_cname;
    }

    @Override
    public String toString(){
        return exp_cname;
    }
}
