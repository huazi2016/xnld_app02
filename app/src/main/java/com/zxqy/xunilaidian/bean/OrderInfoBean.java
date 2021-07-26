package com.zxqy.xunilaidian.bean;

import java.io.Serializable;

/**
 订单信息 Bean
 */
public class OrderInfoBean implements Serializable {


    /**
     * code : 0
     * msg : success
     * data : {"app_id":"wx13e0561c3c9fd6d3","noncestr":"1ydz8y0caor1y4uop7c100y0vaeib0q3","partner_id":"1608459689","prepay_id":"wx16091638712410e61018149f58a96a0000","sign":"2F4659A0781136F5B3583CFC6405ACEE3F650895AC3F296488BC14FFD4D7F68F","timestamp":"1618535792103"}
     */

    public int code;
    public String msg;
    public OrderInfo data;

    public static class OrderInfo {
        /**
         * app_id : wx13e0561c3c9fd6d3
         * noncestr : 1ydz8y0caor1y4uop7c100y0vaeib0q3
         * partner_id : 1608459689
         * prepay_id : wx16091638712410e61018149f58a96a0000
         * sign : 2F4659A0781136F5B3583CFC6405ACEE3F650895AC3F296488BC14FFD4D7F68F
         * timestamp : 1618535792103
         */

        public String app_id;
        public String noncestr;
        public String partner_id;
        public String prepay_id;
        public String sign;
        public String timestamp;
        public String packageValue;
    }
}
