package com.example.assigment_androidnetworking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assigment_androidnetworking.model.gallery.Gallery;

import java.util.ArrayList;

public class GralleryViewAdapter extends RecyclerView.Adapter<GralleryViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Gallery> GalleryArrayList;
    private AdapterListenerGallery adapterListenerGallery;

    public GralleryViewAdapter(Context context, ArrayList<Gallery> galleryArrayList, AdapterListenerGallery adapterListenerGallery) {
        this.context = context;
        GalleryArrayList = galleryArrayList;
        this.adapterListenerGallery = adapterListenerGallery;
    }

    public interface AdapterListenerGallery{
        void OnClick(int position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gallery_design, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Gallery gallery = GalleryArrayList.get(position);
        holder.tv_gallery.setText(gallery.getDescription().getContent());
        holder.cardView_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterListenerGallery.OnClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return GalleryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_gallery;
        private CardView cardView_gallery;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_gallery = itemView.findViewById(R.id.tv_Gallery);
            cardView_gallery = itemView.findViewById(R.id.cardview_gallery);
        }
    }
}
