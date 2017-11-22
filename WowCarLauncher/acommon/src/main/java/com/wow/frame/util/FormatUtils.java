package com.wow.frame.util;

public class FormatUtils {
    public static String bytesToHexString(byte[] paramArrayOfByte) {
        StringBuilder localStringBuilder = new StringBuilder("");
        if ((paramArrayOfByte == null) || (paramArrayOfByte.length <= 0))
            return null;
        for (int i = 0; ; i++) {
            if (i >= paramArrayOfByte.length)
                return localStringBuilder.toString();
            String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
            if (str.length() < 2)
                localStringBuilder.append(0);
            localStringBuilder.append(str);
        }
    }
    public static int bytesToInt(byte[] bytes) {
        int number = bytes[0] & 0xFF;
        // "|="按位或赋值。
        number |= ((bytes[1] << 8) & 0xFF00);
        number |= ((bytes[2] << 16) & 0xFF0000);
        number |= ((bytes[3] << 24) & 0xFF000000);
        return number;
    }

    public static byte hexToByte(String hex) {
        hex = hex.toUpperCase();
        char[] hexChars = hex.toCharArray();
        return (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes( int value )
    {
        byte[] src = new byte[4];
        src[3] =  (byte) ((value>>24) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[0] =  (byte) (value & 0xFF);
        return src;
    }
}

/*
 * Location: C:\Users\LC\Desktop\反编译工具包\classes_dex2jar.jar Qualified Name:
 * com.danny.common.util.FormatUtils JD-Core Version: 0.6.2
 */