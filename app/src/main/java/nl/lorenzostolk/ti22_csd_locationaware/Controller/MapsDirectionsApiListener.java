package nl.lorenzostolk.ti22_csd_locationaware.Controller;

import java.util.List;

import nl.lorenzostolk.ti22_csd_locationaware.Model.Route;

public interface MapsDirectionsApiListener {

    void onRoutesAvailable (List<Route> routes);
    void onRoutesError(Error error);
}
