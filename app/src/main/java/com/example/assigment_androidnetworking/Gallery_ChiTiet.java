package com.example.assigment_androidnetworking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assigment_androidnetworking.model.FlickrPhoto;
import com.example.assigment_androidnetworking.model.Photo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gallery_ChiTiet extends AppCompatActivity {
    private ImageViewAdapter imageViewAdapter;
    private RecyclerView recyclerViewImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int NUM_COLUMNS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery__chi_tiet);
        setTitle("Gallery chi tiết");
        recyclerViewImage = findViewById(R.id.recyclerview_gallery_ct);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_Gallery);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        GetData();
    }
    private void GetData() {
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue requestQueue = Volley.newRequestQueue(Gallery_ChiTiet.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://www.flickr.com/services/rest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                FlickrPhoto flickrPhoto = gson.fromJson(response, FlickrPhoto.class);
                List<Photo> photos = flickrPhoto.getPhotos().getPhoto();
                imageViewAdapter = new ImageViewAdapter(getApplication(), (ArrayList<Photo>) photos,
                        new ImageViewAdapter.AdapterListener() {
                            @Override
                            public void OnClick(int position) {
                                Intent intent = new Intent(Gallery_ChiTiet.this,Anh_Chi_Tiet.class);
                                intent.putExtra("intent",position);
                                startActivity(intent);
                            }
                        });
                StaggeredGridLayoutManager staggeredGridLayoutManager = new
                        StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
                recyclerViewImage.setLayoutManager(staggeredGridLayoutManager);
                recyclerViewImage.setAdapter(imageViewAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(Gallery_ChiTiet.this, error.toString(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", "1628b2d0ed5fb84aa200450cd084886a");
                params.put("gallery_id", "72157715371882547");
                params.put("extras", "views,media,path_alias,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o");
                params.put("format", "json");
                params.put("method", "flickr.galleries.getPhotos");
                params.put("nojsoncallback", "1");

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

//    public void SharingToSocialMedia(String application) {
//
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
//
//        boolean installed = checkAppInstall(application);
//        if (installed) {
//            intent.setPackage(application);
//            startActivity(intent);
//        } else {
//            Toast.makeText(getApplicationContext(),
//                    "Installed application first", Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//
//    private boolean checkAppInstall(String uri) {
//        PackageManager pm = getPackageManager();
//        try {
//            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//        }
//
//        return false;
//    }
}