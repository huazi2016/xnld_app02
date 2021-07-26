package com.zxqy.xunilaidian.utils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/1/20.
 */

public class Shareitem {
    @SerializedName("pid")
    private String pid;
    @SerializedName("image_url")
    private String imageurl;
    @SerializedName("share_num")
    private int sharenum;
    @SerializedName("vip_day")
    private int vipday;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getSharenum() {
        return sharenum;
    }

    public void setSharenum(int sharenum) {
        this.sharenum = sharenum;
    }

    public int getVipday() {
        return vipday;
    }

    public void setVipday(int vipday) {
        this.vipday = vipday;
    }
}
