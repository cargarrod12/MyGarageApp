package com.application.carlosgarro.mygarageapp.data.external.maps.client

import com.application.carlosgarro.mygarageapp.data.external.maps.api.PlacesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlacesService {

    fun create(): PlacesApi {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesApi::class.java)
    }
}

