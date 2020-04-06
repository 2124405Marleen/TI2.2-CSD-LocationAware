package nl.lorenzostolk.ti22_csd_locationaware.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

//SQLB = SQLiteBank
//https://guides.codepath.com/android/local-databases-with-sqliteopenhelper
public class SQLB extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "LocationAwareApp";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    private static final String TABLE_PLACES = "places";

    // Post Table Columns
    private static final String KEY_PLACE_ID = "id";
    private static final String KEY_PLACE_UUID = "uuid";
    private static final String KEY_PLACE_NAME = "name";
    private static final String KEY_PLACE_LAT = "lat";
    private static final String KEY_PLACE_LNG = "lng";
    private static final String KEY_PLACE_IMAGE_URL = "imageurl";
    private static final String KEY_PLACE_DESCRIPTION = "description";

    private volatile static SQLB instance;

    public static SQLB getInstance(Context context) {
        if (instance == null) {
            synchronized (SQLB.class) {
                if (instance == null) {
                    instance = new SQLB(context);
                }
            }
        }
        return instance;
    }

    private SQLB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqldb) {
        final String PLACES_TABLE_CREATE =
                "CREATE TABLE " + TABLE_PLACES
                        + " ("
                        + KEY_PLACE_ID + " INTEGER PRIMARY KEY, " // Primary key
                        + KEY_PLACE_UUID + " TEXT, "
                        + KEY_PLACE_NAME + " TEXT, "
                        + KEY_PLACE_LAT + " REAL,"
                        + KEY_PLACE_LNG + " REAL,"
                        + KEY_PLACE_IMAGE_URL + " TEXT,"
                        + KEY_PLACE_DESCRIPTION + " TEXT"
                        + ");";
        Log.d("SQLITE CREATE", PLACES_TABLE_CREATE);
        sqldb.execSQL(PLACES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqldb, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqldb.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
            onCreate(sqldb);
        }
    }


    public void addOrUpdatePlace(Place place) {
        SQLiteDatabase sqldb = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PLACE_UUID, place.getUuid());

            int rows = sqldb.update(TABLE_PLACES, values, KEY_PLACE_UUID + "= ?", new String[]{place.getUuid()});

            values = new ContentValues();
            values.put(KEY_PLACE_UUID, place.getUuid());
            values.put(KEY_PLACE_NAME, place.getName());
            values.put(KEY_PLACE_LAT, place.getLatLng().latitude);
            values.put(KEY_PLACE_LNG, place.getLatLng().longitude);
            values.put(KEY_PLACE_IMAGE_URL, place.getImageURL());
            values.put(KEY_PLACE_DESCRIPTION, place.getDescription());

            if (rows == 1) {
                sqldb.update(TABLE_PLACES, values, KEY_PLACE_UUID + "= ?", new String[]{place.getUuid()});
                Log.d("++++++", "update");
            } else {
                sqldb.insert(TABLE_PLACES, null, values);
                Log.d("++++++", "insert");
            }
        } catch (Exception e) {
            Log.d("SQLITE ERROR", "Error while trying to add or update user");
        } finally {
            sqldb.close();
        }
    }

    // Get all posts in the database
    public List<Place> getAllPlaces() {
        List<Place> places = new ArrayList<>();

        SQLiteDatabase sqldb = getReadableDatabase();
        Cursor cursor = sqldb.query(TABLE_PLACES,
                new String[]{"*"},
                null, null, null, null, null + " DESC");

        try {
            if (cursor.moveToFirst()) {
                do {
                    //String ID, String name, LatLng latLng, String imageURL, String description

                    Place place = new Place(
                            cursor.getInt(cursor.getColumnIndex(KEY_PLACE_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_PLACE_UUID)),
                            cursor.getString(cursor.getColumnIndex(KEY_PLACE_NAME)),
                            new LatLng(
                                    cursor.getDouble(cursor.getColumnIndex(KEY_PLACE_LAT)),
                                    cursor.getDouble(cursor.getColumnIndex(KEY_PLACE_LNG))),
                            cursor.getString(cursor.getColumnIndex(KEY_PLACE_IMAGE_URL)),
                            cursor.getString(cursor.getColumnIndex(KEY_PLACE_DESCRIPTION))
                    );
                    places.add(place);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("SQLITE addPlace()", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return places;
    }


}
