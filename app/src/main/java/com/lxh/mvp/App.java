package com.lxh.mvp;

import android.app.Application;
import com.lxh.library.log.CrashHandler;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(getApplicationContext());
    }

    public static void dd(){
    }
}
