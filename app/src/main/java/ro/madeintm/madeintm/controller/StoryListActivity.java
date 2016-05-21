package ro.madeintm.madeintm.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ro.madeintm.madeintm.R;
import ro.madeintm.madeintm.model.Story;

/**
 * Created by validraganescu on 21/05/16.
 */
public class StoryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        List<Story> stories = new ArrayList<>(NavigationActivity.stories);

        ListView listView = (ListView) findViewById(R.id.listView);
        StoryListAdapter adapter = new StoryListAdapter(getApplicationContext(), stories);
        if (listView != null) {
            listView.setAdapter(adapter);
        }

    }
}
