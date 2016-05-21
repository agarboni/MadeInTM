package ro.madeintm.madeintm.controller;

import android.app.Application;

import com.firebase.client.Firebase;
import com.karumi.dexter.Dexter;

/**
 * Created by validraganescu on 21/05/16.
 */
public class MadeInTMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Dexter.initialize(getApplicationContext());
        Firebase.setAndroidContext(this);

    }
}
