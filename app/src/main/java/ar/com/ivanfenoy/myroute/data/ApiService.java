package ar.com.ivanfenoy.myroute.data;

import ar.com.ivanfenoy.myroute.model.PhotonPlaces;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ivan on 08/12/16.
 */

public interface ApiService {

    @GET("/api/?")
    Call<PhotonPlaces> getPlaces(
            @Query("q") String place,
            @Query("osm_tag") String tag);
}
