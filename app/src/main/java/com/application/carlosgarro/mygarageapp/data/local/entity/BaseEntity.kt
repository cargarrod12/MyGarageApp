package com.application.carlosgarro.mygarageapp.data.local.entity

interface BaseEntity {
    val id: Long
    val fechaUltModificacion: String?
    fun toFirestore(): Map<String, Any?>
}
