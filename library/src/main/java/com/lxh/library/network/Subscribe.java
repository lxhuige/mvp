package com.lxh.library.network;

import com.google.gson.Gson;

/**
 * @author Created by lxh on 2018/1/18 0018.
 */

public abstract class Subscribe {

    public void onComplete() {
    }

    public abstract void onReceive(String data, Gson gson);

    public void fail(String data, Gson gson) {
    }

}
