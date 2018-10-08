package com.chenminglin.cleananimation;

import android.os.Parcel;
import android.os.Parcelable;

public class Config implements Parcelable {
    long duration;
    float rate;
    int bubbleNum;
    int drawFrequency;
    long junkSize;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.duration);
        dest.writeFloat(this.rate);
        dest.writeInt(this.bubbleNum);
        dest.writeInt(this.drawFrequency);
        dest.writeLong(this.junkSize);
    }

    public Config() {
    }

    protected Config(Parcel in) {
        this.duration = in.readLong();
        this.rate = in.readFloat();
        this.bubbleNum = in.readInt();
        this.drawFrequency = in.readInt();
        this.junkSize = in.readLong();
    }

    public static final Parcelable.Creator<Config> CREATOR = new Parcelable.Creator<Config>() {
        @Override
        public Config createFromParcel(Parcel source) {
            return new Config(source);
        }

        @Override
        public Config[] newArray(int size) {
            return new Config[size];
        }
    };
}
