package com.application.carlosgarro.mygarageapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.application.carlosgarro.mygarageapp.core.worker.BatchScheduler
import com.application.carlosgarro.mygarageapp.ui.theme.MyGarageAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    private lateinit var navHostController : NavHostController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solicitarPermisoNotificacionesSiEsNecesario()
        BatchScheduler.schedule(applicationContext)
        auth = Firebase.auth
        setContent {
            navHostController = rememberNavController()

            MyGarageAppTheme  {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationWrapper(navHostController, auth)
                }
            }
        }
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.i("MainActivity", "Permiso de notificaciones concedido")
            } else {
                Log.i("MainActivity", "Permiso de notificaciones denegado")
            }
        }

    private fun solicitarPermisoNotificacionesSiEsNecesario() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permiso = Manifest.permission.POST_NOTIFICATIONS
            when {
                ContextCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED -> {
                    Log.i("MainActivity", "Permiso de notificaciones ya concedido")
                }

                shouldShowRequestPermissionRationale(permiso) -> {
                    Log.i("MainActivity", "Se debe mostrar una justificación para el permiso de notificaciones")
                    requestNotificationPermissionLauncher.launch(permiso)
                }

                else -> {
                    Log.i("MainActivity", "Solicitando permiso de notificaciones")
                    requestNotificationPermissionLauncher.launch(permiso)
                }
            }
        }
    }

    }
