package com.localhost.jooyeon.final_project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by asmwj on 2017-06-16.
 */

public class ShowChangeAdapter extends RecyclerView.Adapter<ShowChangeAdapter.ViewHolder> {


    Context context;
    int layout;
    ArrayList<CurrentUser> currentUsers;
    LayoutInflater inflater;
    View view;

    public ShowChangeAdapter(Context c, int layout, ArrayList<CurrentUser> data){
        this.context = c;
        this.layout = layout;
        this.currentUsers = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTv;
        TextView kgTv;
        TextView cntTv;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTv = (TextView)view.findViewById(R.id.showChange_date);
            kgTv = (TextView)view.findViewById(R.id.showChange_kg);
            cntTv = (TextView)view.findViewById(R.id.showChange_walkCnt);
        }
    }

    @Override
    public ShowChangeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(this.layout,parent,false);
        ShowChangeAdapter.ViewHolder viewHolder = new ShowChangeAdapter.ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CurrentUser c = currentUsers.get(position);
        holder.dateTv.setText(c.date);
        holder.kgTv.setText(c.current_kg);
        holder.cntTv.setText(c.walk_cnt);
    }

    @Override
    public int getItemCount() {
        return currentUsers.size();
    }



    /*Context context;
    int layout;
    ArrayList<CurrentUser> currentUsers;
    LayoutInflater inflater;

    public ShowChangeAdapter(Context c, int layout, ArrayList<CurrentUser> data){
        this.context = c;
        this.layout = layout;
        this.currentUsers = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return currentUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return currentUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(layout, null);
        }
        TextView dateTv = (TextView)convertView.findViewById(R.id.showChange_date);
        TextView kgTv = (TextView)convertView.findViewById(R.id.showChange_kg);
        TextView cntTv = (TextView)convertView.findViewById(R.id.showChange_walkCnt);

        CurrentUser c = currentUsers.get(position);
        dateTv.setText(c.date);
        kgTv.setText(c.current_kg);
        cntTv.setText(c.walk_cnt);

        return convertView;
    }*/
}
