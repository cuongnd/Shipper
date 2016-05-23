package org.csware.ee.utils;

import org.csware.ee.Guard;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Atwind on 2015/5/15.
 * Md5加密
 */
public class Md5Encryption {


    /**
     * 返回小写的MD5签名
     */
    public final static String md5(String s) {
        return (MD5(s) + "").toLowerCase();
    }

    /**
     * 返回大写的MD5签名
     */
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = getBytes(s, "UTF-8");// s.getBytes(Charset.forName("UTF-8")); //必须要使用UTF8编码
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过指定算法签名字符串
     *
     * @param data        Data to digest
     * @param charset     字符串转码为byte[]时使用的字符集
     * @param algorithm   目前其有效值为<code>MD5,SHA,SHA1,SHA-1,SHA-256,SHA-384,SHA-512</code>
     * @param toLowerCase 指定是否返回小写形式的十六进制字符串
     * @return String algorithm digest as a lowerCase hex string
     * @see Calculates the algorithm digest and returns the value as a hex string
     * @see If system dosen't support this <code>algorithm</code>, return "" not null
     * @see It will Calls {@link TradePortalUtil#getBytes(String str, String charset)}
     * @see 若系统不支持<code>charset</code>字符集,则按照系统默认字符集进行转换
     * @see 若系统不支持<code>algorithm</code>算法,则直接返回""空字符串
     * @see 另外,commons-codec.jar提供的DigestUtils.md5Hex(String data)与本方法getHexSign(data, "UTF-8", "MD5", false)效果相同
     */
    public static String getHexSign(String data, String charset, String algorithm, boolean toLowerCase) {
        char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        //Used to build output as Hex
        char[] DIGITS = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
        //get byte[] from {@link TradePortalUtil#getBytes(String, String)}
        byte[] dataBytes = getBytes(data, charset);
        byte[] algorithmData = null;
        try {
            //get an algorithm digest instance
            algorithmData = MessageDigest.getInstance(algorithm).digest(dataBytes);
        } catch (NoSuchAlgorithmException e) {
            System.out.print("签名字符串[" + data + "]时发生异常:System doesn't support this algorithm[" + algorithm + "]");
            return "";
        }
        char[] respData = new char[algorithmData.length << 1];
        //two characters form the hex value
        for (int i = 0, j = 0; i < algorithmData.length; i++) {
            respData[j++] = DIGITS[(0xF0 & algorithmData[i]) >>> 4];
            respData[j++] = DIGITS[0x0F & algorithmData[i]];
        }
        return new String(respData);
    }

    /**
     * 字符串转为字节数组
     *
     * @see 如果系统不支持所传入的<code>charset</code>字符集,则按照系统默认字符集进行转换
     */
    public static byte[] getBytes(String data, String charset) {
        data = (data == null ? "" : data);
        if (Guard.isNullOrEmpty(charset)) {
            return data.getBytes();
        }
        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            System.out.print("将字符串[" + data + "]转为byte[]时发生异常:系统不支持该字符集[" + charset + "]");
            return data.getBytes();
        }
    }


}
