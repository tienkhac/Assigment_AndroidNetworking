package com.example.assigment_androidnetworking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assigment_androidnetworking.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Photo> photoArrayList;
    private List<Photo> photos;
    private AdapterListener adapterListener;

    public SearchViewAdapter(Context context, ArrayList<Photo> photoArrayList,AdapterListener adapterListener, List<Photo> photos) {
        this.context = context;
        this.photoArrayList = photoArrayList;
        this.adapterListener = adapterListener;
        this.photos=photos;
    }
    public interface AdapterListener{
        void OnClick(int position);
    }

    @NonNull
    @Override
    public SearchViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.image_design, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Photo photo = photoArrayList.get(position);
        holder.tvView.setText(photo.getViews());
        Picasso.with(context).load(photo.getUrlL()).into(holder.imgPhoto);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterListener.OnClick(position);
            }
        });

    }
    @Override
    public int getItemCount() {
        return photoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvView;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imageview);
            tvView = itemView.findViewById(R.id.textviewView);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        photos.clear();
        if (charText.length()==0){
            photos.addAll(photoArrayList);
        }
        else {
            for (Photo photo : photoArrayList){
                if (photo.getTitle().toLowerCase(Locale.getDefault()).contains(charText)){
                    photos.add(photo);
                }
            }
        }
        notifyDataSetChanged();
    }
}
