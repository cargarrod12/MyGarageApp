package com.application.carlosgarro.mygarageapp.data.external.maps.api
//import com.application.carlosgarro.mygarageapp.data.external.maps.response.ListaLugaresResponse
import com.application.carlosgarro.mygarageapp.data.external.maps.response.NearbyPlacesResponse
import com.application.carlosgarro.mygarageapp.data.external.maps.response.PlaceDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyCarRepair(
        @Query("location") location: String,
        @Query("radius") radius: Int = 3000,
        @Query("type") type: String = "car_repair",
        @Query("key") apiKey: String
    ): NearbyPlacesResponse


    @GET("maps/api/place/details/json")
    suspend fun getPlaceReviews(
        @Query("place_id") placeId: String,
        @Query("fields") fields: String = "reviews",
        @Query("language") language: String = "es",
        @Query( "key") apiKey: String
    ): PlaceDetailsResponse
}