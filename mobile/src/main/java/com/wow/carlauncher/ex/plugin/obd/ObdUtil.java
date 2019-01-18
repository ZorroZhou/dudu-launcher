package com.wow.carlauncher.ex.plugin.obd;

/**
 * Created by 10124 on 2018/4/19.
 */

public class ObdUtil {
    public static byte hexToByte(String hex) {
        hex = hex.toUpperCase();
        char[] hexChars = hex.toCharArray();
        return (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
