package com.example.android_mvvm_gitgubapi.resource

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    private var instance : retrofit2.Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()


    fun getInstance() : retrofit2.Retrofit {
        if(instance == null){
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            instance = retrofit2.Retrofit.Builder()
                .baseUrl("https://api.github.com/repos/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return instance!!
    }
}