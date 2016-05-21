package ro.madeintm.madeintm.model;

import com.firebase.client.DataSnapshot;

/**
 * Created by validraganescu on 21/05/16.
 */
public class Link {

    private String url;

    public Link(DataSnapshot dataSnapshot) {
        Object oUrl = dataSnapshot.child("url").getValue();
        if (oUrl != null && oUrl instanceof String){
            url = (String) oUrl;

        }
    }

    public String getUrl() {
        return url;
    }
}
