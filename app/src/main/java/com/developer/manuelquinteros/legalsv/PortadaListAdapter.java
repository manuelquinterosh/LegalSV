package com.developer.manuelquinteros.legalsv;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PortadaListAdapter extends ArrayAdapter<Portada> {

    private Activity context;
    private int resource;
    private List<Portada> listPortada;

    public PortadaListAdapter(Activity context, int resource, List<Portada> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listPortada = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);

        TextView tvTitle = (TextView) v.findViewById(R.id.tvImagePortada);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);

        tvTitle.setText(listPortada.get(position).getTitle());

        Glide.with(context)
                .load(listPortada.get(position).getUrl()).into(img);

        return v;
    }
}