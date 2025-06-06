package com.application.carlosgarro.mygarageapp.di

import com.application.carlosgarro.mygarageapp.data.local.repository.ConsejoRepositoryImpl
import com.application.carlosgarro.mygarageapp.data.local.repository.MantenimientoRepositoryImpl
import com.application.carlosgarro.mygarageapp.data.local.repository.NotificacionRepositoryImpl
import com.application.carlosgarro.mygarageapp.data.local.repository.ReglaMantenimientoRepositoryImpl
import com.application.carlosgarro.mygarageapp.data.local.repository.VehiculoPersonalRepositoryImpl
import com.application.carlosgarro.mygarageapp.data.local.repository.VehiculoRepositoryImpl
import com.application.carlosgarro.mygarageapp.domain.repository.ConsejoRepository
import com.application.carlosgarro.mygarageapp.domain.repository.MantenimientoRepository
import com.application.carlosgarro.mygarageapp.domain.repository.NotificacionRepository
import com.application.carlosgarro.mygarageapp.domain.repository.ReglaMantenimientoRepository
import com.application.carlosgarro.mygarageapp.domain.repository.VehiculoPersonalRepository
import com.application.carlosgarro.mygarageapp.domain.repository.VehiculoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindVehiculoPersonalRepository(
        vehiculoPersonalRepositoryImpl: VehiculoPersonalRepositoryImpl
    ): VehiculoPersonalRepository

    @Singleton
    @Binds
    abstract fun bindVehiculoRepository(
        vehiculoRepositoryImpl: VehiculoRepositoryImpl
    ): VehiculoRepository

    @Singleton
    @Binds
    abstract fun bindMantenimientoRepository(
        mantenimientoRepositoryImpl: MantenimientoRepositoryImpl
    ): MantenimientoRepository

    @Singleton
    @Binds
    abstract fun bindReglaMantenimientoRepository(
        reglaMantenimientoRepository: ReglaMantenimientoRepositoryImpl
    ): ReglaMantenimientoRepository

    @Singleton
    @Binds
    abstract fun bindNotificacionRepository(
       notificacionRepository: NotificacionRepositoryImpl
    ): NotificacionRepository

    @Singleton
    @Binds
    abstract fun bindConsejoRepository(
        consejoRepository: ConsejoRepositoryImpl
    ): ConsejoRepository
}