package nl.lorenzostolk.ti22_csd_locationaware.Controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.lorenzostolk.ti22_csd_locationaware.Controller.MapsDirectionsApiListener;
import nl.lorenzostolk.ti22_csd_locationaware.Model.Route;

public class MapsDirectionsApiManager {
    private RequestQueue requestQueue;
    private final String key = "725b85df-2ff8-4de2-bebc-2ab56b12b701";
    private final String avansUrl = "http://145.48.6.80:3000/directions";
    final String url1 = "http://145.48.6.80:3000/directions?origin=51.589320,4.774480&destination=51.590270,4.764140&mode=walking&key=725b85df-2ff8-4de2-bebc-2ab56b12b701";
//    final String url2 = "https://maps.googleapis.com/maps/api/directions/json?origin=51.589320,4.774480&destination=51.590270,4.764140&key=AIzaSyDan10bLOX0am-X--xe0FzB0Uu_5KYU6qk";

    private MapsDirectionsApiListener listener;

    public MapsDirectionsApiManager(MapsDirectionsApiListener listener, Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.listener = listener;
    }

    public void getDirectionRoutes(LatLng origin, LatLng destination){
//        String origin = "51.589320,4.774480";
//        String destination = "51.590270,4.764140";
        String url = avansUrl + "?origin=" + origin.latitude + "," + origin.longitude + "&destination=" + destination.latitude + "," + destination.longitude + "&mode=walking&key=" + key;
        System.out.println("_____________URL: " + url);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //callbacks
                        List<Route> routes = new ArrayList<>();

                        for (List l : parseRoutesInfo(response)) {
                            routes.add(new Route(l));
                        }
                        if(routes.size() != 0){
                            listener.onRoutesAvailable(routes);
                        }
                        else {

                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRoutesError(new Error(error));
                    }
                }
        );

        this.requestQueue.add(request);
    }

    public List<List<LatLng>> parseRoutesInfo(JSONObject jObject){

        LatLng northEast = null;
        LatLng southWest = null;

        List<List<LatLng>> routes = new ArrayList<>() ;
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {

            jRoutes = jObject.getJSONArray("routes");

            if (jRoutes.length() > 0){
                JSONObject nortEastJson = ((JSONObject)jRoutes.get(0)).getJSONObject("bounds").getJSONObject("northeast");
                northEast = new LatLng(nortEastJson.getDouble("lat"), nortEastJson.getDouble("lng"));
                JSONObject southWestJson = ((JSONObject)jRoutes.get(0)).getJSONObject("bounds").getJSONObject("southwest");
                southWest = new LatLng(southWestJson.getDouble("lat"), southWestJson.getDouble("lng"));
                List<LatLng> bounds = new ArrayList<>();
                bounds.add(northEast);
                bounds.add(southWest);
                routes.add(bounds);
            }

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        routes.add(list);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }


        return routes;
    }

    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
