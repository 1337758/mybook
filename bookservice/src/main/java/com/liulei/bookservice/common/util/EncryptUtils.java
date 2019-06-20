package com.liulei.bookservice.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {

    private static final String KEY_AES = "AES";

    /**
     * 字符编码
     */
    public final static String ENCODING = "UTF-8";

    /**
     * MD5加密
     * @param source
     * @return
     */
    public static String getMD5(String source) {
        return getMD5(source.getBytes());
    }

    private static String getMD5(byte[] source) {
        String s = null;
        // 用来将字节转换成 16 进制表示的字符
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte tmp[];
            synchronized (EncryptUtils.class) {
                md.update(source);
                // MD5 的计算结果是一个 128 位的长整数，
                tmp = md.digest();
            }
            // 用字节表示就是 16 个字节,每个字节用 16 进制表示的话，使用两个字符，
            char str[] = new char[16 * 2];
            // 表示转换结果中对应的字符位置
            int k = 0;
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf];
            }
            // 换后的结果转换为字符串
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

}