package com.application.carlosgarro.mygarageapp.data.external.consultasia.client

import com.application.carlosgarro.mygarageapp.BuildConfig
import com.application.carlosgarro.mygarageapp.data.external.consultasia.api.ConsejosApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ConsejosClient {
    fun create(): ConsejosApi {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConsejosApi::class.java)
    }
}

private fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.OPENAI_API_KEY}")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()
}