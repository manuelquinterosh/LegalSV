package com.developer.manuelquinteros.legalsv.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.manuelquinteros.legalsv.CustomItemClickListener;
import com.developer.manuelquinteros.legalsv.Model.Menu;
import com.developer.manuelquinteros.legalsv.R;

import java.util.List;

public class CodesRecyclerViewAdapter extends RecyclerView.Adapter<CodesRecyclerViewAdapter.MyViewHolder>{

    private Context mContext;
    private List<Menu> mItems;
    private CustomItemClickListener mClickListener;

    public CodesRecyclerViewAdapter(Context context, List<Menu> items, CustomItemClickListener clickListener) {
        mContext = context;
        mItems = items;
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_item, viewGroup, false);
        final CodesRecyclerViewAdapter.MyViewHolder holder = new CodesRecyclerViewAdapter.MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(view, holder.getPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(mItems.get(i).getTitle());
        myViewHolder.subtitle.setText(mItems.get(i).getSubtitle());


        Glide.with(mContext)
                .asBitmap()
                .load(mItems.get(i).getUrl())
                .into(myViewHolder.url_img);

        myViewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(myViewHolder.overflow);
            }
        });
    }

    private void showPopupMenu(View view) {
        //Inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_constitutions, popup.getMenu());
        popup.setOnMenuItemClickListener(new CodesRecyclerViewAdapter.MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_agregar_favoritos:
                    Toast.makeText(mContext, "Agregar a Favoritos", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_ver_descripcion:
                    Toast.makeText(mContext, "Ver Descripcion", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, subtitle;
        ImageView url_img, overflow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            overflow = itemView.findViewById(R.id.overflow);
            url_img = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.titleCard);
            subtitle = itemView.findViewById(R.id.subtitleCard);

            itemView.setClickable(true);
        }
    }
}
