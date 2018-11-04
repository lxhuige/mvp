package com.lxh.library.update;

import java.io.Serializable;

/**
 * Created by ssb on 2017/6/29 13:24.
 * company: lvshandian
 */

public class VersionUpdateBean implements Serializable {

    private static final long serialVersionUID = -1763813605642414524L;

    /**
     * resultCode : 1
     * message : 成功
     * content : 提示信息，如更新内容
     * appUrl : app下载地址
     * type : 0:不需要更新，1:普通更新2：强制更新
     */

    private int resultCode;
    private String message;
    private String content;
    private String appUrl;
    private int type;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
