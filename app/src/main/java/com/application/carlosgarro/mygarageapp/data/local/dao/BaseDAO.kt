package com.application.carlosgarro.mygarageapp.data.local.dao

import androidx.room.Upsert

interface BaseDAO<T> {

    @Upsert
    suspend fun insert(entity: T): Long
}