package vfh.httpInterface.commons;

/**
 * Created by Seven on 15/11/23.
 */
public final class Constants {
    private Constants(){}

    public static final int THUMBNAIL_SIZE = 89; //缩略图大小
    public static final String CHIANESE_PATTERN ="[\\u4e00-\\u9fa5]";
    public static final String QINIU_AK = "_-50a_YKvBD9YBaQ1yZ7z2vjaUtaJpo456RnYNFi";
    public static final String QINIU_SK = "U4eJRdTzeYmlGrzYBu4zaj_fLfAT9Zlrloo3xtrU" ;
    public static final String QINIU_AVATAR = "ysh-avatar";

    //支付
    public static final String STATUS_INIT = "0";//status的值:初始化
    public static final String STATUS_PAY_REQUEST = "1";//status的值:支付请求
    public static final String STATUS_PAY_AFFIRM = "2";//status的值:支付确认
    public static final String STATUS_REFUND_REQUEST = "3";//status的值:支付请求退款
    public static final String STATUS_REFUND_AFFIRM = "4";//status的值:支付确认退款
}
