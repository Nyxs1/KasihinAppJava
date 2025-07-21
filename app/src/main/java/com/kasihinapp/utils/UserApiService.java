package com.kasihinapp.utils;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.kasihinapp.models.User;

public interface UserApiService {

    @FormUrlEncoded
    @POST("?endpoint=login")
    Call<User> login(
            @Field("email") String email,
            @Field("password") String password
    );

}
