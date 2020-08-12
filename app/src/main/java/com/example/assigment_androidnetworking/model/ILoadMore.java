package com.example.assigment_androidnetworking.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ILoadMore {
//    void onLoadMore();
@FormUrlEncoded
@POST("https://www.flickr.com/services/rest")
Call<List<Photo>> GetSearch(@Field("title") String tukhoa);
}
