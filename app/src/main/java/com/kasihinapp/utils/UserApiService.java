package com.kasihinapp.utils;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.google.android.gms.auth.api.Auth;
import com.kasihinapp.model.User;
import com.kasihinapp.model.LoginResponse;
import com.kasihinapp.model.UserResponse;

public interface UserApiService {

    @FormUrlEncoded
    @POST(ApiConfig.LOGIN)
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET(ApiConfig.GET_RECIPIENTS)
    Call<UserResponse> getRecipients();

}
