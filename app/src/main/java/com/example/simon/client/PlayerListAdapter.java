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

    /**
     * @return the count of the players in the list-
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * @param position
     * @return the player in the position of the param.
     */
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

    /**
     * @param position
     * @return 0...
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Update the list of receipts.
     * @param newList
     */
    public void updateReceiptsList(List<String> newList) {

        data.clear();

        data.addAll(newList);

        this.notifyDataSetChanged();
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return the view of the list.
     */
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

        if(item.equals(DataModel.getNick())) {
            image.setImageResource(R.drawable.bird);
        }
        else {
            image.setImageResource(R.drawable.blackbird);
        }

        return result;
    }
}
