package com.example.simon.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
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

public class PlayerListAdapter extends BaseAdapter {

    private List<String> data;

    public PlayerListAdapter(List<String> data){
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    public int getPosition(String item){
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).equals(item)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateReceiptsList(List<String> newList) {

        data.clear();

        System.out.println("Current data in list: " + data);
        System.out.println("New data: "+newList);

        data.addAll(newList);

        System.out.println("New list after inserting data: "+data);

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        } else {
            result = convertView;
        }

        String item = getItem(position);
        ((TextView) result.findViewById(R.id.gameName)).setText("Player: "+getPosition(item));
        ((TextView) result.findViewById(R.id.playerCount)).setText(item);
        ImageView image = (ImageView) result.findViewById(R.id.image_id);
        image.setImageResource(R.drawable.passwordlock);

        return result;
    }
}
