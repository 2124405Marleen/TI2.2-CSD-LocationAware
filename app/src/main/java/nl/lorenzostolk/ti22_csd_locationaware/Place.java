package nl.lorenzostolk.ti22_csd_locationaware;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    //AVANS SCHOOL TIME INVESTMENT
    private String name;
    private LatLng latLng;
    private String imageURL;
    private String description;

    public Place(String name, LatLng latLng, String imageURL, String description) {
        this.name = name;
        this.latLng = latLng;
        this.imageURL = imageURL;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}