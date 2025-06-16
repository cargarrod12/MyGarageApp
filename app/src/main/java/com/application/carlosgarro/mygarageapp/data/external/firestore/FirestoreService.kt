package com.application.carlosgarro.mygarageapp.data.external.firestore

import android.util.Log
import com.application.carlosgarro.mygarageapp.data.local.dao.BaseDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.MantenimientoDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.NotificacionDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.ReglaMantenimientoDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoDAO
import com.application.carlosgarro.mygarageapp.data.local.dao.VehiculoPersonalDAO
import com.application.carlosgarro.mygarageapp.data.local.entity.BaseEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.DatosGeneralesEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.MantenimientoEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.NotificacionEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.ReglaMantenimientoEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.VehiculoPersonalEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val vehiculoPersonalDao: VehiculoPersonalDAO,
    private val vehiculoDao: VehiculoDAO,
    private val notificacionDao: NotificacionDAO,
    private val mantenimientoDao : MantenimientoDAO,
    private val reglaMantenimientoDAO: ReglaMantenimientoDAO
) {


    fun eliminarDatosUsuario(userId: String){
        firestore.collection("usuario")
            .document(userId)
            .delete()
            .addOnSuccessListener {
                Log.i("FirestoreService", "Datos del usuario eliminados correctamente: $userId")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreService", "Error al eliminar los datos del usuario: ${e.message}")
            }
    }

    suspend fun synDatos(userId: String, email: String) {
        synDatosGenerales()

        synDatosUsuario(userId, email)

        Log.i("SincronizacionDatosWorker", "Sincronización de datos completada para el usuario: $email")
    }

    suspend fun synDatosUsuario(userId: String, email: String){
        Log.d("SincronizacionDatosWorker", "Iniciando sincronización de datos para el usuario: $email")

        Log.i("SincronizacionDatosWorker", "Sincronizando vehículos para el usuario: $email")
        syncVehiculosPersonal(userId, email)
        Log.i("SincronizacionDatosWorker", "Sincronizando notificaciones para el usuario: $email")
        syncNotificaciones(userId, email)
        Log.i("SincronizacionDatosWorker", "Sincronizando mantenimientos para el usuario: $email")
        syncMantenimientos(userId, email)
    }

    suspend fun synDatosGenerales() {
        Log.d("SincronizacionDatosWorker", "Iniciando sincronización de datos generales")

        Log.i("SincronizacionDatosWorker", "Sincronizando vehículos generales")
        syncVehiculos()
        Log.i("SincronizacionDatosWorker", "Sincronizando reglas de mantenimiento generales")
        syncReglasMantenimiento()

        Log.i("SincronizacionDatosWorker", "Sincronización de datos generales completada")
    }


    private suspend fun syncReglasMantenimiento() {
        val reglas = reglaMantenimientoDAO.getAllReglasMantenimiento()
        val reglasMap = reglas.associateBy { it.id }.toMutableMap()

        Log.d("SincronizacionDatosWorker", "Reglas locales: ${reglasMap.keys}")

        val reglasNube = firestore.collection("reglas_mantenimiento")
            .get()
            .await()

        Log.d("SincronizacionDatosWorker", "Reglas en la nube: ${reglasNube.documents.size}")
        if (reglasNube.isEmpty) {
            for (entidad in reglasMap.values) {

                Log.d("SincronizacionDatosWorker", "Insertando entidada ${entidad.javaClass.name.toString()} local: ${entidad.id}")
                firestore.collection("reglas_mantenimiento")
                    .document(entidad.id.toString())
                    .set(entidad)
                    .addOnSuccessListener {
                        Log.d("SincronizacionDatosWorker", "entidada ${entidad.javaClass.name} insertada correctamente: ${entidad.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.e("SincronizacionDatosWorker", "Error al insertar entidada ${entidad.javaClass.name}: ${e.message}")
                    }
                    .await()
            }
        }else{
            compararEntidadesGeneralesConFireStore(
                reglasMap,
                reglasNube,
                reglaMantenimientoDAO,
                ReglaMantenimientoEntity::class.java
            )
        }
    }

    private suspend fun syncVehiculos() {
        val vehiculos = vehiculoDao.getAllVehiculos()
        val vehiculosMap = vehiculos.associateBy { it.id }.toMutableMap()

        Log.d("SincronizacionDatosWorker", "Vehículos locales: ${vehiculosMap.keys}")

        val vehiculosNube = firestore.collection("vehiculos")
            .get()
            .await()

        Log.d("SincronizacionDatosWorker", "Vehículos en la nube: ${vehiculosNube.documents.size}")

        compararEntidadesGeneralesConFireStore(
            vehiculosMap,
            vehiculosNube,
            vehiculoDao,
            VehiculoEntity::class.java

        )


    }


    private suspend fun syncMantenimientos(userId: String, email: String) {
        val mantenimientos = mantenimientoDao.getMantenimientosByUsuario(email)
        val mantenimientosMap = mantenimientos.associateBy { it.id }.toMutableMap()

        Log.d("SincronizacionDatosWorker", "Mantenimientos locales: ${mantenimientosMap.keys}")

        val mantenimientosNube = firestore.collection("usuarios")
            .document(userId)
            .collection("mantenimientos")
            .get()
            .await()

        Log.d("SincronizacionDatosWorker", "Mantenimientos en la nube: ${mantenimientosNube.documents.size}")
        if (mantenimientosNube.isEmpty) {
            insertEntidadLocalToFirestore(mantenimientosMap, userId, "mantenimientos")
        }else{
            compararEntidadesConFireStore(
                userId,
                email,
                mantenimientosMap,
                mantenimientosNube,
                mantenimientoDao,
                "mantenimientos",
                MantenimientoEntity::class.java
            )
            Log.i("SincronizacionDatosWorker", "Mantenimientos restantes en local: ${mantenimientosMap.keys.size}")
            insertEntidadLocalToFirestore(mantenimientosMap, userId, "mantenimientos")
        }

    }


    private suspend fun syncNotificaciones(userId: String, email: String){
        val notificaciones = notificacionDao.getNotificacionesByUser(email)
        val notificacionesMap = notificaciones.associateBy { it.id }.toMutableMap()

        Log.d("SincronizacionDatosWorker", "Notificaciones locales: ${notificacionesMap.keys}")

        val notificacionesNube = firestore.collection("usuarios")
            .document(userId)
            .collection("notificaciones")
            .get()
            .await()

        Log.d("SincronizacionDatosWorker", "Notificaciones en la nube: ${notificacionesNube.documents.size}")

        if (notificacionesNube.isEmpty) {
            insertEntidadLocalToFirestore(notificacionesMap, userId, "notificaciones")
        }else{
           compararEntidadesConFireStore(
                userId,
                email,
                notificacionesMap,
                notificacionesNube,
                notificacionDao,
                "notificaciones",
                NotificacionEntity::class.java
            )

            Log.i("SincronizacionDatosWorker", "Notificaciones restantes en local: ${notificacionesMap.keys.size}")
            insertEntidadLocalToFirestore(notificacionesMap, userId, "notificaciones")
        }


    }



    private suspend fun syncVehiculosPersonal(userId: String, email: String) {
        val vehiculos = vehiculoPersonalDao.getVehiculosPersonalesByUsuario(email, listOf(0, 1)).map { it.vehiculoPersonal }
        val vehiculosMap = vehiculos.associateBy { it.id }.toMutableMap()

        Log.d("SincronizacionDatosWorker", "Vehiculos locales: ${vehiculosMap.keys}")

        val vehiculosNube = firestore.collection("usuarios")
            .document(userId)
            .collection("vehiculos")
            .get()
            .await()
        Log.d("SincronizacionDatosWorker", "Vehiculos en la nube: ${vehiculosNube.documents.size}")


        if (vehiculosNube.isEmpty){
            insertVehiculosLocalToFirestore(vehiculosMap, userId)
        }else{
            compararEntidadesConFireStore(
                userId,
                email,
                vehiculosMap,
                vehiculosNube,
                vehiculoPersonalDao,
                "vehiculos",
                VehiculoPersonalEntity::class.java
            )

            Log.i("SincronizacionDatosWorker", "Vehículos restantes en local: ${vehiculosMap.keys.size}")
            insertVehiculosLocalToFirestore(vehiculosMap, userId)
        }

    }

    private suspend fun insertVehiculosLocalToFirestore(
        vehiculosMap: MutableMap<Long, VehiculoPersonalEntity>,
        userId: String
    ) = coroutineScope {
        val jobs = vehiculosMap.values.map { vehiculo ->
            async {
                if (!vehiculo.borrado) {
                    Log.d("SincronizacionDatosWorker", "===================================================")
                    Log.d("SincronizacionDatosWorker", "Insertando vehículo local: ${vehiculo.id}")
                    Log.d("SincronizacionDatosWorker", "Vehículo a insertar: ${vehiculo.toFirestore()}")

                    try {
                        firestore.collection("usuarios")
                            .document(userId)
                            .collection("vehiculos")
                            .document(vehiculo.id.toString())
                            .set(vehiculo.toFirestore())
                            .await()
                        Log.d("SincronizacionDatosWorker", "Vehiculo insertado correctamente: ${vehiculo.id}")
                    } catch (e: Exception) {
                        Log.e("SincronizacionDatosWorker", "Error al insertar vehiculo: ${e.message}")
                    }
                }
            }
        }
        jobs.awaitAll()
    }





    private suspend fun <E : BaseEntity> compararEntidadesConFireStore(userId: String, email: String, mapaEntidadLocal: MutableMap<Long, E>, listaEntidadNube: QuerySnapshot,
        dao: BaseDAO<E>, nombreColeccion: String, clase: Class<E>
    ) = coroutineScope {
        val mutex = Mutex()
        val jobs = listaEntidadNube.documents.map { doc ->
            async {
                Log.d("SincronizacionDatosWorker", "===================================================")
                Log.d("SincronizacionDatosWorker", "Procesando entidad remota: ${doc.data}")
                val remoteEntidad = doc.toObject(clase) ?: return@async
                Log.d("SincronizacionDatosWorker", "Entidad remota: ${remoteEntidad.id}, fechaUltModificacion: ${remoteEntidad.fechaUltModificacion}")
                val localEntidad = mutex.withLock { mapaEntidadLocal[remoteEntidad.id] }
                when {
                    localEntidad == null -> {
                        Log.d("SincronizacionDatosWorker", "entidad remota no existe localmente: ${remoteEntidad.id}")
                        dao.insert(remoteEntidad)
                        mutex.withLock { mapaEntidadLocal.remove(remoteEntidad.id) }
                    }
                    remoteEntidad.fechaUltModificacion!! > localEntidad.fechaUltModificacion!! -> {
                        Log.d("SincronizacionDatosWorker", "entidad remota más nuevo: ${remoteEntidad.id}")
                        dao.insert(remoteEntidad)
                        mutex.withLock { mapaEntidadLocal.remove(remoteEntidad.id) }
                    }
                    localEntidad.fechaUltModificacion!! > remoteEntidad.fechaUltModificacion!! -> {
                        Log.d("SincronizacionDatosWorker", "entidad local más nueva: ${localEntidad.id}")
                        firestore.collection("usuarios")
                            .document(userId)
                            .collection(nombreColeccion)
                            .document(localEntidad.id.toString())
                            .set(localEntidad.toFirestore())
                            .await()
                        mutex.withLock { mapaEntidadLocal.remove(remoteEntidad.id) }
                    }
                    else -> {
                        Log.d("SincronizacionDatosWorker", "entidad local ya está sincronizado: ${remoteEntidad.id}")
                        mutex.withLock { mapaEntidadLocal.remove(remoteEntidad.id) }
                    }
                }
            }
        }
        jobs.awaitAll()
    }


    private suspend fun <E: DatosGeneralesEntity> compararEntidadesGeneralesConFireStore(
        mapaEntidadLocal: MutableMap<Long, E>,
        listaEntidadNube: QuerySnapshot,
        dao: BaseDAO<E>,
        clase: Class<E>
    ) = coroutineScope {
        val jobs = listaEntidadNube.documents.map { doc ->
            async {
                Log.d("SincronizacionDatosWorker", "===================================================")
                Log.d("SincronizacionDatosWorker", "Procesando entidad remota: ${doc.data}")
                val remoteEntidad = doc.toObject(clase) ?: return@async
                val localEntidad = mapaEntidadLocal[remoteEntidad.id]
                Log.d("SincronizacionDatosWorker", "Entidad remota: $remoteEntidad, entidad local: $localEntidad")

                when {
                    localEntidad == null -> {
                        Log.d("SincronizacionDatosWorker", "entidad remota no existe localmente: ${remoteEntidad.id}")
                        dao.insert(remoteEntidad)
                        mapaEntidadLocal.remove(remoteEntidad.id)
                    }
                    else -> {
                        Log.i("SincronizacionDatosWorker", "entidad local ya está sincronizado: ${remoteEntidad.id}")
                        mapaEntidadLocal.remove(remoteEntidad.id)
                    }
                }
            }
        }
        jobs.awaitAll()
    }

    private suspend fun <E : BaseEntity> insertEntidadLocalToFirestore(
        mapaEntidadLocal: MutableMap<Long, E>,
        userId: String,
        nombreColeccion: String
    ) = coroutineScope {
        val jobs = mapaEntidadLocal.values.map { entidad ->
            async {
                Log.d("SincronizacionDatosWorker", "===================================================")
                Log.d("SincronizacionDatosWorker", "Insertando entidada ${entidad.javaClass.name} local: ${entidad.id}")
                try {
                    firestore.collection("usuarios")
                        .document(userId)
                        .collection(nombreColeccion)
                        .document(entidad.id.toString())
                        .set(entidad)
                        .await()
                    Log.i("SincronizacionDatosWorker", "entidada ${entidad.javaClass.name} insertada correctamente: ${entidad.id}")
                } catch (e: Exception) {
                    Log.e("SincronizacionDatosWorker", "Error al insertar entidada ${entidad.javaClass.name}: ${e.message}")
                }
            }
        }
        jobs.awaitAll()
    }
}