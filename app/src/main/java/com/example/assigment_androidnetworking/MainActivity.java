package com.example.assigment_androidnetworking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
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

public class MainActivity extends AppCompatActivity {
    private ImageViewAdapter imageViewAdapter;
    private RecyclerView recyclerViewImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int NUM_COLUMNS = 2;
    String text="";
    String method="flickr.favorites.getList";
    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewImage = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //swipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GetData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        GetData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem Gallery_Item = menu.findItem(R.id.item_gallery);
        Gallery_Item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, Gallery_Activity.class);
                startActivity(intent);
                return true;
            }
        });

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();
                recent_search("flickr.favorites.getList",s);
                GetData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){

                }

                return false;
            }
        });


        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(MainActivity.this, "Type your search item.", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                recent_search("flickr.favorites.getList","");
                GetData();
                Toast.makeText(MainActivity.this, "Back to Recent images...", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return true;
    }
    public void recent_search(String method1, String text1)
    {
        method=method1;
        text=text1;
    }
    private void GetData() {
//        EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
//            @Override
//            protected void onLoadMore(int currentPage, int totalItemCount, RecyclerView view) {
                swipeRefreshLayout.setRefreshing(true);
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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
                                        Intent intent = new Intent(MainActivity.this, Anh_Chi_Tiet.class);
                                        intent.putExtra("position", position);
                                        startActivity(intent);
                                    }
                                });
//                        staggeredGridLayoutManager = new
//                                StaggeredGridLayoutManager();
                        recyclerViewImage.setLayoutManager(staggeredGridLayoutManager);
                        recyclerViewImage.setAdapter(imageViewAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("api_key", "1628b2d0ed5fb84aa200450cd084886a");
                        params.put("user_id", "186985306@N02");
                        params.put("extras", "views,media,path_alias,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o");
                        params.put("format", "json");
                        params.put("method", "flickr.favorites.getList");
                        params.put("nojsoncallback", "1");

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
//            }
//
//        };
//        recyclerViewImage.addOnScrollListener(endlessRecyclerViewScrollListener);
    }
}