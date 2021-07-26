package com.zxqy.xunilaidian.entity;


/**
 * QQ登录
 * Created by huanghaibin on 2017/12/4.
 */
public class QQLoginTokenEntity {


    /**
     * ret : 0
     * openid : 9AFA9E0C8191CE1B2A803573855788C6
     * access_token : CB1B47802CE94E2E1EACF8A984CABF43
     * pay_token : EDB8CD63B380EC2D39667A968B050A20
     * expires_in : 7776000
     * code :
     * proxy_code :
     * proxy_expires_in : 0
     * pf : desktop_m_qq-10000144-android-2002-
     * pfkey : bd6f9975705b39b6e65d32e818ef2e92
     * msg :
     * login_cost : 107
     * query_authority_cost : -32905078
     * authority_cost : 0
     * expires_time : 1627193320995
     */

    public int ret;
    public String openid;
    public String access_token;
    public String pay_token;
    public int expires_in;
    public String code;
    public String proxy_code;
    public int proxy_expires_in;
    public String pf;
    public String pfkey;
    public String msg;
    public int login_cost;
    public int query_authority_cost;
    public int authority_cost;
    public long expires_time;
}
