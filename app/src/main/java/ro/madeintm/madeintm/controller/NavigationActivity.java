package ro.madeintm.madeintm.controller;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ro.madeintm.madeintm.R;
import ro.madeintm.madeintm.model.Story;

public class NavigationActivity extends LocationAwareActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PATH_COLOR = "#edec09";
    public static final String STORIES = "stories";
    private MapView mapView = null;
    private MapboxMap mMapBoxMap;
    public static List<Story> stories = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
            toolbar.setTitle("Made In TM");
        }
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addMapBoxConfig(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.random_list: {
                Intent intent = new Intent(this, StoryListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.categories: {
                Intent intent = new Intent(this, CategorySelectionActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //========================
    // Map Box stuff
    //=========================
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
                getStories();

                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                        int index = Integer.valueOf(marker.getSnippet());
                        intent.putExtra("object", stories.get(index));
                        startActivity(intent);
                        return true;
                    }
                });
            }
        });
    }

    private void getStories() {
        myFirebaseRef.child(STORIES).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Story> stories = new ArrayList<>();
                Log.d(TAG, "Firebase result: " + snapshot.getChildrenCount());
                for (DataSnapshot storiesSnapShot : snapshot.getChildren()) {
                    Story story = new Story(storiesSnapShot);
                    stories.add(story);
                }
                Log.d(TAG, "We have " + stories.size() + " stories");
                NavigationActivity.stories.clear();
                NavigationActivity.stories.addAll(stories);
                addToMap(stories);

            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.d(TAG, "Firebase error: " + error.getMessage());
            }

        });
    }

    private void setCamera(MapboxMap mapboxMap, double lat, double lon) {
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(13)
                .target(new LatLng(lat, lon))
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }

    private void addToMap(List<Story> stories) {
        if (mMapBoxMap == null) {
            createMap();
        } else {
            mMapBoxMap.removeAnnotations();
            int i = 0;
            for (Story story : stories) {
                for (ro.madeintm.madeintm.model.Location location : story.getLocations()) {
                    mMapBoxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.getLat(), location.getLon()))
                            .title(story.getTitle())
                            .snippet(String.valueOf(i)));
                }
                i++;

            }
        }
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
                    .color(Color.parseColor(PATH_COLOR))
                    .width(5));

        }

        @Override
        public void onFailure(Throwable t) {
            Log.d(TAG, "Route failed: " + t.getMessage());
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
