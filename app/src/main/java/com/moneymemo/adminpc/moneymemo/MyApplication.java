package com.moneymemo.adminpc.moneymemo;

import android.app.Application;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Chococooky.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }
}
