package com.joe_wise.wagchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    private Context context;
    private ArrayList<ResultsItem> data;
    private ResultsItem result;

    ListViewAdapter(Context context, ArrayList<ResultsItem> arraylist)
    {
        this.context = context;
        data = arraylist;
        result = new ResultsItem();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        LayoutInflater inflater;
        ImageView profile_pic;
        TextView name;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.result_item, parent, false);

        // Get the position
        result = data.get(position);

        //Display profile picture
        profile_pic = itemView.findViewById(R.id.profile_pic);

        //Passes profile picture URL into Picasso to load images.
        //Picasso automatically handles image caching.
        Picasso.get()
                .load(result.getPic_url())
                .placeholder(R.drawable.loading_anim)
                .error(R.drawable.load_failed_sad_dog)
                .into(profile_pic);

        //Allows flags to display over images to let you know where images are being loaded from
        //Red - Network, Blue - Disk, Green - Memory
        //Picasso.get().setIndicatorsEnabled(true);

        //Display name
        name = itemView.findViewById(R.id.name);
        name.setText(result.getName());

        //Display badges
        name = itemView.findViewById(R.id.badges);
        name.setText(result.getBadges());

        return itemView;
    }
}