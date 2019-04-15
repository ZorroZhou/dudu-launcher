package com.wow.musicapi.util;

import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommonUtils {
    public static String generateChinaRandomIP() {
        String ip = "47.93.50." + (1 + RandomHelper.getInstance().nextInt(255));
        return ip;
    }

    public static String runCmd(String[] args) throws IOException {
        Process p = Runtime.getRuntime().exec(args);
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = is.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    public static String unescapeHtmlAndXml(String text) {
        return StringEscapeUtils.unescapeHtml4(StringEscapeUtils.unescapeXml(text));
    }
}
