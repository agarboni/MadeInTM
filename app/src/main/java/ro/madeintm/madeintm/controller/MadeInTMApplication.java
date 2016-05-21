package ro.madeintm.madeintm.controller;

import android.app.Application;
import android.graphics.Bitmap;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.karumi.dexter.Dexter;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import io.fabric.sdk.android.Fabric;
import ro.madeintm.madeintm.R;

/**
 * Created by validraganescu on 21/05/16.
 */
public class MadeInTMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        Dexter.initialize(getApplicationContext());
        Firebase.setAndroidContext(this);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher) // resource or drawable
                .showImageForEmptyUri(R.mipmap.ic_launcher) // resource or drawable
                .showImageOnFail(R.mipmap.ic_launcher) // resource or drawable
                .cacheInMemory(true) // default
                .cacheOnDisk(false) // default
                .build();

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(30 * 1024 * 1024))
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);




    }
}
