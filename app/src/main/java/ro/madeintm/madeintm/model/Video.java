package ro.madeintm.madeintm.model;

import com.firebase.client.DataSnapshot;

/**
 * Created by validraganescu on 21/05/16.
 */
public class Video {
    public static final String URL = "url";

    private String url;

    public Video(String url) {
        this.url = url;
    }

    public Video(DataSnapshot videoSnapshot) {
        if (videoSnapshot != null){
            Object oUrl = videoSnapshot.child(URL).getValue();
            if (oUrl != null && oUrl instanceof String){
                url = (String) oUrl;
            }
        }
    }

    public String getUrl() {
        return url;
    }
}
