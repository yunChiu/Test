package com.example.instagram;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.ViewHolder> {
    ArrayList<String> photoList = new ArrayList<>();

    PagerAdapter(ArrayList<String> photoList) {
        this.photoList = photoList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_photo;

        ViewHolder(View item){
            super(item);
            img_photo = item.findViewById(R.id.img_photo);
        }
    }

    @NonNull
    @Override
    public PagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_photo, parent, false);
        return new PagerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagerAdapter.ViewHolder holder, int position) {
        //Glide.with(MainActivity.this).load(photoList.get(position)).into(holder.img_photo);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }
}
