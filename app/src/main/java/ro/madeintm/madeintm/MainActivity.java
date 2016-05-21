package ro.madeintm.madeintm;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.crashlytics.android.Crashlytics;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private MapView mapView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
