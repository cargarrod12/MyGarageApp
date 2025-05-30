package com.application.carlosgarro.mygarageapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.application.carlosgarro.mygarageapp.core.enums.MarcaVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.ModeloVehiculo
import com.application.carlosgarro.mygarageapp.data.local.Converters

@Entity(tableName = "vehiculo")
data class VehiculoEntity(

    @PrimaryKey val id: Long,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "marca")
    val marca: MarcaVehiculo,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "modelo")
    val modelo: ModeloVehiculo
)