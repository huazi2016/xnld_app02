package com.zxqy.xunilaidian.utils;

public class Constants {

    //=============用户权限开始==============

    public final static String PERMISSION_SHORTCUTS = "permission_shortcuts";//快捷方式权限  Shortcuts
    //=============用户权限结束==============

    public static final String FIRST_REGISTER = "march_first_register"; // 首次调用注册
    public final static String READ_EXTERNAL = "read_external";//文件读写
    public static final String PUB_LOG_TAG = "guajiduanxin";

    public final static String USER_PHONE = "user_phone";//用户登录手机号
    public final static String USER_NAME = "user_name";//用户
    public final static String IS_LOGIN = "is_login";//是否登录

    public static final int SELECT_PHOTO = 300;
    public static final int SELECT_VIDEO = 301;
    public static final int SELECT_RECODE = 302;
    public static final int SELECT_LOCATION = 306;
    public static final int CAMERA = 303;
    public static final int VIDEO = 304;
    public static final int RECORD = 305;
    public static final String QINIU_FILE_KEY_FEED= "message_feed_photo_";//七牛文件上传  反馈 图片
    // 微信id  速换图标
    public static final String WX_APP_ID = "wx9ab0b2f1432e933e";
    // 微信secret  速换图标

    public static final String WX_APP_SECRET = "ee2263cd727475204128077afd2d1925";

    // qq登录APPID  速换图标
    public static final String QQ_APP_ID = "101952485";
    public static final String QQ_APP_KEY = "ab0453e97c0ccf27e16702959c50061e";
    public static final String YM_APP_KEY = "60adc36553b672649910bbe2";

    public static final String QQ_APP_AUTHORITIES="com.tencent.sample.fileprovider";

    public static final String COMMON_URL = "common_url";        //通用的网址

    public static final String USER_ID = "user_id"; // 登录后返回的user_id
    public static final String USER_KEY = "user_key"; // 登录后返回的ukey
    public static final String  OUR_LOGIN = "our_login"; // 走自己的登录

    public final static String APP_TOKEN = "app_token";//apptoken

    //微信头像
    public final static String HEAD_IMAGE = "head_image";
    public final static String WX_NAME = "wx_name";
    // sp存储deviceId
    public static final String DEVICE_ID = "device_id";
    public final static String VIP_STATUS = "vip_status";//vip状态  1表示正常  2表示过期 3表示非会员

    /**
     * log打印控制
     */
    public static final boolean isPrint = true;
    //是否同意用户协议
    public static String IS_CONFIRM_USER_AGREEMENT = "is_confirm_user_agreement";

    public final static String SP_NAME = "ztzh_sp";

    public final static String ID_APP = "id_app";//登录接口返回，其余所有接口需要传递




    public final static String MUL_FILE = "stellarService_mul";


    public static final String USER_PHOTO_SUFFIX = ".jpg";
    public static final String USER_VIDEO_SUFFIX = ".mp4";

    /**
     * 头像名称
     */
    public static String MYPICFILE = "CacheImage";

    /**
     * 头像名称
     */
    public static String PICNAME = "_photo";




    /**刷新*/
    public static final int REFRESH = 113;

    /**表扬的标志*/
    public static final int PRAISE = 123;

    /**
     * 选择照片标识符
     */
    public final static int CODE_PHOTO = 100;
    /**
     * 拍照标识符
     */
    public final static int CODE_CAMRE = 101;
    /**
     * 裁剪标识符
     */
    public final static int PHOTO_CROP = 102;
    /**
     * 裁剪识符
     */
    public final static int CODE_CUT_IMG = 99;




    /**
     * 微信支付APP_ID
     */
    public static final String APP_ID = "wxd930ea5d5a258f4f";

    //跳转key
    public static final String EXTRA_KEY = "extra_key";


    //用于记录本地学习记录 书本类型保存SPutils字段//类型：语文1  英语3 国学0
    public static final String BOOKTYPE = "bookType";

    //用于记录本地学习记录 标题保存SPutils字段
    public static final String BOOKNAME = "bookName";


//    /**
//     * 支付宝APP_ID
//     */
//    public static final String APP_ID = "wxd930ea5d5a258f4f";


    //分享 单篇课文分享
    public static final String SHAR_BOOKVIEW_URL = "http://h5.maoedu.cn/share/bookview/";

    /**
     * 分享语文朗读列表
     * http://h5.maoedu.cn/share/booklist/eid/出版社/gid/年级
     */
    public static final String SHAR_BOOKLIST_URL = "http://h5.maoedu.cn/share/booklist/";

    /**
     * 国学朗读
     * http://h5.maoedu.cn/share/cnbooklist/cid/列表id
     */
    public static final String SHAR_CHINABOOKLIST_URL = "http://h5.maoedu.cn/share/cnbooklist/";

    /**
     * 国学朗读 内容
     */
    public static final String SHAR_BOOKCHINAVIEW_URL = "http://h5.maoedu.cn/share/cnbookview/";

    /**
     * 生字读写 列表
     */
    public static final String SHAR_WRITELIST_URL = "http://h5.maoedu.cn/share/writelist/";

    /**
     * 生字
     */
    public static final String SHAR_WORD_URL = "http://h5.maoedu.cn/share/writeview/";

    /**
     * 日积月累/ 英语作文 /英语演讲
     */
    public static final String SHAR_ENGLISH_URL = "http://h5.maoedu.cn/share/wordview/";


    /**
     * http://h5.maoedu.cn/share/workview/hash(id)
     * 朗读作业分享
     */
    public static final String SHAR_HOMEWORK_URL = "http://h5.maoedu.cn/share/workview/";





    //    分享朗读
//    列表 http://h5.maoedu.cn/share/booklist/eid/出版社/gid/年级
//    内容 http://h5.maoedu.cn/share/bookview/kid/音频id
//    国学朗读
//    列表  http://h5.maoedu.cn/share/cnbooklist/cid/列表id
//    内容  http://h5.maoedu.cn/share/cnbookview/kid/音频id

    public static final String SHAR_CONTENT = "咏蛙诵读:是专注于中小学生语文、英语课文同步朗读及国学诵读的移动互联网+教育的免费资源平台。";


}
