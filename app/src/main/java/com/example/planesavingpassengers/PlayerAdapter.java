package com.example.planesavingpassengers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.planesavingpassengers.Models.PlayerModel;

import java.util.ArrayList;

public class PlayerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PlayerModel> playerModelArrayList;

    public PlayerAdapter(Context context, ArrayList<PlayerModel> foodModelArrayList) {
        this.context = context;
        this.playerModelArrayList = foodModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


    @Override
    public int getCount() {
        return playerModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return playerModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null, true);

            holder.playerName = (TextView) convertView.findViewById(R.id.tvProduct);
            holder.playerScore = (TextView) convertView.findViewById(R.id.tvQty);

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.playerName.setText(playerModelArrayList.get(position).getName());
        holder.playerScore.setText(String.valueOf(playerModelArrayList.get(position).getScore()));

        return convertView;
    }

    private class ViewHolder {
        public TextView playerName, playerScore;
    }


}
