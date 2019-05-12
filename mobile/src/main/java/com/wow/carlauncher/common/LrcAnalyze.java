package com.wow.carlauncher.common;


import java.util.ArrayList;
import java.util.List;

public class LrcAnalyze {

    /**
     * [ar:艺人名] [ti:曲名] [al:专辑名] [by:编者（指编辑LRC歌词的人）] [offset:时间补偿值]
     * 其单位是毫秒，正值表示整体提前，负值相反。这是用于总体调整显示快慢的。
     */
    // parse taget artist
    private static final String TagAr = "[ar:";

    // perse taget tittle
    private static final String TagTi = "[ti:";

    // perse target album
    private static final String TagAl = "[al:";

    // perse target author of the lrc
    private static final String TagBy = "[by:";

    // perse taget offset
    private static final String TagOff = "[offset:";

    // get lrc artist
    private static final int ARTIST_ZONE = 0;

    // get lrc tittle
    private static final int TITTLE_ZONE = 1;

    // get lrc album
    private static final int ALBUM_ZONE = 2;

    // get lrc author
    private static final int AOTHOR_ZONE = 3;

    // get lrc offset
    private static final int OFFSET_ZONE = 4;

    // get lrc
    private static final int LRC_ZONE = 5;

    // lrc data contract
    public static class LrcData {
        private int type;
        private String Time; // time of string format
        private long TimeMs; // time of long format ms
        private String LrcLine; // one line lrc

        public int getType() {
            return type;
        }

        public LrcData setType(int type) {
            this.type = type;
            return this;
        }

        public String getTime() {
            return Time;
        }

        public LrcData setTime(String time) {
            Time = time;
            return this;
        }

        public long getTimeMs() {
            return TimeMs;
        }

        public LrcData setTimeMs(long timeMs) {
            TimeMs = timeMs;
            return this;
        }

        public String getLrcLine() {
            return LrcLine;
        }

        public LrcData setLrcLine(String lrcLine) {
            LrcLine = lrcLine;
            return this;
        }

        @Override
        public String toString() {
            return "LrcData{" +
                    "type=" + type +
                    ", Time='" + Time + '\'' +
                    ", TimeMs=" + TimeMs +
                    ", LrcLine='" + LrcLine + '\'' +
                    '}';
        }
    }

    // record analyzed lrc
    private List<LrcData> lrcList;

    /**
     * constract
     */
    public LrcAnalyze(String ContentForString) {
        String[] ContentLine = ContentForString.split("\n");
        lrcList = new ArrayList<>();
        for (int i = 0; i < ContentLine.length; i++) {
            lrcAnalyzeLine(ContentLine[i]);
        }
    }

    private long lrcAnalyzeTimeStringToValue(String time) {
        try {
            long hour = 0;
            long minute = 0;
            if (time.indexOf(":") != time.lastIndexOf(":")) {
                hour = Integer.parseInt(time.substring(0, time.indexOf(":")));
                minute = Integer.parseInt(time.substring(time.indexOf(":") + 1, time.lastIndexOf(":")));
            } else {
                minute = Integer.parseInt(time.substring(0, time.lastIndexOf(":")));
            }
            long second = Integer.parseInt(time.substring(time.lastIndexOf(":") + 1, time.lastIndexOf(".")));
            long millisecond = Integer.parseInt(time.substring(time.indexOf(".") + 1));
            return ((hour * 60 + minute) * 60 * 1000 + second * 1000 + millisecond);
        } catch (Throwable e) {
            return -1;
        }

    }

    private void lrcAnalyzeLine(String ContentLine) {
        if (ContentLine.contains(TagAr)) {
            LrcData lrcdata = new LrcData();
            lrcdata.type = ARTIST_ZONE;
            lrcdata.LrcLine = ContentLine.substring(
                    ContentLine.indexOf(':') + 1, ContentLine.lastIndexOf(']'));
            lrcList.add(lrcdata);
        } else if (ContentLine.contains(TagAl)) {
            LrcData lrcdata = new LrcData();
            lrcdata.type = ALBUM_ZONE;
            lrcdata.LrcLine = ContentLine.substring(
                    ContentLine.indexOf(':') + 1, ContentLine.lastIndexOf(']'));
            lrcList.add(lrcdata);
        } else if (ContentLine.contains(TagTi)) {
            LrcData lrcdata = new LrcData();
            lrcdata.type = TITTLE_ZONE;
            lrcdata.LrcLine = ContentLine.substring(
                    ContentLine.indexOf(':') + 1, ContentLine.lastIndexOf(']'));
            lrcList.add(lrcdata);
        } else if (ContentLine.contains(TagBy)) {
            LrcData lrcdata = new LrcData();
            lrcdata.type = AOTHOR_ZONE;
            lrcdata.LrcLine = ContentLine.substring(
                    ContentLine.indexOf(':') + 1, ContentLine.lastIndexOf(']'));
            lrcList.add(lrcdata);
        } else if (ContentLine.contains(TagOff)) {
            LrcData lrcdata = new LrcData();
            lrcdata.type = OFFSET_ZONE;
            lrcdata.LrcLine = ContentLine.substring(
                    ContentLine.indexOf(':') + 1, ContentLine.lastIndexOf(']'));
            lrcList.add(lrcdata);
        } else {
            String[] cut = ContentLine.split("]");
            if (cut.length >= 2) {
                for (int i = 0; i < cut.length - 1; i++) {
                    LrcData lrcdata = new LrcData();
                    lrcdata.type = LRC_ZONE;
                    lrcdata.Time = cut[i]
                            .substring(ContentLine.indexOf('[') + 1);
                    lrcdata.TimeMs = lrcAnalyzeTimeStringToValue(lrcdata.Time);
                    lrcdata.LrcLine = cut[cut.length - 1];
                    if (lrcdata.TimeMs > 0) {
                        lrcList.add(lrcdata);
                    }

                }
            }
        }
    }

    public List<LrcData> lrcList() {
        return lrcList;
    }
}
