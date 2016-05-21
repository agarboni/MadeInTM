package ro.madeintm.madeintm.model;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by validraganescu on 21/05/16.
 */
public class Story extends Model {
    public static final String AUTHOR = "author";
    public static final String DATE = "date";
    public static final String ICON = "icon";
    public static final String IMAGES = "images";
    public static final String LOCATIONS = "locations";
    public static final String TAGS = "tags";
    public static final String TAGLINE = "tagline";
    public static final String TEXT = "text";
    public static final String TITLE = "title";
    public static final String VIDEOS = "videos";
    public static final String LINKS = "links";

    private String author = "";
    private String date = "";
    private String icon = "";
    private List<Image> images = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String tagLine = "";
    private String text = "";
    private String title = "";
    private List<Video> videos = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    public Story(DataSnapshot snapshot) {
        update(snapshot);
    }

    public void update(DataSnapshot snapshot) {
        if (snapshot != null) {
            Object oAuthor = snapshot.child(AUTHOR).getValue();
            if (oAuthor != null && oAuthor instanceof String) {
                author = (String) oAuthor;
            }

            Object oDate = snapshot.child(DATE).getValue();
            if (oDate != null && oDate instanceof String) {
                date = (String) oDate;
            }

            Object oIcon = snapshot.child(ICON).getValue();
            if (oIcon != null && oIcon instanceof String) {
                icon = (String) oIcon;
            }

            DataSnapshot snapshotImages = snapshot.child(IMAGES);
            if (snapshotImages != null) {
                for (DataSnapshot imageSnapshot : snapshotImages.getChildren()) {
                    Image image = new Image(imageSnapshot);
                    images.add(image);
                }
            }

            DataSnapshot locationsSnapshot = snapshot.child(LOCATIONS);
            if (locationsSnapshot != null) {
                for (DataSnapshot locationSnapshot : locationsSnapshot.getChildren()) {
                    Location location = new Location(locationSnapshot);
                    locations.add(location);
                }
            }

            Iterable<DataSnapshot> tagsSnapshot = snapshot.child(TAGS).getChildren();
            for (Object oTag : tagsSnapshot) {
                if (oTag != null && oTag instanceof String){
                    tags.add((String) oTag);
                }
            }

            Object oTagLine = snapshot.child(TAGLINE).getValue();
            if (oTagLine != null && oTagLine instanceof String) {
                tagLine = (String) oTagLine;
            }

            Object oText = snapshot.child(TEXT).getValue();
            if (oText != null && oText instanceof String) {
                text = (String) oText;
            }

            Object oTitle = snapshot.child(TITLE).getValue();
            if (oTitle != null && oTitle instanceof String) {
                title = (String)  oTitle;
            }

            DataSnapshot videosSnapshot = snapshot.child(VIDEOS);
            if (videosSnapshot != null) {
                for (DataSnapshot videoSnapshot : videosSnapshot.getChildren()){
                    Video video = new Video(videoSnapshot);
                    videos.add(video);
                }
            }

            DataSnapshot linksSnapShot = snapshot.child(LINKS);
            if (linksSnapShot != null) {
                for (DataSnapshot linkSnapshot : linksSnapShot.getChildren()){
                    Link link = new Link(linkSnapshot);
                    links.add(link);
                }
            }



        }
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getIcon() {
        return icon;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTagLine() {
        return tagLine;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public List<Link> getLinks() {
        return links;
    }
}
