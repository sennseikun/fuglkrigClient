package com.example.simon.client;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by thoma on 3/23/2017.
 */

public class customListAdapter extends BaseAdapter {

    private List<Lobby> data;

    public customListAdapter(List<Lobby> data){
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Lobby getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateReceiptsList(List<Lobby> newList) {

        data.clear();
        data.addAll(newList);

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


        Lobby item = getItem(position);
        ((TextView) result.findViewById(R.id.gameName)).setText(item.getName());
        ((TextView) result.findViewById(R.id.playerCount)).setText(item.getPlayerCount() + "/" + item.getMaxPlayerCount());
        ImageView image = (ImageView) result.findViewById(R.id.image_id);
        if (!item.getPassword().equals("")) {
            image.setImageResource(R.drawable.passwordlock);
        }
        else {
            image.setImageResource(R.drawable.openpasswordlock);
        }

        return result;
    }
}
