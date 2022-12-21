package com.example.planesavingpassengers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.planesavingpassengers.Models.PlayerModel;
import com.example.planesavingpassengers.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlayerAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<PlayerModel> playerModelArrayList;

    public PlayerAdapter(Context context, ArrayList<PlayerModel> playerModelArrayList) {
        this.context = context;
//        this.playerModelArrayList = playerModelArrayList;
        setPlayerModelArrayList(sortPlayerModelArrayListByScore(playerModelArrayList));
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

            holder.playerName = (TextView) convertView.findViewById(R.id.listItem_name);
            holder.playerScore = (TextView) convertView.findViewById(R.id.listItem_score);

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

    public ArrayList<PlayerModel> getPlayerModelArrayList() {
        return playerModelArrayList;
    }

    public PlayerAdapter setPlayerModelArrayList(ArrayList<PlayerModel> playerModelArrayList) {
        this.playerModelArrayList = playerModelArrayList;
        return this;
    }

    /**
     * This method sort the list of top 10 players by the score of the players
     *
     * @param list == the list of top 10 players
     * @return == the sorted list of top 10 players
     */
    private ArrayList<PlayerModel> sortPlayerModelArrayListByScore(ArrayList<PlayerModel> list) {
        Collections.sort(list, new Comparator<PlayerModel>() {
            @Override
            public int compare(PlayerModel p1, PlayerModel p2) {
                int score1 = p1.getScore();
                int score2 = p2.getScore();
                if (score1 == score2)
                    return 0;
                else if (score1 < score2)
                    return 1;
                else
                    return -1;
            }
        });
        return list;
    }
}
