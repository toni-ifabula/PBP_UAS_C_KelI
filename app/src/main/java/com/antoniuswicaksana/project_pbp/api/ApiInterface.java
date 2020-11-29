package com.antoniuswicaksana.project_pbp.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("user")
    Call<com.antoniuswicaksana.project_pbp.api.UserResponse> getAllUser(@Query("data") String data);

    @GET("user/{id}")
    Call<com.antoniuswicaksana.project_pbp.api.UserResponse> getUserById(@Path("id") String id,
                                                          @Query("data") String data);

    @POST("user")
    @FormUrlEncoded
    Call<com.antoniuswicaksana.project_pbp.api.UserResponse> createUser(@Field("email") String email,
                                                         @Field("password") String password,
                                                         @Field("nama") String nama,
                                                         @Field("alamat") String alamat,
                                                         @Field("noTelp") String noTelp,
                                                         @Field("image") String image);

    @POST("login")
    @FormUrlEncoded
    Call<com.antoniuswicaksana.project_pbp.api.UserResponse> loginRequest(@Field("nim") String nim,
                                                           @Field("password") String password);

    @PUT("user/{id}")
    @FormUrlEncoded
    Call<com.antoniuswicaksana.project_pbp.api.UserResponse> updateUser(@Path("id") String id,
                                                                        @Query("data") String data,
                                                                        @Field("email") String email,
                                                                        @Field("password") String password,
                                                                        @Field("nama") String nama,
                                                                        @Field("alamat") String alamat,
                                                                        @Field("noTelp") String noTelp,
                                                                        @Field("image") String image);

    @POST("user/delete/{id}")
    Call<com.antoniuswicaksana.project_pbp.api.UserResponse> deleteUser(@Path("id") String id);
}
