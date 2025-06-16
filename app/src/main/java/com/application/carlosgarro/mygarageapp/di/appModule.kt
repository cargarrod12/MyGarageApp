package com.application.carlosgarro.mygarageapp.di

import android.content.Context
import com.application.carlosgarro.mygarageapp.core.worker.Notifier
import com.application.carlosgarro.mygarageapp.data.external.firestore.FirestoreService
import com.application.carlosgarro.mygarageapp.data.local.dao.MantenimientoDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.NotificacionDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.ReglaMantenimientoDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoPersonalDAO
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.getNotificacionesByUserToNotify
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoPersonalByUserUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.saveVehiculoPersonalUseCase
import com.application.carlosgarro.mygarageapp.domain.repository.NotificacionRepository
import com.application.carlosgarro.mygarageapp.domain.repository.VehiculoPersonalRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class appModule {


    @Provides
    @Singleton
    fun provideNotifier(@ApplicationContext context: Context): Notifier {
        return Notifier(context)
    }

    @Provides
    @Singleton
    fun providegetNotificacionesByUserToNotify(repository: NotificacionRepository): getNotificacionesByUserToNotify {
        return getNotificacionesByUserToNotify(repository)
    }

    @Provides
    @Singleton
    fun providegetVehiculoPersonalByUserUseCase(repository: VehiculoPersonalRepository): getVehiculoPersonalByUserUseCase {
        return getVehiculoPersonalByUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideSaveVEhiculoPersonalUseCase(repository: VehiculoPersonalRepository): saveVehiculoPersonalUseCase {
        return saveVehiculoPersonalUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideFirestoreService(
        firestore: FirebaseFirestore,
        vehiculoDAO: VehiculoDAO,
        notificacionDao: NotificacionDAO,
        mantenimientoDao: MantenimientoDAO,
        vehiculoPersonalDao: VehiculoPersonalDAO,
        reglaMantenimientoDAO: ReglaMantenimientoDAO
        ): FirestoreService {
        return FirestoreService(
            firestore = firestore,
            vehiculoPersonalDao = vehiculoPersonalDao,
            vehiculoDao = vehiculoDAO,
            notificacionDao = notificacionDao,
            mantenimientoDao = mantenimientoDao,
            reglaMantenimientoDAO = reglaMantenimientoDAO
        )
    }

}