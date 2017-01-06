package indi.seven.android.utils.string;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Description: TODO. <br/>
 * Date: 2016/11/7 <br/>
 * @author mr.hoo7793@gmail.com
 */

public class StringUtils {

    public static String filter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String   regEx  =  "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 检查字符串str是否属于字符数组array
     * @param array 字符数组array
     * @param str 字符str
     * @return 属于返回true.
     */
    public static boolean belongTo(String[] array, String str){
        if(array != null && str != null){
            for (String item: array) {
                if(item.equals(str.trim())){
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 检查字符串str是否属于字符数组array,忽略大小写
     * @param array 字符数组array
     * @param str 字符str
     * @return 属于返回true.
     */
    public static boolean belongToIgnoreCase(String[] array, String str){
        if(array != null && str != null){
            for (String item: array) {
                if(item.equalsIgnoreCase(str.trim())){
                    return true;
                }
            }

        }
        return false;
    }


    /**
     * 将多个字符串数组合并为一个
     * @param first 第一个字符数组
     * @param rest 余下的字符数组
     * @return 合并后的字符数组
     */
    public static String[] concatAll(String[] first, String[]... rest) {
        int totalLength = first.length;
        for (String[] array : rest) {
            totalLength += array.length;
        }
        String[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (String[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * 计算token在str中出现的次数
     * @param str
     * @param token
     * @return
     */
    public static int getTokenConut(String str,String token){
        if(str != null && str.length() > 0){
            String[] array = str.split(token);
            if(array.length > 0){
                return array.length -1;
            }
        }
        return 0;
    }

    /**
     * 去除字符串中的前后空格、回车、换行符、制表符
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {

        if(str == null){
            return null;
        }

        Pattern p = Pattern.compile("\t|\r|\n");
        Matcher m = p.matcher(str);
        String dest = m.replaceAll("");

        return dest.trim();
    }

    /**
     * 将byte转换成16进制字符串(两个字符)
     * @param src
     * @return 16进制表示的字符串
     */
    public static String toHexString(byte src) {
        StringBuilder stringBuilder = new StringBuilder("");
        int v = src & 0xFF;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        return stringBuilder.toString();
    }


    /**
     * 将byte数组转换成16进制字符串(两个字符)
     * @param src
     * @return 16进制表示的字符串
     */
    public static String toHexString(byte[] src) {
        if (src == null || src.length <= 0) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
