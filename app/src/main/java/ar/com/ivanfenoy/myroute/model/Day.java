package ar.com.ivanfenoy.myroute.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ivanj on 31/10/2016.
 */

public class Day implements Parcelable {
    public int dayNumber;
    public long date;
    public String description;
    public String notes;

    public Day(){}

    public Day(int pDayNumber, long pDate){
        this.dayNumber = pDayNumber;
        this.date = pDate;
    }

    //Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dayNumber);
        dest.writeLong(this.date);
        dest.writeString(this.description);
        dest.writeString(this.notes);
    }

    protected Day(Parcel in) {
        this.dayNumber = in.readInt();
        this.date = in.readLong();
        this.description = in.readString();
        this.notes = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
