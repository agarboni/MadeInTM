package ro.madeintm.madeintm;

import android.location.Location;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import io.fabric.sdk.android.Fabric;

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

    private void addMapBoxConfig(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.mapboxMapView);
        mapView.setAccessToken(getString(R.string.accessToken));
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.EMERALD);
                mapboxMap.setMyLocationEnabled(true);
                Location myLocation = mapboxMap.getMyLocation();
                if (myLocation != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15)
                            .target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                            .build();
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                }
                mMapBoxMap = mapboxMap;
            }
        });

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
