package com.moneymemo.adminpc.moneymemo;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;


public class MyValueFormatter implements IValueFormatter {
    private DecimalFormat mFormat;
    public  MyValueFormatter(){
        mFormat = new DecimalFormat("###,###,###");

    }

    @Override
    public String getFormattedValue(float value , Entry entry , int dataSetIndex , ViewPortHandler viewPortHandler){
        if(value < 10 ) return "";
        return  mFormat.format(value) + "%";

    }
}
