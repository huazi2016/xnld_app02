package com.zxqy.xunilaidian.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/1/20.
 */

public class Share {
    @SerializedName("share")
    private List<Shareitem> shareitemList;

    public List<Shareitem> getShareitemList() {
        return shareitemList;
    }

    public void setShareitemList(List<Shareitem> shareitemList) {
        this.shareitemList = shareitemList;
    }
}
