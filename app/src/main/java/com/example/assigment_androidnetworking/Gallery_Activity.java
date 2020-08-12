package com.example.assigment_androidnetworking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assigment_androidnetworking.model.gallery.Example;
import com.example.assigment_androidnetworking.model.gallery.Gallery;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gallery_Activity extends AppCompatActivity {
    private GralleryViewAdapter gralleryViewAdapter;
    private RecyclerView recyclerView_Gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_);
        setTitle("Gallery Activity");
        recyclerView_Gallery = findViewById(R.id.Grallery_recyclerview);
        GetData();
    }
    private void GetData() {
        RequestQueue requestQueue = Volley.newRequestQueue(Gallery_Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://www.flickr.com/services/rest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Example example = gson.fromJson(response, Example.class);
                List<Gallery> galleries = example.getGalleries().getGallery();
                gralleryViewAdapter = new GralleryViewAdapter(getApplication(), (ArrayList<Gallery>) galleries,
                        new GralleryViewAdapter.AdapterListenerGallery() {
                            @Override
                            public void OnClick(int position) {
                                Intent intent = new Intent(Gallery_Activity.this,Gallery_ChiTiet.class);
                                intent.putExtra("intent",position);
                                startActivity(intent);
                            }
                        });
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Gallery_Activity.this, LinearLayoutManager.VERTICAL, false);
                recyclerView_Gallery.setLayoutManager(linearLayoutManager);
                recyclerView_Gallery.setAdapter(gralleryViewAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", "1628b2d0ed5fb84aa200450cd084886a");
                params.put("user_id", "186985306@N02");
                params.put("extras", "views,media,path_alias,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o");
                params.put("format", "json");
                params.put("method", "flickr.galleries.getList");
                params.put("nojsoncallback", "1");

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}