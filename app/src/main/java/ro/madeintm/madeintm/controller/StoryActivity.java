package ro.madeintm.madeintm.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ro.madeintm.madeintm.R;
import ro.madeintm.madeintm.model.Story;

/**
 * Created by validraganescu on 21/05/16.
 */
public class StoryActivity extends AppCompatActivity{

    private Story story;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Object oStory = getIntent().getSerializableExtra("object");
        story = null;
        if (oStory != null && oStory instanceof Story) {
            story = (Story) oStory;
            Log.d("Story", story.getTitle());
        }


    }
}
