package com.moneymemo.adminpc.moneymemo;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.StringTokenizer;

public class NumberTextWatcherForThousand2 implements TextWatcher {
    EditText income_amount;


    public NumberTextWatcherForThousand2(EditText income_amount) {
        this.income_amount =income_amount;


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try
        {
            income_amount.removeTextChangedListener(this);
            String value = income_amount.getText().toString();


            if (value != null && !value.equals(""))
            {

                if(value.startsWith(".")){
                    income_amount.setText("0.");
                }
                if(value.startsWith("0") && !value.startsWith("0.")){
                    income_amount.setText("");

                }


                String str = income_amount.getText().toString().replaceAll(",", "");
                if (!value.equals(""))
                    income_amount.setText(getDecimalFormattedString(str));
                income_amount.setSelection(income_amount.getText().toString().length());
            }
            income_amount.addTextChangedListener(this);
            return;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            income_amount.addTextChangedListener(this);
        }

    }

    public static String getDecimalFormattedString(String value)
    {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1)
        {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt( -1 + str1.length()) == '.')
        {
            j--;
            str3 = ".";
        }
        for (int k = j;; k--)
        {
            if (k < 0)
            {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3)
            {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }

    }

    public static String trimCommaOfString(String string) {

        if(string.contains(",")){
            return string.replace(",","");}
        else {
            return string;
        }

    }
}
