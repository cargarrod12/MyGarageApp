package com.application.carlosgarro.mygarageapp.data.external.maps.response

import com.google.gson.annotations.SerializedName

data class NearbyPlacesResponse(
    @SerializedName("results") val results: List<Lugar>
)

data class Lugar(
    @SerializedName("name") val name: String?,
    @SerializedName("vicinity") val vicinity: String?,
    @SerializedName("photos") val photos: List<Photo>?,
    @SerializedName("opening_hours") val openingHours: OpeningHours?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("geometry") val geometry: Geometry?,
    @SerializedName("place_id") val placeId: String

)

data class Photo(
    @SerializedName("photo_reference") val photoReference: String
)

data class OpeningHours(
    @SerializedName("open_now") val openNow: Boolean
)

data class Geometry(
    @SerializedName("location") val location: LatLngResult
)

data class LatLngResult(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)

data class PlaceDetailsResponse(
    @SerializedName("result") val result: ReviewsListResponse
)

data class ReviewsListResponse(
    @SerializedName("reviews") val reviews: List<ReviewsResponse>?
)

data class ReviewsResponse(
    @SerializedName("author_name") val authorName: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("text") val text: String,
    @SerializedName("relative_time_description") val time: String
)