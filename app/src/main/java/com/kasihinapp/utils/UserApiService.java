package com.kasihinapp.utils;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.google.android.gms.auth.api.Auth;
import com.kasihinapp.model.DonationResponse;
import com.kasihinapp.model.ProfileResponse;
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

    @GET(ApiConfig.GET_USER_PROFILE)
    Call<ProfileResponse> getUserProfile(@Query("user_id") int userId);

    @FormUrlEncoded
    @POST(ApiConfig.DONATE_USER)
    Call<DonationResponse> sendUserDonation(
            @Field("dari_user_id") int dariUserId,
            @Field("ke_user_id") int keUserId,
            @Field("jumlah") int jumlah,
            @Field("pesan") String pesan
    );

}
