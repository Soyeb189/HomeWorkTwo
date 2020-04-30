package com.example.homeworktwo.ViewModel;

import com.example.homeworktwo.Model.AllUser;
import com.example.homeworktwo.Model.LoginData;
import com.example.homeworktwo.Model.RegStatus;
import com.example.homeworktwo.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("createNew")
    Call<RegStatus> getReg(

            @Field("checked_value") String checked_value,
            @Field("email") String email,
            @Field("image") String image,
            @Field("name") String name,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("uuid") String uuid
    );

    @FormUrlEncoded
    @POST("login")
    Call<LoginData> getLogin(

            @Field("email") String email,
            @Field("password") String password

    );

    @FormUrlEncoded
    @POST("update")
    Call<RegStatus> getImage(

            @Field("id") long id,
            @Field("image") String image

    );

    @FormUrlEncoded
    @POST("update/name")
    Call<RegStatus> getName(

            @Field("id") long id,
            @Field("name") String name

    );

    @FormUrlEncoded
    @POST("update/email")
    Call<RegStatus> getEmail(

            @Field("id") long id,
            @Field("email") String email

    );

    @FormUrlEncoded
    @POST("update/phone")
    Call<RegStatus> getPhone(

            @Field("id") long id,
            @Field("phone") String phone

    );

    @FormUrlEncoded
    @POST("update/password")
    Call<RegStatus> getPassword(

            @Field("id") long id,
            @Field("password") String password

    );

    @FormUrlEncoded
    @POST("update/password")
    Call<LoginData> getCheckedMail(

            @Field("email") String email

    );
    @GET("users")
    Call<List<AllUser>> getUser();

}
