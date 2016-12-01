package ar.com.ivanfenoy.myroute.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ivanj on 31/10/2016.
 */

public class Trip implements Parcelable {
    public String id;
    public String name;
    public String description;
    public int kilometers;
    public String image;
    public long initDate;
    public ArrayList<Step> listSteps;

    public Trip(){}

    public Trip(String pname, long pDate, String pImage) {
        this.name = pname;
        this.listSteps = new ArrayList<>();
        this.initDate = pDate;
        this.image = pImage;
    }

    public int getDaysCount(){
        int wDays = 0;
        if (listSteps !=null && listSteps.size() > 0) {
            for (Step wStep : listSteps) {
                for (Day wDay : wStep.listDays) {
                    wDays++;
                }
            }
        }
        return wDays;
    }

    public long getLastDate() {
        long wlastDate = 0;
        if (listSteps !=null && listSteps.size() > 0) {
            for (Step wStep : listSteps) {
                for (Day wDay : wStep.listDays) {
                    if (wDay.date > wlastDate) {
                        wlastDate = wDay.date;
                    }
                }
            }
        }
        else{
            wlastDate = this.initDate;
        }
        return wlastDate;
    }

    //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.kilometers);
        dest.writeString(this.image);
        dest.writeLong(this.initDate);
        dest.writeTypedList(this.listSteps);
    }

    protected Trip(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.kilometers = in.readInt();
        this.image = in.readString();
        this.initDate = in.readLong();
        this.listSteps = in.createTypedArrayList(Step.CREATOR);
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel source) {
            return new Trip(source);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}