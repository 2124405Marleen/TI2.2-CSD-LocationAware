package nl.lorenzostolk.ti22_csd_locationaware.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// SPB = SharedPreferencesBank
public class SPB {

    private volatile static SPB instance;

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static boolean isContextSet = false;

    private SPB() {

    }

    public static void initPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        isContextSet = true;
    }

    public static SPB getInstance() throws NoContextAvailableException {
        if (isContextSet) {
            if (instance == null) {
                synchronized (SPB.class) {
                    if (instance == null) {
                        instance = new SPB();
                    }
                }
            }
        } else {
            throw new NoContextAvailableException();
        }

        return instance;

    }


    public void safePlaces(List<Place> placesAdd) {
        List<Place> places = new ArrayList<>();
        editor = preferences.edit();
        boolean isChanged = false;

        for (Place pa : placesAdd) {
            if (!places.contains(pa)) {
                places.add(pa);
                isChanged = true;
            }
        }

        if (isChanged) {
            Gson gson = new Gson();
            editor.clear();
            for (int i = 0; i < places.size(); i++) {
                Place p = places.get(i);
                String jsonPlace = gson.toJson(p);
                editor.putString(String.valueOf(i), jsonPlace);
            }
            editor.apply();
        }
    }

//    public void safePlace(Place place) {
//
//    }

    public List<Place> getPlaces() {
        List<Place> places = new ArrayList<>();

        Gson gson = new Gson();
        for (int i = 0; i < preferences.getAll().size(); i++) {
            String jsonPlace = preferences.getString(String.valueOf(i), "");
            Place p = gson.fromJson(jsonPlace, Place.class);
            places.add(p);
        }

        return places;
    }


}

