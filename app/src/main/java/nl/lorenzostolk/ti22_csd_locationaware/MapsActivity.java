package nl.lorenzostolk.ti22_csd_locationaware;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Place> avansBuildings;
    private ArrayList<Place> drinkPlaces;
    private Button buttonToStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        avansBuildings = new ArrayList<>();
        initPlaces();
        initStatistics();
    }

    private void initStatistics() {
        buttonToStatistics = findViewById(R.id.map_button_toStatistics);
        buttonToStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
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


        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(51.585281,4.794852)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.585281,4.794852), 15.5f));
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        addMarkers();

    }

    private void initPlaces() {
        Collections.addAll(avansBuildings,
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
    }

    private void addMarkers() {

        for (Place p : avansBuildings
        ) {
            mMap.addMarker(new MarkerOptions().position(p.getLatLng()).title(p.getDescription()));
        }

    }


}
