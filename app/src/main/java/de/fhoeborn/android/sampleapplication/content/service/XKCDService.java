package de.fhoeborn.android.sampleapplication.content.service;

import java.util.List;

import de.fhoeborn.android.comic_api.Comic;

public interface XKCDService {
    @GET("/info.0.json")
    Comic getCurrentComic();

    @GET("/{number}/info.0.json")
    Comic getComic(@Path("number") int number);
}
