package ro.madeintm.madeintm;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * Created by validraganescu on 21/05/16.
 */
public class LocationAwareActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = "Location";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            Log.d(TAG, "Creating mGoogleApiClient");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        final PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        Log.d(TAG, "Location setting status success");
                        requestLocation();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.d(TAG, "Location setting status resolution required");
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    LocationAwareActivity.this,
                                    1);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d(TAG, "Location setting status unavailable");
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }

            }
        });

    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "Connected mGoogleApiClient");

        int granted = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);
        Log.d(TAG, "Check locaiton permission");
        if (granted != PackageInfo.REQUESTED_PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Log.d(TAG, "Request permission");
            PermissionListener dialogPermissionListener =
                    DialogOnDeniedPermissionListener.Builder
                            .withContext(this)
                            .withTitle("Permission to use location")
                            .withMessage("Stories are displayed on a map and we need your location to provide propper guiding.")
                            .withButtonText(android.R.string.ok)
                            .build();
            Dexter.checkPermission(dialogPermissionListener, Manifest.permission.CAMERA);

        } else {
            Log.d(TAG, "Permission granted");
            if (mLastLocation != null) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                Log.d(TAG, "got location");
            }
            createLocationRequest();
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Suspended mGoogleApiClient");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "mGoogleApiClient connection failed " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "Connecting mGoogleApiClient");
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Disconnecting mGoogleApiClient");
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");
        mLastLocation = location;
    }
}
