package ro.madeintm.madeintm.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;

/**
 * Created by validraganescu on 21/05/16.
 */
public class BaseActivity extends AppCompatActivity {

    public Firebase myFirebaseRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myFirebaseRef = new Firebase("https://fiery-inferno-7150.firebaseio.com");
    }
}
