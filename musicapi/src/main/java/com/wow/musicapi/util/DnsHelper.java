package com.wow.musicapi.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class DnsHelper {

    private static final String DNS = "158.69.209.100";

    @SuppressWarnings("unused")
    public static void testResolve() {
        try {
            System.out.println("resolving m10.music.126.net --> " + InetAddress.getByName("m10.music.126.net"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static List<String> resolveIp(String host) throws IOException {
        String result = CommonUtils.runCmd(new String[]{"dig", "+short", "@" + DNS, host});
        String[] lines = result.split("\n");
        return Arrays.asList(lines);
    }
}
