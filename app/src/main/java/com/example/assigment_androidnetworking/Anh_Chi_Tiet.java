package com.example.assigment_androidnetworking;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class Anh_Chi_Tiet extends AppCompatActivity {
    public ViewPager viewPager;
    Image_Adapter viewpagerAdapter;
    public int position;
    List<Photo> photos;
    private static final int PERMISSION_REQUEST_CODE = 1000 ;
    FloatingActionButton fab, fab1, fab2, fab3,fb4;
    TextView tv1, tv2;
    boolean An_Hien = false;
    AlertDialog dialog;
    String link;

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;

    public static  int Select_Image=1;
    ImageView imageView;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Ðã được cho phép", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Quyền bị từ chối", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anh__chi__tiet);
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        tv1 = findViewById(R.id.tv1);
        fab2 = findViewById(R.id.fab2);
        tv2 = findViewById(R.id.tv2);

        fab3 = findViewById(R.id.fab3);
        fb4 = findViewById(R.id.fab4);

        imageView = findViewById(R.id.image_slide);



        viewPager = findViewById(R.id.viewPager);
        final Intent intent = this.getIntent();
        position = intent.getIntExtra("position", 0);
        getData();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

            },PERMISSION_REQUEST_CODE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (An_Hien == false){
                    Show();
                    An_Hien = true;
                } else {
                    Hide();
                    An_Hien = false;
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(Anh_Chi_Tiet.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(Anh_Chi_Tiet.this, "",Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSION_REQUEST_CODE);
                    return;
                } else {
                    dialog = new SpotsDialog.Builder().setContext(Anh_Chi_Tiet.this).build();
                    progressBar = new ProgressDialog(Anh_Chi_Tiet.this);
                    progressBar.setCancelable(true);
                    progressBar.setMessage("File downloading ...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();
                    //reset progress bar and filesize status
                    progressBarStatus = 0;
                    fileSize = 0;

                    new Thread(new Runnable() {
                        public void run() {
                            while (progressBarStatus < 100) {
                                // performing operation
                                progressBarStatus = doOperation();
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // Updating the progress bar
                                progressBarHandler.post(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(progressBarStatus);
                                    }
                                });
                            }
                            if (progressBarStatus >= 100) {
                                // sleeping for 1 second after operation completed
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // close the progress bar dialog
                                progressBar.dismiss();
                            }

                        }
                    }).start();


                // checking how much file is downloaded and updating the filesize
//                    dialog.show();
//                    dialog.setMessage("Downloading...");
                    String fileName = UUID.randomUUID().toString()+".jpg";
                    Picasso.with(getBaseContext())
                            .load(photos.get(viewPager.getCurrentItem()).getUrlL())
                            .into(new SaveImage(getBaseContext(), dialog,
                                    getApplicationContext().getContentResolver(),
                                    fileName, "Image description"));

                }

            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(Anh_Chi_Tiet.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(Anh_Chi_Tiet.this, "",Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSION_REQUEST_CODE);
                    return;
                } else {
                    dialog = new SpotsDialog.Builder().setContext(Anh_Chi_Tiet.this).build();
                    progressBar = new ProgressDialog(Anh_Chi_Tiet.this);
                    progressBar.setCancelable(true);
                    progressBar.setMessage("File downloading ...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();
                    //reset progress bar and filesize status
                    progressBarStatus = 0;
                    fileSize = 0;

                    new Thread(new Runnable() {
                        public void run() {
                            while (progressBarStatus < 100) {
                                // performing operation
                                progressBarStatus = doOperation();
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // Updating the progress bar
                                progressBarHandler.post(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(progressBarStatus);
                                    }
                                });
                            }
                            if (progressBarStatus >= 100) {
                                // sleeping for 1 second after operation completed
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // close the progress bar dialog
                                progressBar.dismiss();
                            }


                        }
                    }).start();
//                    dialog.show();
//                    dialog.setMessage("Downloading...");
                    String fileName2 = UUID.randomUUID().toString()+".png";
                    Picasso.with(getBaseContext()).load(photos.get(viewPager.getCurrentItem()).getUrlL()).into(new SaveImage(getBaseContext(),
                            dialog,
                            getApplicationContext().getContentResolver(),
                            fileName2, "Image description2"));



                }

            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList<Uri> imageUris = new ArrayList<Uri>();
////                imageUris.add(Picasso.with(getBaseContext()).load(photos.get(viewPager.getCurrentItem()).getUrlL()).into();)
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
//                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//                shareIntent.setType("image/jpg");
//                startActivity(Intent.createChooser(shareIntent, "Share images to.."));

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//                intent.setType("image/jpg");
                startActivityForResult(intent,Select_Image);
//                startActivity(Intent.createChooser(intent, "Share images to.."));

            }
        });
        fb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.car);
//                Bitmap bitmap = image_slide

                WallpaperManager wallpaperManager  = WallpaperManager.getInstance(getApplicationContext());
                try {
//                    getData();
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(getApplicationContext(), "Thanh cong",Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }
    public int doOperation() {
        //The range of ProgressDialog starts from 0 to 10000
        while (fileSize <= 10000) {
            fileSize++;
            if (fileSize == 1000) {
                return 10;
            } else if (fileSize == 2000) {
                return 20;
            } else if (fileSize == 3000) {
                return 30;
            } else if (fileSize == 4000) {
                return 40; // you can add more else if
            }

        }//end of while
        return 100;
    }//end of doOperation

    private void getData() {
        final ProgressDialog loading = new ProgressDialog(Anh_Chi_Tiet.this);
        loading.setMessage("Loading...");
        loading.show();

        RequestQueue requestQueue = Volley.newRequestQueue(Anh_Chi_Tiet.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://www.flickr.com/services/rest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                FlickrPhoto flickrPhoto =
                        gson.fromJson(response, FlickrPhoto.class);

                photos = flickrPhoto.getPhotos().getPhoto();

                viewpagerAdapter = new Image_Adapter(Anh_Chi_Tiet.this, photos);
                viewPager.setAdapter(viewpagerAdapter);
                viewPager.setCurrentItem(position, true);
                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                viewpagerAdapter.notifyDataSetChanged();
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Anh_Chi_Tiet.this, error.toString(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // lưu giữ các giá trị theo cặp key/value
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
    }
    private void Hide() {
        fab1.hide();
        fab2.hide();
        fab3.hide();
        fb4.hide();
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
    }

    private void Show() {
        fab1.show();
        fab2.show();
        fab3.show();
        fb4.show();
        tv1.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.VISIBLE);
    }

}