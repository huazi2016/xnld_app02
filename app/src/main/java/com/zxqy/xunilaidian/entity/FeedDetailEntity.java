package com.zxqy.xunilaidian.entity;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 反馈详情
 * Created by huanghaibin on 2017/12/4.
 */
public class FeedDetailEntity implements Serializable {


    /**
     * code : 0
     * msg : success
     * data : [{"id":"18199dmcawkrxn3brx470058n6ygai1x","idFeedback":"rmha4b0cawkrxmq87lw60042h7njzgg7","type":0,"timeCreate":"2021-04-25 14:02:40","idUser":"WX1lzespqcawhbptcfuwk100p24t2kn87g","typeUser":0,"idsFile":"{message_feed_photo_1619330557084.jpg,message_feed_photo_1619330558427.jpg}","msg":"看看刷新"}]
     */

    public int code;
    public String msg;
    public List<FeedDetail> data;

    public static class FeedDetail  implements MultiItemEntity {
        public static final int RIGHT = 1;
        public static final int RIGHT_1 = 3;
        public static final int LEFT = 2;
        /**
         * id : 18199dmcawkrxn3brx470058n6ygai1x
         * idFeedback : rmha4b0cawkrxmq87lw60042h7njzgg7
         * type : 0
         * timeCreate : 2021-04-25 14:02:40
         * idUser : WX1lzespqcawhbptcfuwk100p24t2kn87g
         * typeUser : 0
         * idsFile : {message_feed_photo_1619330557084.jpg,message_feed_photo_1619330558427.jpg}
         * msg : 看看刷新
         *
         */

        public String id;
        public String idFeedback;
        public int type;
        public String timeSubmit;
        public String idUser;
        public int typeSubmitter;//2 ===客服  1 ===普通要用户自己   3===vip用户自己
        public String idsFile;
        public String msg;
        public String timeSend;

        @Override
        public int getItemType() {
            return typeSubmitter;
        }
    }
}
