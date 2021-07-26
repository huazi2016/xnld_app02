package com.zxqy.xunilaidian.entity;


import java.io.Serializable;
import java.util.List;

/**
 * 反馈列表实体类
 * Created by huanghaibin on 2017/12/4.
 */
public class FeedBackListEntity implements Serializable {

    /**
     * code : 0
     * msg : success
     * data : {"currentPage":10,"itemCount":4,"list":[{"idUser":"caj1fkoivnhk100ubkutnfyx80cc2zoz","label":1,"timeCreate":"2021-04-13 16:41:07","status":1,"typeCreator":1,"id":"caj1fkoivnhk100ubkutnfyx80cc2zoz","packageName":"com.zxqy.guajiduanxin","title":"Test 提交反馈","platform":1,"files":"{}"},{"idUser":"caldmg8lfg9c700fgpi8dvoo66k3jgjv","label":1,"timeCreate":"2021-04-13 16:55:41","status":1,"typeCreator":1,"id":"lwcg6e0camgxvgmdkic200rxwxy1i889","packageName":"com.zxqy.guajiduanxin","title":"反馈标题","platform":1,"files":"{}"},{"idUser":"caldmg8lfg9c700fgpi8dvoo66k3jgjv","label":1,"timeCreate":"2021-04-13 17:11:05","status":1,"typeCreator":1,"id":"nrgbrw0camh9nmc6wc8100s25jg9z8jh","packageName":"com.zxqy.guajiduanxin","title":"反馈标题","platform":1,"files":"{message_feed_photo_1618304140226.png,message_feed_photo_1618304262839.png}"},{"idUser":"caldmg8lfg9c700fgpi8dvoo66k3jgjv","label":1,"timeCreate":"2021-04-13 17:11:52","status":1,"typeCreator":1,"id":"btwmmc0camha9bvm1nw200b9quak9ns4","packageName":"com.zxqy.guajiduanxin","title":"反馈标题","platform":1,"files":"{message_feed_photo_1618305059781.png,message_feed_photo_1618305062872.png}"}]}
     */

    public int code;
    public String msg;
    public FeedBackDataBean data;

    public static class FeedBackDataBean {
        /**
         * currentPage : 10
         * itemCount : 4
         * list : [{"idUser":"caj1fkoivnhk100ubkutnfyx80cc2zoz","label":1,"timeCreate":"2021-04-13 16:41:07","status":1,"typeCreator":1,"id":"caj1fkoivnhk100ubkutnfyx80cc2zoz","packageName":"com.zxqy.guajiduanxin","title":"Test 提交反馈","platform":1,"files":"{}"},{"idUser":"caldmg8lfg9c700fgpi8dvoo66k3jgjv","label":1,"timeCreate":"2021-04-13 16:55:41","status":1,"typeCreator":1,"id":"lwcg6e0camgxvgmdkic200rxwxy1i889","packageName":"com.zxqy.guajiduanxin","title":"反馈标题","platform":1,"files":"{}"},{"idUser":"caldmg8lfg9c700fgpi8dvoo66k3jgjv","label":1,"timeCreate":"2021-04-13 17:11:05","status":1,"typeCreator":1,"id":"nrgbrw0camh9nmc6wc8100s25jg9z8jh","packageName":"com.zxqy.guajiduanxin","title":"反馈标题","platform":1,"files":"{message_feed_photo_1618304140226.png,message_feed_photo_1618304262839.png}"},{"idUser":"caldmg8lfg9c700fgpi8dvoo66k3jgjv","label":1,"timeCreate":"2021-04-13 17:11:52","status":1,"typeCreator":1,"id":"btwmmc0camha9bvm1nw200b9quak9ns4","packageName":"com.zxqy.guajiduanxin","title":"反馈标题","platform":1,"files":"{message_feed_photo_1618305059781.png,message_feed_photo_1618305062872.png}"}]
         */

        public int currentPage;
        public int itemCount;
        public List<FeedBackBean> list;

        public static class FeedBackBean {
            /**
             * idUser : caj1fkoivnhk100ubkutnfyx80cc2zoz
             * label : 1
             * timeCreate : 2021-04-13 16:41:07
             * status : 1
             * typeCreator : 1
             * id : caj1fkoivnhk100ubkutnfyx80cc2zoz
             * packageName : com.zxqy.guajiduanxin
             * title : Test 提交反馈
             * platform : 1
             * files : {}
             */

            public String idUser;
            public int type;//1 bug  2 使用问题  3 建议  4 充值问题  5 其他
            public String timeSubmit;
            public int status; //1 是未完成 2 是完成
            public int typeCreator;
            public String id;
            public String packageName;
            public String title;
            public int platform;
            public String files;
        }
    }
}
