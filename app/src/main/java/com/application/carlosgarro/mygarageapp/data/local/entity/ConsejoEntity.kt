package com.application.carlosgarro.mygarageapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.Converters


@Entity(tableName = "consejo")
data class ConsejoEntity (

    @PrimaryKey
    @TypeConverters(Converters::class)
    val tipoServicio: TipoServicio,
    val consejo: String,
) {




}