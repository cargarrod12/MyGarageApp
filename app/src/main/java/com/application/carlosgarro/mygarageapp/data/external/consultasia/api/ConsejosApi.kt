package com.application.carlosgarro.mygarageapp.data.external.consultasia.api

import com.application.carlosgarro.mygarageapp.data.external.consultasia.request.ChatGPTRequest
import com.application.carlosgarro.mygarageapp.data.external.consultasia.response.ChatGPTResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ConsejosApi {

    @POST("chat/completions")
    suspend fun enviarPregunta(@Body request: ChatGPTRequest): ChatGPTResponse
}