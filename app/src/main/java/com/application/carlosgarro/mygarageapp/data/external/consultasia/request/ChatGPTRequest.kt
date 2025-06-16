package com.application.carlosgarro.mygarageapp.data.external.consultasia.request

data class ChatGPTRequest(val model: String = "gpt-3.5-turbo", val messages: List<Message>)
