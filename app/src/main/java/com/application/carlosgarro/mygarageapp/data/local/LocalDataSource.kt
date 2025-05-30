package com.application.carlosgarro.mygarageapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.application.carlosgarro.mygarageapp.data.local.dao.MantenimientoDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.NotificacionDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.ReglaMantenimientoDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoPersonalDAO
import com.application.carlosgarro.mygarageapp.data.local.entity.MantenimientoEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.NotificacionEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.ReglaMantenimientoEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.UsuarioEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoPersonalEntity

@Database(
    entities = [
        VehiculoPersonalEntity::class,
        VehiculoEntity::class,
        UsuarioEntity::class,
        MantenimientoEntity::class,
        NotificacionEntity::class,
        ReglaMantenimientoEntity::class
               ],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract  class LocalDataSource : RoomDatabase() {

    abstract fun vehiculoPersonalDAO(): VehiculoPersonalDAO

    abstract fun vehiculoDAO(): VehiculoDAO

    abstract fun mantenimientoDAO(): MantenimientoDAO

    abstract fun reglaMantenimientoDAO(): ReglaMantenimientoDAO

    abstract fun notificacionDAO(): NotificacionDAO

}