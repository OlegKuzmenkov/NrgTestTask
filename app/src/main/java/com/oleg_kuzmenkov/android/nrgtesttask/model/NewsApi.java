package com.oleg_kuzmenkov.android.nrgtesttask.model;

import com.google.gson.JsonObject;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface NewsApi {

    @GET("top-headlines")
    Observable<NewsList> getNews(@Query("country") String location, @Query("apikey") String key);

    @GET("photos/random")
    Observable<List<JsonObject>> getRandomPhotos(@Query("count") int count, @Query("client_id") String id);
}
