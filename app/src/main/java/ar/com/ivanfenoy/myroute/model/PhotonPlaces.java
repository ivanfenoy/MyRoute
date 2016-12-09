package ar.com.ivanfenoy.myroute.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 08/12/16.
 */

public class PhotonPlaces {

    @SerializedName("features")
    public ArrayList<Feature> listFeatures;

    public class Feature{
        @SerializedName("properties")
        public Place place;

        @SerializedName("type")
        public String type;

        @SerializedName("geometry")
        public Geometry point;

    }

    public class Place{
        @SerializedName("name")
        public String name;

        @SerializedName("state")
        public String state;

        @SerializedName("country")
        public String country;

        @SerializedName("osm_key")
        public String osmKey;

        @SerializedName("osm_value")
        public String osmValue;

        @SerializedName("osm_type")
        public String osmType;

        @SerializedName("osm_id")
        public String osmId;

        @SerializedName("extent")
        public List<Double> extent = null;
    }

    public class Geometry {

        @SerializedName("type")
        public String type;

        @SerializedName("coordinates")
        public List<Double> coordinates = null;

    }
}
