package ar.com.ivanfenoy.myroute.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ivanj on 1/11/2016.
 */

public class SleepPlace implements Parcelable {
    public String name;
    public String address;
    public double latitude;
    public double longitude;
    public String info;

    public SleepPlace(){}


    //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.info);
        dest.writeString(this.address);
    }

    protected SleepPlace(Parcel in) {
        this.name = in.readString();
        this.latitude = in.readFloat();
        this.longitude = in.readFloat();
        this.info = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<SleepPlace> CREATOR = new Parcelable.Creator<SleepPlace>() {
        @Override
        public SleepPlace createFromParcel(Parcel source) {
            return new SleepPlace(source);
        }

        @Override
        public SleepPlace[] newArray(int size) {
            return new SleepPlace[size];
        }
    };
}
