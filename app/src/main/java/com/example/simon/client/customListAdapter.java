package com.example.simon.client;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by thoma on 3/23/2017.
 */

public class customListAdapter extends BaseAdapter {

    private List<List<String>> data;

    public customListAdapter(List<List<String>> data){
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public List<String> getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        } else {
            result = convertView;
        }

        List<String> item = getItem(position);
        ((TextView) result.findViewById(R.id.gameName)).setText(item.get(0));
        ((TextView) result.findViewById(R.id.playerCount)).setText(item.get(1) + "/" + item.get(2));
        ImageView image = (ImageView) result.findViewById(R.id.image_id);
        if (!item.get(3).equals("")) {
            image.setImageResource(R.drawable.passwordlock);
        }
        else {
            image.setImageResource(R.drawable.startbutton);
        }

        return result;
    }
}
