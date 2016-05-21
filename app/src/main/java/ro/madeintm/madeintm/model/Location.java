package ro.madeintm.madeintm.model;

import com.firebase.client.DataSnapshot;

/**
 * Created by validraganescu on 21/05/16.
 */
public class Location extends Model {

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private double lat;
    private double lon;

    public Location(DataSnapshot dataSnapshot) {
        Object oLat = dataSnapshot.child(LATITUDE).getValue();
        if (oLat != null && oLat instanceof Double) {
            lat = (Double) oLat;
        }

        Object oLon = dataSnapshot.child(LONGITUDE).getValue();
        if (oLon != null && oLon instanceof Double){
            lon = (Double) oLon;
        }
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
