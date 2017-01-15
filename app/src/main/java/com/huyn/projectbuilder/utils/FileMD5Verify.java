package com.huyn.projectbuilder.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件完整性验证
 * Created by huyaonan on 16/5/16.
 */
public class FileMD5Verify {

    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    //先上Java代码
    public static String getMD5(String s) {
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean md5sum(String filename, String target) {
        InputStream fis;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try{
            fis = new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while((numRead=fis.read(buffer)) > 0) {
                md5.update(buffer,0,numRead);
            }
            fis.close();
            String str = toHexString(md5.digest());
            Utils.sysout(target + "-------md5 compare-------" + str);
            return str.equalsIgnoreCase(target);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean md5sum(File file, String target) throws IOException, NoSuchAlgorithmException {
        byte[] buffer = new byte[1024];
        int numRead = 0;
        InputStream fis = new FileInputStream(file);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        while((numRead=fis.read(buffer)) > 0) {
            md5.update(buffer,0,numRead);
        }
        fis.close();
        String str = toHexString(md5.digest());
        return str.equalsIgnoreCase(target);
    }

}
