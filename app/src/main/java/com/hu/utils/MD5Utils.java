package com.hu.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class MD5Utils {
    /**
     * 创建MD5加密方法md5()
     */
    public static String md5(String text) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                //将字节byte转换为int类型的数据
                int number = b & 0xff;
                //将int类型的数据转换为十六进制的字符串数据
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0" + hex);
                } else {
                    sb.append(hex);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
