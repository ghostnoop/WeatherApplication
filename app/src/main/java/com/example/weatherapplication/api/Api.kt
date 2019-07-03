package com.example.kotlinretro.api

import com.example.weatherapplication.models.Citynames
import com.example.weatherapplication.models.DefaultResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import retrofit2.http.GET
import java.net.URL
import retrofit2.http.PUT
import retrofit2.http.FormUrlEncoded




interface Api {

    @FormUrlEncoded
    @POST("createcity")
    fun createCity(
        @Field("cityname") cityname:String
    ): Call<DefaultResponse>




    @FormUrlEncoded
    @POST("createads")
    fun createAds(
        @Field("img") img: String,
        @Field("name") name: String,
        @Field("days") days: String,
        @Field("phont") phont: String
    ):Call<DefaultResponse>


    @FormUrlEncoded
    @PUT("updateadver/{id}")
    fun updateAdver(
        @Path("id") id: Int,
        @Field("img") img: String,
        @Field("name") name: String,
        @Field("days") days: String,
        @Field("phont") phont: String
    ): Call<DefaultResponse>



//
    @GET("getcitys")
    fun getCitys(): Call<List<Citynames>>


    @DELETE("deletecity/{cityname}")
    fun deleteCity(
        @Path("cityname") cityname: String
    ):Call<DefaultResponse>




}