package com.dicoding.githubuserapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiConfig {
    companion object{
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

//            With Github Token
//            val token = "" // add your github token
//            val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
//                val newRequest: Request = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer $token")
//                    .build()
//                chain.proceed(newRequest)
//            }).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}