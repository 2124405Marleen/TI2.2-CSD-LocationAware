package nl.lorenzostolk.ti22_csd_locationaware.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import nl.lorenzostolk.ti22_csd_locationaware.Controller.MapsDirectionsApiListener;
import nl.lorenzostolk.ti22_csd_locationaware.Controller.MapsDirectionsApiManager;
import nl.lorenzostolk.ti22_csd_locationaware.Model.NoContextAvailableException;
import nl.lorenzostolk.ti22_csd_locationaware.Model.Place;
import nl.lorenzostolk.ti22_csd_locationaware.Model.Route;
import nl.lorenzostolk.ti22_csd_locationaware.Model.SPB;
import nl.lorenzostolk.ti22_csd_locationaware.Model.SQLB;
import nl.lorenzostolk.ti22_csd_locationaware.R;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsDirectionsApiListener {
    //AVANS SCHOOL TIME INVESTMENT by Lorenzo en Marleen 2019
    private GoogleMap mMap;
    private List<Place> avansBuildings;
    private ArrayList<Place> drinkPlaces;
//    private Place nearestPlace;
    private Location currentLocation;
    private Place currentCheckPlace;
    private Button buttonToStatistics;
    private Button buttonToDirection;
    private Button buttonToInfo;
    private Thread locationThread;

    //Request code
    final int REQUEST_CODE = 123;
    final long MIN_TIME = 2500;
    // Distance between location updates (1000m or 1km)
    // if set to 0 then updates are determined bij the MIN_TIME timeout
    final float MIN_DISTANCE = 0;

    //Permissions
    public static final String PERMISSION_STRING
            = android.Manifest.permission.ACCESS_FINE_LOCATION;
    final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    //Location manager and listener
    LocationManager locationManager;
    LocationListener locationListener;

    //    private Location initLastlocation;
    private LatLng currentLatLng;
    private Marker mCurrLocation;
    private MapsDirectionsApiManager MDAM;
    //Polylines Route
    private List<Route> routes;
    private Polyline polyline;
    private Route currentRoute;
    private SQLB sqlb;

//    @Override
//    public void onProviderEnabled(String provider) {
//        Log.d("MyClima", "locationListener: OnProviderEnabled()");
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Log.d("MyClima", "locationListener: OnProviderDisabled()");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        avansBuildings = new ArrayList<>();


        Toast.makeText(this, "Loading Map", Toast.LENGTH_SHORT).show();

        sqlb = SQLB.getInstance(this);
        setupLocationServices();
        getPermissions();
        initStatistics();
        initPlaces();

        MDAM = new MapsDirectionsApiManager(
                this,
                getApplicationContext()
        );

        //SharedPreferences test
        SPB.initPreferences(this);
        try {
            SPB spb = SPB.getInstance();
            spb.safePlaces(avansBuildings);
        } catch (NoContextAvailableException e) {
            e.printStackTrace();
        }

    }

    private void getPermissions() {
//        Toast.makeText(this,"TEST2222",Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, PERMISSION_STRING) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        locationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
//        Toast.makeText(this, "RequestLocationUpdates Set", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Location provider: " + LOCATION_PROVIDER, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ASTI", "onRequestPermissionsResult granted!");
                Toast.makeText(this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
//                updateLocationMarker();
                Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();
//                updateLocationMarker(initLastlocation);

            } else {
                Log.d("ASTI", "onRequestPermissionsResult NOT granted!");
                Toast.makeText(this, "PERMISSION_NOT_GRANTED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateLocationMarker(Location location) {
        buttonToDirection.setEnabled(true);
        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (mCurrLocation != null) {
            mCurrLocation.remove();
        }

        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocation = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15.5f));
    }

    private void setupLocationServices() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationMarker(location);
                if (polyline != null) {
                    updateRouteUI();
                }

                currentLocation = location;


                Place nearestPlace = getNearestPlace(location);
                double distance = distance(location.getLatitude(), location.getLongitude(), nearestPlace.getLatLng().latitude, nearestPlace.getLatLng().longitude);


                if(locationThread == null || distance < 50) {
                    locationThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            long lBegin = System.currentTimeMillis();
                            currentCheckPlace = getNearestPlace(currentLocation);
                            try {
                                while(distance(currentLocation.getLatitude(), currentLocation.getLongitude(), currentCheckPlace.getLatLng().latitude, currentCheckPlace.getLatLng().longitude) < 50){
                                    double d = distance(currentLocation.getLatitude(), currentLocation.getLongitude(), currentCheckPlace.getLatLng().latitude, currentCheckPlace.getLatLng().longitude);
                                    Log.d("ASTI", ("CurrentDistance = " + d + " on " + currentCheckPlace.getName()));
                                    locationThread.wait(10);
                                }

                            } catch (Exception e){

                            } finally {
                                long lEnd = System.currentTimeMillis();

                                long lDuration = lEnd - lBegin;
                                //lDuration is the time spend on x place
                                //x place is currentCheckPlace

                                currentCheckPlace = null;
                                locationThread = null;
                            }
                        }
                    });
                    locationThread.start();
                }


//                Toast.makeText(MapsActivity.this,"Location Changed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String s, int status, Bundle bundle) {
                //temporarily no gps status
                String statusTxt = "";
                switch (status) {
                    case LocationProvider.OUT_OF_SERVICE:
                        statusTxt = "OUT_OF_SERVICE";
                        break;
                    case LocationProvider.AVAILABLE:
                        statusTxt = "AVAILABLE";
                        break;
                    // setting the minTime when requesting location updates will cause the provider to set itself to TEMPORARILY_UNAVAILABLE for minTime milliseconds in order to conserve battery power.
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        statusTxt = "TEMPORARILY_UNAVAILABLE";
                        break;
                }
                Log.d("ASTI", "status: " + statusTxt);
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d("ASTI", "locationListener: OnProviderEnabled()");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("ASTI", "locationListener: OnProviderDisabled()");
            }
        };
    }

    private void initStatistics() {
        buttonToStatistics = findViewById(R.id.map_button_toStatistics);
        buttonToDirection = findViewById(R.id.btnToDirection);
        buttonToInfo = findViewById(R.id.btnToInfo);

        buttonToStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
            }
        });
        buttonToDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MDAM.getDirectionRoutes(currentLatLng, new LatLng(51.590270, 4.764140));
//                Toast.makeText(MapsActivity.this,"MDAM",Toast.LENGTH_SHORT).show();
            }
        });

        buttonToInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, InformationActivity.class));
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

//        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                MapsInitializer.initialize(MapsActivity.this);
                Toast.makeText(MapsActivity.this, "Map Loaded", Toast.LENGTH_SHORT).show();

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    getPermissions();
                }

                if (locationManager.getLastKnownLocation(LOCATION_PROVIDER) == null) {
                    Toast.makeText(MapsActivity.this, "Last known location is unknown", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(MapsActivity.this, "LOCATION_PROVIDER is: " + LOCATION_PROVIDER, Toast.LENGTH_SHORT).show();

                    Location lastlocation = locationManager.getLastKnownLocation(LOCATION_PROVIDER);
                    String i = lastlocation.getLatitude() + "" + lastlocation.getLongitude();
//                    Toast.makeText(MapsActivity.this, i, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MapsActivity.this, "Last known location is \n" + i, Toast.LENGTH_SHORT).show();
                    updateLocationMarker(lastlocation);
                }


            }
        });

        mMapSettings();

    }

    private void trackLocation() {
        mMap.setMyLocationEnabled(true);
    }

    private void mMapSettings() {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(51.585281, 4.794852)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.585281, 4.794852), 15.5f));
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        trackLocation();
        addMarkers();
    }

    private void initPlaces() {
        List<Place> avansBuildingsToSQLite = new ArrayList<>();
        Collections.addAll(avansBuildingsToSQLite,
                new Place("Avans H", new LatLng(51.584018, 4.797174),
                        "https://punt.avans.nl/wp-content/uploads/2016/08/HogeschoollaanBreda-1920.jpg",
                        "Hogeschoollaan main building. "),

                new Place("Avans HQ", new LatLng(51.584565, 4.798295),
                        "https://gebouwen.avans.nl/binaries/content/gallery/iavans/thematic.gebouwen/quadrium/bouwfotos/12-1/01_20170112_104732_web.jpg",
                        "Quadrium building. "),

                new Place("Avans LA", new LatLng(51.585765, 4.792273),
                        "https://gebouwen.avans.nl/binaries/content/gallery/iavans/thematic.gebouwen/lovensdijkstraat/3693_web.jpg",
                        "Lovensdijk A building. "),

                new Place("Avans LC", new LatLng(51.586038, 4.793533),
                        "https://scontent-yyz1-1.cdninstagram.com/vp/06ffdc03e320bc7d07385c7519049295/5E2CD9C4/t51.2885-15/sh0.08/e35/s640x640/47223033_194201228112483_4394909092588572764_n.jpg?_nc_ht=scontent-yyz1-1.cdninstagram.com&_nc_cat=103",
                        "Lovensdijk C building. "),

                new Place("Avans LD"
                        , new LatLng(51.585775, 4.794450),
                        "https://gebouwen.avans.nl/binaries/content/gallery/iavans/thematic.gebouwen/lovensdijkstraat/3711_web.jpg",
                        "Lovensdijk D building. ")
        );

        for (Place p : avansBuildingsToSQLite) {
            sqlb.addOrUpdatePlace(p);
        }
    }

    private void addMarkers() {
        avansBuildings = sqlb.getAllPlaces();
        for (Place p : avansBuildings) {
            Log.d("-------------Places", p.toString());
            mMap.addMarker(new MarkerOptions().position(p.getLatLng()).title(p.getDescription()));
//            System.out.println("SQLite: ID= " + p.getID() + " |name= " + p.getName() + " |latlng= " + p.getLatLng());
        }

    }


    @Override
    public void onRoutesAvailable(List<Route> routes) {
//        System.out.println("Route Json: " + route.getJsonRoute());
        Toast.makeText(MapsActivity.this, "Routes Received", Toast.LENGTH_SHORT).show();

        LatLng origin = new LatLng(51.589320, 4.774480);
        LatLng destination = new LatLng(51.590270, 4.764140);
//        LatLng dest = markerPoints.get(1);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLngBounds bounds = builder.include(origin).include(destination).build();
        int margin = 100; //50
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, margin));

        this.routes = routes;
        setRouteUI();
    }

    private void setRouteUI() {
        PolylineOptions lineOptions = new PolylineOptions();

        LatLng northEast = routes.get(0).getRouteLatsLngs().get(0);
        LatLng southWest = routes.get(0).getRouteLatsLngs().get(1);
        // The first list contained the bounds of the route and is not part of the route:
        routes.remove(0);

        for (Route leg : routes) {
            lineOptions.addAll(leg.getRouteLatsLngs());
        }

        lineOptions.width(10);
        lineOptions.color(Color.BLUE);

        Log.d("onPostExecute", "onPostExecute lineoptions decoded");

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            if (polyline != null) {
                polyline.remove();
//                lineOptions.color(Color.RED);
                polyline = this.mMap.addPolyline(lineOptions);
            } else {
                polyline = this.mMap.addPolyline(lineOptions);
            }
//            mMap.addPolyline(lineOptions);


            // zoom to bounding-box of the route:
            LatLngBounds bounds = new LatLngBounds(southWest, northEast);
            int padding = 80;
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

        } else {
            Log.d("onPostExecute", "without Polylines drawn");
        }
    }

    private void updateRouteUI() {
        PolylineOptions lineOptions = new PolylineOptions();

        LatLng northEast = routes.get(0).getRouteLatsLngs().get(0);
        LatLng southWest = routes.get(0).getRouteLatsLngs().get(1);

        for (Route leg : routes) {
            lineOptions.addAll(leg.getRouteLatsLngs());
        }

        lineOptions.width(10);
        lineOptions.color(Color.BLUE);

        Log.d("onPostExecute", "onPostExecute lineoptions decoded");

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            if (polyline != null) {
                polyline.remove();
                polyline = this.mMap.addPolyline(lineOptions);
            } else {
                polyline = this.mMap.addPolyline(lineOptions);
            }

        } else {
            Log.d("onPostExecute", "without Polylines drawn");
        }
    }

    @Override
    public void onRoutesError(Error error) {
        System.out.println("Error: " + error.toString());
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1609.344;

            return (dist);
        }
    }

    private Place getNearestPlace(Location location){

        Place nearestPlace = null;
        double shortestDistance = distance(location.getLatitude(), location.getLongitude(), avansBuildings.get(0).getLatLng().latitude, avansBuildings.get(0).getLatLng().longitude);;
        for (Place p: avansBuildings) {
            double d = distance(location.getLatitude(), location.getLongitude(), p.getLatLng().latitude, p.getLatLng().longitude);
            Log.d("ASTI", ("Distance = " + p.getName() + " : " + d + " M"));
            if(d < shortestDistance) {
                shortestDistance = d;
                nearestPlace = p;
            }
        }

        if(nearestPlace != null)
        Log.d("ASTI", ("NearestPlace = " + nearestPlace.getName() + "\t : " + shortestDistance + " M"));

        return nearestPlace;
    }
}
