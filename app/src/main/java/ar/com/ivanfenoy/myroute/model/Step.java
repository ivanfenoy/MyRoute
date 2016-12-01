package ar.com.ivanfenoy.myroute.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ivanj on 2/11/2016.
 */

public class Step implements Parcelable {
    public String name;
    public Point point;
    public ArrayList<Day> listDays;
    public SleepPlace sleepPlace;

    public Step(){}

    public Step(String pName, Day pDay){
        this.name = pName;
        this.listDays = new ArrayList<>();
        this.listDays.add(pDay);
    }

    //Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.point, flags);
        dest.writeTypedList(this.listDays);
        dest.writeParcelable(this.sleepPlace, flags);
    }

    protected Step(Parcel in) {
        this.name = in.readString();
        this.point = in.readParcelable(Point.class.getClassLoader());
        this.listDays = in.createTypedArrayList(Day.CREATOR);
        this.sleepPlace = in.readParcelable(SleepPlace.class.getClassLoader());
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
