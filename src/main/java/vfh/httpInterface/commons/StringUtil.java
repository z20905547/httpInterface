package vfh.httpInterface.commons;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/*
 * 所属模块: 工具包
 * 说明：常用字符串及数组操作
 */
public class StringUtil {
    private static StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i ++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }

    public static String getInitialUpperCase(String str) {
        if(str != null && str.length() > 0) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return str;
    }

    private final static String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化小数保持最少和最多小数点.
     *
     *  @param  num
     *  @param  minFractionDigits
     *  @param  maxFractionDigits
     *  @return
     */
    public   static  String formatFraction( double  num,  int  minFractionDigits,  int  maxFractionDigits) {
        //  输出固定小数点位数
        java.text.NumberFormat nb  =  java.text.NumberFormat.getInstance();
        nb.setMaximumFractionDigits(maxFractionDigits);
        nb.setMinimumFractionDigits(minFractionDigits);
        nb.setGroupingUsed( false );
        String rate  =  nb.format(num);
        return  rate;
    }

    /**
     * 得到当前日期前后的日期
     * @return 返回日期字符串
     */
    public static String getBefDateString(int day_i){
        Calendar day=Calendar.getInstance();
        day.add(Calendar.DATE,day_i);
        return new SimpleDateFormat(dateFormat).format(day.getTime());
    }

    /**
     * 获取当前日期的字符串长度
     * @return
     */
    public static int getDateTimeLen(){
        return dateFormat.length();
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getDateTime(){
        return new   SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getDate(){
        return new   SimpleDateFormat( "yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    /**
     * 获取当前日期(传入指定格式)
     * @return
     */
    public static String getDateByFormat(String dateFormat){
        return new SimpleDateFormat(dateFormat).format(Calendar.getInstance().getTime());
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getTime(){
        return new   SimpleDateFormat( "HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * 字符串转换为毫秒
     * @return
     */
    public static long parseLongDate(String str){
        try {
            return new SimpleDateFormat(dateFormat).parse(str).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 计算两个字符串转换为日期后相减得到的分钟
     * @param str1
     * @param str2
     * @return
     */
    public static int getSubTime(String str1,String str2){
        return (int)(parseLongDate(str2) - parseLongDate(str1))/(1000*60);
    }

    /**
     * Change the null string value to "", if not null, then return it self, use
     * this to avoid display a null string to "null".
     *
     *  @param  input
     *            the string to clear
     *  @return  the result
     */
    public   static  String clearNull(String input) {
        return  isEmpty(input)  ?   ""  : input;
    }

    /**
     * 转换由表单读取的数据的内码(从 ISO8859 转换到 gb2312).
     *
     *  @param  input
     *            输入的字符串
     *  @return  转换结果, 如果有错误发生, 则返回原来的值
     */
    public   static  String toChi(String input) {
        try  {
            byte [] bytes  =  input.getBytes( " ISO8859-1 " );
            return   new  String(bytes,  " GBK " );
        }  catch  (Exception ex) {
        }
        return  input;
    }

    /**
     * 转换由表单读取的数据的内码到 ISO(从 GBK 转换到ISO8859-1).
     *
     *  @param  input
     *            输入的字符串
     *  @return  转换结果, 如果有错误发生, 则返回原来的值
     */
    public   static  String toISO(String input) {
        return  changeEncoding(input,  " GBK " ,  " ISO8859-1 " );
    }

    /**
     * 转换字符串的内码.
     *
     *  @param  input
     *            输入的字符串
     *  @param  sourceEncoding
     *            源字符集名称
     *  @param  targetEncoding
     *            目标字符集名称
     *  @return  转换结果, 如果有错误发生, 则返回原来的值
     */
    public   static  String changeEncoding(String input, String sourceEncoding,
                                           String targetEncoding) {
        if  (input  ==   null   ||  input.equals( "" )) {
            return  input;
        }

        try  {
            byte [] bytes  =  input.getBytes(sourceEncoding);
            return   new  String(bytes, targetEncoding);
        }  catch  (Exception ex) {
        }
        return  input;
    }


    /**
     * 对给定字符进行 URL 编码
     */
    public   static  String encode(String value) {
        if  (isEmpty(value)) {
            return   "" ;
        }

        try  {
            value  =  java.net.URLEncoder.encode(value,  " GB2312 " );
        }  catch  (Exception ex) {
            ex.printStackTrace();
        }

        return  value;
    }

    /**
     * 对给定字符进行 URL 解码
     *
     *  @param  value
     *            解码前的字符串
     *  @return  解码后的字符串
     */
    public   static  String decode(String value) {
        if  (isEmpty(value)) {
            return   "" ;
        }

        try  {
            return  java.net.URLDecoder.decode(value,  " GB2312 " );
        }  catch  (Exception ex) {
            ex.printStackTrace();
        }

        return  value;
    }

    /**
     * 判断map是否未空, 如果不为 null 或者长度大于0, 均返回 true.
     */
    public   static   boolean  isNotEmptyMap(Map<?,?> input) {
        return  (input  !=   null   &&  input.size()  >   0 );
    }

    /**
     * 判断list是否未空, 如果不为 null 或者长度大于0, 均返回 true.
     */
    public   static   boolean  isNotEmptyList(List<?> input) {
        return  (input  !=   null   &&  input.size()  >   0 );
    }

    /**
     * 判断字符串数组是否未空, 如果不为 null 或者长度大于0, 均返回 .
     */
    public   static   boolean  isNotEmptyArray(String[] input) {
        return  (input  !=   null   &&  input.length  >   0 );
    }


    /**
     * 判断字符串是否未空, 如果为 null 或者长度为0, 均返回 true.
     */
    public   static   boolean  isEmpty(String input) {
        return  (input  ==   null   ||  input.length()  ==   0 );
    }

    /**
     * 判断字符串数组是否未空, 如果为 null 或者长度为0, 均返回 true.
     */
    public   static   boolean  isEmptyArr(String[] input) {
        return  (input  ==   null   ||  input.length  ==   0 );
    }

    /**
     * 判断字符串是否未空, 如果为 null 或者长度为0, 均返回 true.
     */
    public   static   boolean  isNotEmpty(String input) {
        return  !isEmpty(input);
    }

    /**
     * 将字符串转换为整型数字
     * @param str
     * @return
     */
    public static int parseInt(String str){
        if(isNotEmpty(str)){
            return Integer.parseInt(str);
        }else{
            return 0;
        }
    }

    /**
     * 获得输入字符串的字节长度(即二进制字节数), 用于发送短信时判断是否超出长度.
     *
     *  @param  input
     *            输入字符串
     *  @return  字符串的字节长度(不是 Unicode 长度)
     */
    public   static   int  getBytesLength(String input) {
        if  (input  ==   null ) {
            return   0 ;
        }
        int  bytesLength  =  input.getBytes().length;
        return  bytesLength;
    }

    /**
     * 删除指定的 Web 应用程序目录下所上传的文件
     *
     *  @param  application
     *            JSP/Servlet 的 ServletContext
     *  @param  filePath
     *            相对文件路径
     */
    public   static   void  deleteFile(ServletContext application, String filePath) {
        if  ( ! isEmpty(filePath)) {
            String physicalFilePath  =  application.getRealPath(filePath);
            if  ( ! isEmpty(physicalFilePath)) {
                java.io.File file  =   new  java.io.File(physicalFilePath);
                file.delete();
            }
        }
    }

    /**
     * 在指定的 Web 应用程序目录下以指定路径创建文件
     *
     *  @param  application
     *            JSP/Servlet 的 ServletContext
     *  @param  filePath
     *            相对文件路径
     */
    public   static   boolean  createFile(ServletContext application, String filePath) {
        if  ( ! isEmpty(filePath)) {
            String physicalFilePath  =  application.getRealPath(filePath);
            if  ( ! isEmpty(physicalFilePath)) {
                java.io.File file  =   new  java.io.File(physicalFilePath);

                try  {
                    //  创建文件
                    return  file.createNewFile();
                }  catch  (IOException e) {
                    System.err.println( " Unable to create file  "   +  filePath);
                }
            }
        }

        return   false ;
    }

    /**
     * 在指定的 Web 应用程序目录下以指定路径创建目录.
     *
     *  @param  application
     *            JSP/Servlet 的 ServletContext
     *  @param  filePath
     *            相对文件路径
     */
    public   static   boolean  createDir(ServletContext application, String filePath) {
        if  ( ! isEmpty(filePath)) {
            String physicalFilePath  =  application.getRealPath(filePath);
            if  ( ! isEmpty(physicalFilePath)) {
                try  {
                    //  创建目录
                    java.io.File dir  =   new  java.io.File(application
                            .getRealPath(filePath));
                    return  dir.mkdirs();
                }  catch  (Exception e) {
                    System.err
                            .println( " Unable to create directory  "   +  filePath);
                }
            }
        }

        return   false ;
    }

    /**
     * 检查指定的 Web 应用程序目录下的文件是否存在.
     *
     *  @param  application
     *            JSP/Servlet 的 ServletContext
     *  @param  filePath
     *            相对文件路径
     *  @return  boolean - 文件是否存在
     */
    public   static   boolean  checkFileExists(ServletContext application,
                                               String filePath) {
        if  ( ! isEmpty(filePath)) {
            String physicalFilePath  =  application.getRealPath(filePath);
            if  ( ! isEmpty(physicalFilePath)) {
                java.io.File file  =   new  java.io.File(physicalFilePath);
                return  file.exists();
            }
        }

        return   false ;
    }

    /**
     * 将日期转换为中文表示方式的字符串(格式为 yyyy年MM月dd日 HH:mm:ss).
     */
    public   static  String dateToChineseString(Date date) {
        if  (date  ==   null ) {
            return   "" ;
        }

        java.text.SimpleDateFormat dateFormat  =   new  java.text.SimpleDateFormat(
                " yyyy年MM月dd日 HH:mm:ss " );

        return  dateFormat.format(date);
    }

    /**
     * 将日期转换为 14 位的字符串(格式为yyyyMMddHHmmss).
     */
    public   static  String dateTo14String(Date date) {
        if  (date  ==   null ) {
            return   null ;
        }

        java.text.SimpleDateFormat dateFormat  =   new  java.text.SimpleDateFormat(
                " yyyyMMddHHmmss " );

        return  dateFormat.format(date);
    }

    /**
     * 将 14 位的字符串(格式为yyyyMMddHHmmss)转换为日期.
     */
    public   static  Date string14ToDate(String input) {
        if  (isEmpty(input)) {
            return   null ;
        }

        if  (input.length()  !=   14 ) {
            return   null ;
        }

        java.text.SimpleDateFormat dateFormat  =   new  java.text.SimpleDateFormat(
                " yyyyMMddHHmmss " );

        try  {
            return  dateFormat.parse(input);
        }  catch  (Exception ex) {
            ex.printStackTrace();
        }

        return   null ;
    }

    /**
     * 获取 boolean 参数从ServletRequest中.
     *  @param  request
     *  @param  name
     *  @return
     */
    public   static   boolean  getBoolean(HttpServletRequest request, String name) {
        return  Boolean.valueOf(request.getParameter(name));
    }

    public   static  String replaceChar(String s,  char  c,  char  c1) {
        if  (s  ==   null ) {
            return   "" ;
        }
        return  s.replace(c, c1);
    }

    public   static  String replaceString(String s, String s1, String s2) {
        if  (s  ==   null   ||  s1  ==   null   ||  s2  ==   null ) {
            return   "" ;
        }
        return  s.replaceAll(s1, s2);
    }

    public   static  String toHtml(String s) {
        s  =  replaceString(s,  " < " ,  " &#60; " );
        s  =  replaceString(s,  " > " ,  " &#62; " );
        return  s;
    }

    public   static  String toBR(String s) {
        s  =  replaceString(s,  " \n " ,  " <br>\n " );
        s  =  replaceString(s,  " \t " ,  " &nbsp;&nbsp;&nbsp;&nbsp; " );
        s  =  replaceString(s,  "    " ,  " &nbsp;&nbsp; " );
        return  s;
    }

    public   static  String toSQL(String s) {
        s  =  replaceString(s,  " \r\n " ,  " \n " );
        return  s;
    }

    public   static  String replaceEnter(String s)  throws  NullPointerException {
        return  s.replaceAll( " \n " ,  " <br> " );
    }

    public   static  String replacebr(String s)  throws  NullPointerException {
        return  s.replaceAll( " <br> " ,  " \n " );
    }

    public   static  String replaceQuote(String s)  throws  NullPointerException {
        return  s.replaceAll( " ' " ,  " '' " );
    }

    public static int toInteger(Object str){
        return Integer.parseInt(str.toString());
    }

    /**
     * 切割字符串，将字符串切割成指定的长度，后面的用省略号补充
     * @param str
     * @param count
     * @return
     */
    public static String subString(String str,int count){
        if(isEmpty(str))return null;
        if(str.length() > count){
            return str.substring(0,count) + "...";
        }else{
            return str;
        }
    }

    /**
     * 切割字符串，将字符串切割成指定的长度，后面的直接去掉
     * @param str
     * @param count
     * @return
     */
    public static String subString1(String str,int count){
        if(isEmpty(str))return null;
        if(str.length() > count){
            return str.substring(0,count);
        }else{
            return str;
        }
    }

    /**
     * 切割字符串，将字符串切割成指定的长度，后面加上两个省略号
     * @param str
     * @param count
     * @return
     */
    public static String subString2(String str,int count){
        if(isEmpty(str))return null;
        if(str.length() > count){
            return str.substring(0,count) + "..";
        }else{
            return str;
        }
    }

    /**
     * 转换字符串为数字
     * @param str
     * @return
     */
    public static String convertNumber(String str){
        if(isEmpty(str))return "0";//空字符串返回0
        try{
            if(str.matches("^[0-9]*$"))return str;//整数直接返回
            return new DecimalFormat("0.0").format(Double.parseDouble(str));//非整数转换为1位小数点
        }catch(Exception ex){
            ex.printStackTrace();
            return "0";
        }
    }

    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是字母
     * @param str
     * @return
     */
    public static boolean isLetter(String str){
        if(isEmpty(str))return false;
        if(str.replaceAll("\\s*", "").matches("^[a-zA-Z]*"))return true;
        return false;
    }

    /**
     * 获取list的长度
     * @param str
     * @return
     */
    public static Integer listLen(List<?> list){
        if(isNotEmptyList(list))return list.size();
        return 0;
    }

    /**
     * 获取map的长度
     * @param str
     * @return
     */
    public static Integer mapLen(Map<?,?> map){
        if(isNotEmptyMap(map))return map.size();
        return 0;
    }

    /**
     * 判断是否包含字符串
     * @param str
     * @return
     */
    public static boolean isExist(String str1,String str2){
        boolean b = false;
        if(str2.indexOf("\""+str1+"\"")>-1){
            b = true;
        }
        return b;
    }
    /**
     * 判断是否包含字符串
     * @param str
     * @return
     */
    public static boolean isExists(String str1,String str2){
        boolean b = false;
        try{
            if(isNotEmpty(str1) && isNotEmpty(str2)){
                if(str2.indexOf(str1)>-1){
                    b = true;
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return b;
    }

    /**
     * 判断对象是否为空
     * @param obj
     * @return
     */
    public static boolean isNotEmptyObject(Object obj){
        return (obj != null) && isNotEmpty(obj.toString());
    }

    /**
     * 判断是否包含字符串
     * @param str
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isExistsArray(String str,List list){
        try{
            if(isNotEmptyList(list) && isNotEmpty(str)){
                return list.contains(str);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 将字符串转换为后台的标签
     * @param str
     * @return
     */
    public static String convertFnTag(String str){
        if(isNotEmpty(str)){
            if(str.contains("${fn:getLink('")){
                return getSplicStr(str,"\\$\\{fn\\:getLink\\(\\'","')}");
            }else if(str.contains("${fn:getLink(")){
                return getSplicStr(str,"\\$\\{fn\\:getLink\\(",")}");
            }else{
                return null;
            }
        }else{
            return null;
        }
    }


    private static String getSplicStr(String str,String start,String end){
        StringBuffer sb = new StringBuffer();
        if(isNotEmpty(str)){
            String[] s = str.split(start);
            for(int i=0;i<s.length;i++){
                String temp = s[i];
                if(temp.contains(end)){
                    sb.append(temp.substring(0,temp.lastIndexOf(end)));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 去掉字符串最后一个逗号
     * @param str
     * @return
     */
    public static String noLastComma(String str){
        if(isNotEmpty(str)){
            if(str.endsWith(",") || str.endsWith("，")){
                return str.substring(0,str.length()-1);
            }
            return str;
        }else{
            return "";
        }
    }
    /**
     * 判断是否是空
     * @param str
     * @return
     */
    public static boolean isEmptyOfObject (Object o){
        if(null==o||o.equals("")){
            return true;
        }else{
            return false;
        }
    }
    public static boolean isNotEmptyOfObject (Object o){
        if(null!=o&&!o.equals("")){
            return true;
        }else{
            return false;
        }
    }
    public static Date StringToDate(String dateStr){
        DateFormat dd = new SimpleDateFormat(dateFormat);
        Date date=null;
        try {
            date = dd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
     * @param sourceDate
     * @param formatLength
     * @return 重组后的数据
     */
    public static String frontCompWithZore(int sourceDate,int formatLength)
    {

    	/*
	      * 0 指前面补充零
	      * formatLength 字符总长度为 formatLength
	      * d 代表为正数。
	      */
        String newString = String.format("%0"+formatLength+"d", sourceDate);
        return  newString;
    }

    /**
     *
     * 字符串保留两位小数
     * @param strNum
     * @return
     */
    public static String keep2Decimal(String strNum) {
        DecimalFormat    df   = new DecimalFormat("######0.00");
        double num = Double.parseDouble(strNum);
        return df.format(num);
    }

    public static double keep2Decimal(double num) {
        BigDecimal b = new BigDecimal(num);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static Pattern p = Pattern.compile(Constants.CHIANESE_PATTERN);

    /**
     * 判断字符串是否有中文
     * @param str
     * @return
     */
    public static boolean hasChinese(String str){
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 把map转换成string类型
     * #month#=01&#date#=01&#hour#=12&#minute#=11&#opertion#=?&#amount#&#balance#&#telephone#
     * @param m
     * @return
     */
    public static Map mapToHponeMsg(Map m) {
        Map rm = new HashMap();
        if (!StringUtil.isNotEmptyMap(m)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Set keys = m.keySet();
        for (Object o : keys) {
            sb.append("#").append(o.toString()).append("#=").append(m.get(o.toString())).append("&");
        }
        sb.deleteCharAt(sb.toString().length() - 1);

        rm.put("tpl_value", sb.toString());
        return rm;
    }
}