package ar.com.ivanfenoy.myroute.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ar.com.ivanfenoy.myroute.interfaces.Error404Exception;
import ar.com.ivanfenoy.myroute.interfaces.ListCallback;
import ar.com.ivanfenoy.myroute.interfaces.ObjectCallback;
import ar.com.ivanfenoy.myroute.model.PhotonPlaces;
import ar.com.ivanfenoy.myroute.utils.Interceptor;
import ar.com.ivanfenoy.myroute.utils.Logger;
import ar.com.ivanfenoy.myroute.utils.Utils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ivan on 08/12/16.
 */

public class ApiController {
    private static final String WS_ENDPOINT = "http://photon.komoot.de/";
    private ApiService service;
    private Context context;
    private OkHttpClient rest;

    public static final long READ_TIMEOUT = 30;
    public static final long WRITE_TIMEOUT = 60;

    public ApiController(Context context) {
        this.context = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WS_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client())
                .build();

        service = retrofit.create(ApiService.class);
    }

    private OkHttpClient client() {
        if(rest == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
            if(true) {
                Interceptor logging = new Interceptor(new HttpLoggingInterceptor.Logger() {
                    @Override public void log(String message) {
                        Logger.i(ApiController.class.getName(), message);
                    }
                });
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addNetworkInterceptor(logging);
            }
            rest = builder.build();
        }
        return rest;
    }

    public void getPlaces(String pCity, String pFilter, final ObjectCallback callback) {
        if(!Utils.isConnected(context)) {
            callback.done(new Exception(), null);
            return;
        }
        service.getPlaces(pCity, pFilter).enqueue(new Callback<PhotonPlaces>() {
            @Override
            public void onResponse(Call<PhotonPlaces> call, Response<PhotonPlaces> response) {
                if (response.code() != 200) {
                    callback.done(new Error404Exception(), null);
                    return;
                }
                callback.done(null, response.body());
            }

            @Override
            public void onFailure(Call<PhotonPlaces> call, Throwable t) {
                if (t != null) {
                    callback.done((Exception) t, null);
                    return;
                }
            }
        });
    }

}
