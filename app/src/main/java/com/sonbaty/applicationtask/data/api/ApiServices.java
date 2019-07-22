package com.sonbaty.applicationtask.data.api;

import com.sonbaty.applicationtask.data.model.getValues.GetValues;
import com.sonbaty.applicationtask.data.model.storeValue.StoreValue;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiServices {

    @POST("value/store")
    @Multipart
    Call<StoreValue> addValueItem(@Part("email") RequestBody email,
                                  @Part("type") RequestBody type,
                                  @Part MultipartBody.Part File);

    @POST("get/all")
    @FormUrlEncoded
    Call<GetValues> getAllValues(@Field("email") String email);

}
