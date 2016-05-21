package ro.madeintm.madeintm.controller;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mapbox.directions.DirectionsCriteria;
import com.mapbox.directions.MapboxDirections;
import com.mapbox.directions.service.models.DirectionsResponse;
import com.mapbox.directions.service.models.DirectionsRoute;
import com.mapbox.directions.service.models.Waypoint;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ro.madeintm.madeintm.R;
import ro.madeintm.madeintm.model.Story;

public class MainActivity extends LocationAwareActivity {

    private MapView mapView = null;
    private MapboxMap mMapBoxMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        addMapBoxConfig(savedInstanceState);

    }

    private void getStories() {
        myFirebaseRef.child("stories").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Story> stories = new ArrayList<>();
                Log.d(TAG, "Firebase result: " + snapshot.getChildrenCount());
                for (DataSnapshot storiesSnapShot : snapshot.getChildren()){
                    Story story = new Story(storiesSnapShot);
                    stories.add(story);
                }
                Log.d(TAG, "We have " + stories.size() + " stories");

                addToMap(stories);

            }

            @Override public void onCancelled(FirebaseError error) {
                Log.d(TAG, "Firebase error: " + error.getMessage());
            }

        });
    }


    private void addMapBoxConfig(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.mapboxMapView);
        mapView.setAccessToken(getString(R.string.accessToken));
        mapView.onCreate(savedInstanceState);

        createMap();

    }

    private void createMap() {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.DARK);
                mapboxMap.setMyLocationEnabled(true);
                Location myLocation = mapboxMap.getMyLocation();
                if (myLocation != null) {
                    setCamera(mapboxMap, myLocation.getLatitude(), myLocation.getLongitude());
                }
                mMapBoxMap = mapboxMap;

//                getRoute();
                getStories();
            }
        });
    }

    private void setCamera(MapboxMap mapboxMap, double lat, double lon) {
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(13)
                .target(new LatLng(lat, lon))
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

    }

    private void getRoute() {
        Log.d(TAG, "get Route");
        Location myLocation = mMapBoxMap.getMyLocation();
        if (myLocation != null) {
            Log.d(TAG, "Lat: " + myLocation.getLatitude() + ", Lon: " + myLocation.getLongitude());
            Waypoint origin = new Waypoint(myLocation.getLongitude(), myLocation.getLatitude());

            // Santa Monica Pier
            Waypoint destination = new Waypoint(21.2281851, 45.757664);

            // Build the client object
            MapboxDirections client = new MapboxDirections.Builder()
                    .setAccessToken(getString(R.string.accessToken))
                    .setOrigin(origin)
                    .setDestination(destination)
                    .setProfile(DirectionsCriteria.PROFILE_WALKING)
                    .build();

            client.enqueue(new DirectionsCallback());
        } else {

            Log.d(TAG, "Location in null");
        }

    }

    private class DirectionsCallback implements Callback<DirectionsResponse> {

        @Override
        public void onResponse(Response<DirectionsResponse> response, Retrofit retrofit) {
            Log.d(TAG, "Route received");
            DirectionsRoute route = response.body().getRoutes().get(0);
            List<Waypoint> waypoints = route.getGeometry().getWaypoints();
            LatLng[] points = new LatLng[waypoints.size()];
            for (int i = 0; i < waypoints.size(); i++) {
                points[i] = new LatLng(
                        waypoints.get(i).getLatitude(),
                        waypoints.get(i).getLongitude());
            }

            mMapBoxMap.addPolyline(new PolylineOptions()
                    .add(points)
                    .color(Color.parseColor("#edec09"))
                    .width(5));

        }

        @Override
        public void onFailure(Throwable t) {
            Log.d(TAG, "Route failed: " + t.getMessage());
        }
    }

    private void addToMap(List<Story> stories) {
        if (mMapBoxMap == null){
            createMap();
        } else {
            mMapBoxMap.removeAnnotations();
            for (Story story : stories) {
                for (ro.madeintm.madeintm.model.Location location : story.getLocations()) {
                    mMapBoxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getLat(), location.getLon()))
                            .title(story.getTitle())
                            .snippet(story.getTagLine()));
                }

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }


}
