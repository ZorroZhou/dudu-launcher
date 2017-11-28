package com.acloud.newinterface;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicBean implements Parcelable {
    public static final Creator<MusicBean> CREATOR = new Creator() {
        public MusicBean createFromParcel(Parcel paramAnonymousParcel) {
            return new MusicBean(paramAnonymousParcel);
        }

        public MusicBean[] newArray(int paramAnonymousInt) {
            return new MusicBean[paramAnonymousInt];
        }
    };
    public static final int TYPE_PLAYING_ITEM = 1;
    public static final int TYPE_TRACK_LIST_ITEM = 0;
    public String ALBUM;
    public String ALBUM_ID;
    public String ARTIST;
    public String DATA;
    public String DISPLAY_NAME;
    public String DURATION;
    public String ID;
    public String MOUNT_PATH;
    public String PARENT_PATH;
    public String SIZE;
    public String TEMP_ALBUM;
    public String TEMP_ARTIST;
    public String TITLE;
    public int TYPE = 0;

    public MusicBean() {
    }

    private MusicBean(Parcel paramParcel) {
        this.ID = paramParcel.readString();
        this.DATA = paramParcel.readString();
        this.SIZE = paramParcel.readString();
        this.ARTIST = paramParcel.readString();
        this.ALBUM_ID = paramParcel.readString();
        this.DISPLAY_NAME = paramParcel.readString();
        this.TITLE = paramParcel.readString();
        this.DURATION = paramParcel.readString();
        this.TYPE = paramParcel.readInt();
        this.ALBUM = paramParcel.readString();
        this.PARENT_PATH = paramParcel.readString();
        this.TEMP_ARTIST = paramParcel.readString();
        this.TEMP_ALBUM = paramParcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object paramObject) {
        if ((paramObject instanceof MusicBean)) {
            paramObject = (MusicBean) paramObject;
            return (paramObject != null) && (this.DATA != null) && (this.DATA.equals(((MusicBean) paramObject).DATA)) && (this.TYPE == ((MusicBean) paramObject).TYPE);
        }
        return super.equals(paramObject);
    }

    public String getLetterKeyValue() {
        return this.DISPLAY_NAME;
    }

    public int hashCode() {
        int i = super.hashCode();
        if (this.DATA != null) {
            i = this.DATA.hashCode() * 31 + this.TYPE;
        }
        return i;
    }

    public String toString() {
        return "id = " + this.ID + ",DATA = " + this.DATA + ",Size = " + this.SIZE + ",Artist = " + this.ARTIST + ",Albums_id = " + this.ALBUM_ID + "display_name = " + this.DISPLAY_NAME + ",Title = " + this.TITLE + ",Duration = " + this.DURATION + ",type = " + this.TYPE + ",ALBUM = " + this.ALBUM + ",PARENT_PATH =" + this.PARENT_PATH;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeString(this.ID);
        paramParcel.writeString(this.DATA);
        paramParcel.writeString(this.SIZE);
        paramParcel.writeString(this.ARTIST);
        paramParcel.writeString(this.ALBUM_ID);
        paramParcel.writeString(this.DISPLAY_NAME);
        paramParcel.writeString(this.TITLE);
        paramParcel.writeString(this.DURATION);
        paramParcel.writeInt(this.TYPE);
        paramParcel.writeString(this.ALBUM);
        paramParcel.writeString(this.PARENT_PATH);
        paramParcel.writeString(this.TEMP_ARTIST);
        paramParcel.writeString(this.TEMP_ALBUM);
    }
}


/* Location:              C:\Users\10124\Desktop\android\com.nwd.android.music.ui\classes-dex2jar.jar!\com\acloud\newinterface\MusicBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */