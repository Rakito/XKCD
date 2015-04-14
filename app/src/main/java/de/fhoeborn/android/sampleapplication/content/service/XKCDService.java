package de.fhoeborn.android.sampleapplication.content.service;


import de.fhoeborn.android.comic_api.Comic;
import retrofit.http.GET;
import retrofit.http.Path;

public interface XKCDService {
    @GET("/info.0.json")
    Comic getCurrentComic();

    @GET("/{number}/info.0.json")
    Comic getComic(@Path("number") int number);
}
