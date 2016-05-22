package ro.madeintm.madeintm.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.util.Linkify;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import ro.madeintm.madeintm.R;
import ro.madeintm.madeintm.model.Image;
import ro.madeintm.madeintm.model.Story;
import ro.madeintm.madeintm.model.Link;

/**
 * Created by validraganescu on 21/05/16.
 */
public class StoryActivity extends AppCompatActivity{

    private Story story;
    private List<Image> images;
    private List<Link> links;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Object oStory = getIntent().getSerializableExtra("object");
        story = null;
        images = null;
        if (oStory != null && oStory instanceof Story) {
            story = (Story) oStory;
            Log.d("Story", story.getTitle());

            //display image / icon
            ImageView imageView = (ImageView)  findViewById(R.id.imageView);

            if (story.getImages().size() > 0){
                images = story.getImages();
                ImageLoader.getInstance().displayImage(images.get(0).getUrl(), imageView);
            }
            else{
                if(story.getIcon() != null)
                    ImageLoader.getInstance().displayImage(story.getIcon(),imageView);
            }

            //display title
            TextView titleView = (TextView) findViewById(R.id.textView);
            if(story.getTitle() != null)
                titleView.setText(story.getTitle());

            //display author
            TextView authorView = (TextView) findViewById(R.id.textView2);
            if(story.getAuthor() != null){
                //add custom text to json info
                String author = "Autor: " + story.getAuthor();
                authorView.setText(author);
            }

            //display text
            TextView textView = (TextView) findViewById(R.id.textView3);
            if (story.getText() != null)
                textView.setText(story.getText());

            //add link
            TextView linkView = (TextView) findViewById(R.id.textView4);
            if (story.getLinks() != null) {
                links = story.getLinks();
                String link = "Sursa " + links.get(0);
                linkView.setText(link);
                Linkify.addLinks(linkView, Linkify.WEB_URLS);
            }

        }


    }
}
