package com.example.socialx.Retrofit

import com.example.socialx.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ServiceInterface {

    @Headers("Content-Type:application/json")


    @GET("everything")
    fun gettallnews(@Query("q") query: String, @Query("apiKey") apiKey: String): Call<ApiResponse>


}