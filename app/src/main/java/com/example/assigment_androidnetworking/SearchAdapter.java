package com.example.assigment_androidnetworking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assigment_androidnetworking.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    public Context context;
    public RecyclerView recyclerView;
    public List<Photo> getList;

    public SearchAdapter(Context context, RecyclerView recyclerView, List<Photo> getList) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.getList = getList;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        final Photo photoget = getList.get(position);
        Picasso.with(context).load(photoget.getUrlL()).into(holder.imageView);

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                adapterListener.OnClick(position);
//            }
//        });
//        holder.title.setText(get.getTitle()+"");
//        androidHolder.date.setText(get.getDate()+"");
//        androidHolder.views.setText(get.getViews()+"");
//
//        androidHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, Showdetail.class);
//                intent.putExtra("link",get.Url);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return getList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageview);
        }
    }
}
