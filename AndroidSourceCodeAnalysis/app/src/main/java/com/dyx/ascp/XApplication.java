package com.dyx.ascp;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Author：dayongxin
 * Function：
 */
public class XApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
    }

    /**
     * Initialize Logger
     */
    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
