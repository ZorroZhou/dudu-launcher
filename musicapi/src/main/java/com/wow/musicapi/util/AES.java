package com.wow.musicapi.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by haohua on 2018/2/10.
 */
public class AES {

    /**
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String sSrc, byte[] sKey) throws Exception {
        return encrypt(sSrc.getBytes("utf-8"), sKey);
    }

    /**
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String sSrc, byte[] sKey, byte[] iv) throws Exception {
        return encrypt(sSrc.getBytes("utf-8"), sKey, iv);
    }

    /**
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] sSrc, byte[] sKey, byte[] iv) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
        IvParameterSpec ivspec = null;
        if (iv != null) {
            ivspec = new IvParameterSpec(iv);
        }
        Cipher cipher;
        if (ivspec == null) {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        } else {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
        }
        byte[] encrypted = cipher.doFinal(sSrc);
        return encrypted;
    }

    /**
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] sSrc, byte[] sKey) throws Exception {
        return encrypt(sSrc, sKey, null);
    }

    /**
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.decodeBase64(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
}
