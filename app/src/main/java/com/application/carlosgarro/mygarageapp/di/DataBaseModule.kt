package com.application.carlosgarro.mygarageapp.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.application.carlosgarro.mygarageapp.data.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext appContext: Context):  LocalDataSource{

        //Borrar base de datos
//      appContext.deleteDatabase("my_garage_database")

        return Room.databaseBuilder(
            appContext,
            LocalDataSource::class.java, "my_garage_database"
        )
            .fallbackToDestructiveMigration(false)
//            .createFromAsset("my_garage_database.db")
            .createFromAsset("my_garage_database_datos_generales.db")
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE) // opcional, mejora logs
            .setQueryCallback({ sqlQuery, bindArgs ->
                Log.d("RoomQuery", "SQL Query: $sqlQuery, args: $bindArgs")
            }, Executors.newSingleThreadExecutor())
            .build()
    }

    @Singleton
    @Provides
    fun provideVehiculoPersonalDAO (db: LocalDataSource) = db.vehiculoPersonalDAO()

    @Singleton
    @Provides
    fun provideVehiculoDAO (db: LocalDataSource) = db.vehiculoDAO()

    @Singleton
    @Provides
    fun provideMantenimientoDAO (db: LocalDataSource) = db.mantenimientoDAO()

    @Singleton
    @Provides
    fun provideReglaMantenimientoDAO (db: LocalDataSource) = db.reglaMantenimientoDAO()

    @Singleton
    @Provides
    fun provideNotificacionDAO (db: LocalDataSource) = db.notificacionDAO()

    @Singleton
    @Provides
    fun provideConsejoDAO (db: LocalDataSource) = db.consejoDAO()

    @Singleton
    @Provides
    fun provideUsuarioDAO (db: LocalDataSource) = db.usuarioDAO()


}