package nl.lorenzostolk.ti22_csd_locationaware.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {

//    private String jsonRoute;
    private List<LatLng> routeLatsLngs;

    public Route(List<LatLng> routeLatsLngs) {
        this.routeLatsLngs = routeLatsLngs;
    }

    public List<LatLng> getRouteLatsLngs() {
        return routeLatsLngs;
    }

    public void setRouteLatsLngs(List<LatLng> routeLatsLngs) {
        this.routeLatsLngs = routeLatsLngs;
    }
}
