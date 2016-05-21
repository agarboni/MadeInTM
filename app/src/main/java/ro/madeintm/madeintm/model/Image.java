package ro.madeintm.madeintm.model;

import com.firebase.client.DataSnapshot;

/**
 * Created by validraganescu on 21/05/16.
 */
public class Image {

    public static final String ALT_TEXT = "alt-text";
    public static final String URL = "url";

    private String altText;
    private String url;

    public Image(DataSnapshot imageSnapshot) {
        Object oAltText = imageSnapshot.child(ALT_TEXT).getValue();
        if (oAltText != null && oAltText instanceof String){
            altText = (String) oAltText;
        }

        Object oUrl = imageSnapshot.child(URL).getValue();
        if (oUrl != null && oUrl instanceof String ){
            url = (String) oUrl;
        }
    }



    public String getAltText() {
        return altText;
    }

    public String getUrl() {
        return url;
    }
}
