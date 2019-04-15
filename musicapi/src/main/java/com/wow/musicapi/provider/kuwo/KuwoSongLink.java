package com.wow.musicapi.provider.kuwo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.wow.musicapi.model.BaseBean;
import com.wow.musicapi.model.MusicLink;

/**
 * Created by haohua on 2018/2/23.
 * <Song>
 * <music_id>93385</music_id>
 * <mv_rid>MV_0</mv_rid>
 * <name>不再犹豫</name>
 * <song_url>http://yinyue.kuwo.cnhttp://yinyue.kuwo.cn/yy/gequ-Beyond_buzaiyouyu/93385.htm</song_url>
 * <artist>Beyond</artist>
 * <artid>1250</artid>
 * <singer>Beyond</singer>
 * <special>Recognition</special>
 * <ridmd591>BC52F4EC8657AC1C3C49C9053B4B9FAA</ridmd591>
 * <mp3size>9.45 MB</mp3size>
 * <artist_url>http://yinyue.kuwo.cnhttp://yinyue.kuwo.cn/yy/geshou-Beyond/Beyond.htm</artist_url>
 * <auther_url>http://www.kuwo.cn/mingxing/Beyond/</auther_url>
 * <playid>play?play=MQ==&num=MQ==&name0=srvU2dPM1KU=&artist0=QmV5b25k&ssig10=MzgzMjkwODY0MA==&ssig20=Mzg2Njc4ODEyMA
 * ==&musicrid0=TVVTSUNfOTMzODU=&mvrid0=TVZfMA==&mp3size0=OS40NSBNQg==&mrid0=TVAzXzkzMzg1&msig10=ODkzMzUwMzM1&msig20
 * =Mjk0Mjc0OTE3&mkvnsig10=MjEyOTE2MDEwMw==&mkvnsig20=MzE4NTY5NjE3NA==&mkvrid0=TVZfMjk4MTg4&mvsig10=MA==&mvsig20=MA
 * ==&size0=My44MSBNQg==&album0=UmVjb2duaXRpb24=&kalaok0=MA==&hasecho0=MQ==&filetype0=c29uZw==&score0=Mw==&source0
 * =aHR0cDovL3dlbnJzb25ncy5mbG93ZXJnb29kLmNvbS91cGxvYWRmaWxlLzM1OTU1NzU3NS53bWE=&mvprovider0=&</playid>
 * <artist_pic>http://img1.kuwo.cn/star/starheads/120/8/5964989611934e09fd33690cd7aa279_0.jpg</artist_pic>
 * <artist_pic240>http://img1.kuwo.cn/star/starheads/120/14/69/139406023.jpg</artist_pic240>
 * <path>m1/ape2wma_20090522/1/72/1741214903.wma</path>
 * <mp3path>n3/16/80/3217671453.mp3</mp3path>
 * <aacpath>a2/24/33/3570379210.aac</aacpath>
 * <wmadl>wmadl.cdn.kuwo.cn</wmadl>
 * <mp3dl>other.web.rc01.sycdn.kuwo.cn</mp3dl>
 * <aacdl>other.web.ra03.sycdn.kuwo.cn</aacdl>
 * <lyric>DBYAHlReXEpRUEAeCgxVEgAORRgLG0MXCRgaCwoRAB5UAwEaBAkEBhwaXxcAHVReSAsMAVEkOj0wJjpVWlxWTA==</lyric>
 * <lyric_zz>DBYAHlReXEpRUEAeCgxVEgAORRgLG0MXCRgaCwoRAB5UAwEaBAkEBhwaXxcAHVReSAsMAVEkOj0wJjpVWlxWTEMJHgoXU0g=</lyric_zz>
 * </Song>
 */
@SuppressWarnings("SpellCheckingInspection")
@JacksonXmlRootElement(localName = "Song")
@JsonIgnoreProperties
class KuwoSongLink extends BaseBean implements MusicLink {
    @JacksonXmlProperty(localName = "mp3dl")
    private String mp3dl;

    @JacksonXmlProperty(localName = "mp3path")
    private String mp3path;

    @JacksonXmlProperty(localName = "mp3size")
    private String mp3size;

    @JacksonXmlProperty(localName = "singer")
    private String singer;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "artist_pic")
    private String artist_pic;

    @Override
    public String getUrl() {
        return "http://" + mp3dl + "/resource/" + mp3path;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public String getSongId() {
        return null;
    }

    @Override
    public long getBitRate() {
        return 0;
    }

    @Override
    public String getMd5() {
        return null;
    }

    public void setUrl(String url) {
        throw new UnsupportedOperationException();
    }
}
