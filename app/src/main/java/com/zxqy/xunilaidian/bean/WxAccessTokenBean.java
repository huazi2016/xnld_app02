package com.zxqy.xunilaidian.bean;

/**
 * 调起微信返回信息
 */
public class WxAccessTokenBean {
    /**
     * access_token:"21_bLDNTtSvd_G6EhVr-JPDDWuK6pjy6OQ4dByKpknteDvF_z8L0iM7f3AN5p4iPspdXMvxDurpK9HlmtpmbWFwb3QsbEG-keedRp-YlESSvqI"
     * expires_in:7200
     * refresh_token:"21_EPaMEr8AnfmgoKvIicc92t9Mza5-8vyZ1RyUG8ihtof8WaDOrx9SmtaY-X50_mdgCQV3EDgkMy9Ksj7ewhcL2CkKpQ96RYz5KCeadkgb4kY"
     * openid:"oGXNx5ouUa6jFPtMcG-ZYPSgmzqQ"
     * scope:"snsapi_userinfo"
     * unionid:"oaKRD5sRoiEI-1WngFbAGBArmKYA"
     */
    //接口调用凭证
    private String access_token;
    //access_token接口调用凭证超时时间，单位（秒）
    private String expires_in;
    //用户刷新access_token
    private String refresh_token;
    //授权用户唯一标识
    private String openid;
    //用户授权的作用域，使用逗号（,）分隔
    private String scope;
    //当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
    private String unionid;

    public String getAccess_token() {
        return access_token == null ? "" : access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token == null ? "" : access_token;
    }

    public String getExpires_in() {
        return expires_in == null ? "" : expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in == null ? "" : expires_in;
    }

    public String getRefresh_token() {
        return refresh_token == null ? "" : refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token == null ? "" : refresh_token;
    }

    public String getOpenid() {
        return openid == null ? "" : openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? "" : openid;
    }

    public String getScope() {
        return scope == null ? "" : scope;
    }

    public void setScope(String scope) {
        this.scope = scope == null ? "" : scope;
    }

    public String getUnionid() {
        return unionid == null ? "" : unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid == null ? "" : unionid;
    }

    @Override
    public String toString() {
        return "WxAccessTokenBean{" +
                "access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
