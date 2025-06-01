package com.application.carlosgarro.mygarageapp.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel

fun calcularProximosMantenimientos(notificaciones: List<NotificacionModel>, kilometros: Int): List<NotificacionModel> {
    val proximosMantenimientos = mutableListOf<NotificacionModel>()

    for (notificacion in notificaciones) {
        if (notificacion.kilometrosProximoServicio - kilometros < 4000) {
            proximosMantenimientos.add(notificacion)
        }
    }

    return proximosMantenimientos
}

fun checkLocationPermission(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}


@Suppress("DEPRECATION")
fun getApiKeyFromManifest(context: Context): String {
    return try {
        val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        } else {
            context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
        }

        appInfo.metaData?.getString("com.google.android.geo.API_KEY") ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun construirFotoUrl(photoReference: String, apiKey: String): String {
    return "https://maps.googleapis.com/maps/api/place/photo" +
            "?maxwidth=800" +
            "&photo_reference=$photoReference" +
            "&key=$apiKey"
}