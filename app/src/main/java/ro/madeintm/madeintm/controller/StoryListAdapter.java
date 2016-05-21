package ro.madeintm.madeintm.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import ro.madeintm.madeintm.R;
import ro.madeintm.madeintm.model.Story;

/**
 * Created by validraganescu on 21/05/16.
 */
public class StoryListAdapter extends BaseAdapter {

    private List<Story> stories = new ArrayList<>();
    private Context context;

    public StoryListAdapter(Context context, List<Story> stories) {
        this.stories = stories;
        this.context = context;
    }

    public void update(List<Story> stories){
        this.stories.clear();
        this.stories.addAll(stories);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stories.size();
    }

    @Override
    public Object getItem(int position) {
        return stories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoryItemViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.story_item_row, null);
            viewHolder = new StoryItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (StoryItemViewHolder) convertView.getTag();
        }
        Story story = stories.get(position);
        if (story.getImages().size() > 0){
            String imageUrl = story.getIcon();
            ImageLoader.getInstance().displayImage(imageUrl, viewHolder.image);
        }
        if (story.getTitle() != null) {
            viewHolder.title.setText(story.getTitle());
        }

        return convertView;
    }

    class StoryItemViewHolder {
        public TextView title;
        public ImageView image;

        public StoryItemViewHolder(View base) {
            title = (TextView) base.findViewById(R.id.title);
            image = (ImageView) base.findViewById(R.id.image);
        }
    }
}
