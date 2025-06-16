package com.application.carlosgarro.mygarageapp.data.local

import androidx.room.TypeConverter
import com.application.carlosgarro.mygarageapp.core.enums.EstadoVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.MarcaVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.ModeloVehiculo
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import java.time.LocalDate

class Converters {

    /// Convertidor para EstadoVehiculo
    @TypeConverter
    fun fromEstadoVehiculo(value: EstadoVehiculo): String {
        return value.name
    }

    @TypeConverter
    fun toEstadoVehiculo(value: String): EstadoVehiculo {
        return EstadoVehiculo.valueOf(value)
    }

    /// Convertidor para ModeloVehiculo
    @TypeConverter
    fun toModeloVehiculo(value: String): ModeloVehiculo{
        return ModeloVehiculo.valueOf(value)
    }

    @TypeConverter
    fun fromModeloVehiculo(value: ModeloVehiculo): String {
        return value.name
    }


    /// Convertidor para MarcaVehiculo
    @TypeConverter
    fun toMarcaVehiculo(value: String): MarcaVehiculo{
        return MarcaVehiculo.valueOf(value)
    }

    @TypeConverter
    fun fromMarcaVehiculo(value: MarcaVehiculo): String {
        return value.name
    }

    /// Convertidor para TipoServicio
    @TypeConverter
    fun toTipoServicio(value: String): TipoServicio{
        return TipoServicio.valueOf(value)
    }

    @TypeConverter
    fun fromTipoServicio(value: TipoServicio): String {
        return value.name
    }

    /// Convertidor para LocalDate
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }
}