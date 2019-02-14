package com.developer.manuelquinteros.legalsv.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.manuelquinteros.legalsv.Model.Menu;
import com.developer.manuelquinteros.legalsv.R;
import com.developer.manuelquinteros.legalsv.books.CodesActivity;
import com.developer.manuelquinteros.legalsv.books.ConstitutionsActivity;
import com.developer.manuelquinteros.legalsv.books.LawsActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.ViewHolder> {

    private List<Menu> listPortada;
    private Context mContext;

    public MenuRecyclerViewAdapter(List<Menu> object, Context context) {
        listPortada = object;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_menu, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Menu menu = listPortada.get(position);

        Glide.with(mContext)
                .asBitmap()
                .load(menu.getUrl())
                .into(holder.imageView);

        holder.imageName.setText(menu.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        Intent intentConstitutions = new Intent(mContext, ConstitutionsActivity.class);
                        mContext.startActivity(intentConstitutions);
                        break;
                    case 1:
                        Intent intentCodes = new Intent(mContext, CodesActivity.class);
                        mContext.startActivity(intentCodes);
                        break;
                    case 2:
                        Intent intentLaws = new Intent(mContext, LawsActivity.class);
                        mContext.startActivity(intentLaws);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPortada.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.item_menu);
        }
    }
}
