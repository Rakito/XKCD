package de.fhoeborn.android.comic_api;

import com.google.gson.annotations.SerializedName;

public class Comic {
    private String title;
    @SerializedName("img")
    private String imageUrl;
    @SerializedName("alt")
    private String altText;
    @SerializedName("num")
    private int number;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
