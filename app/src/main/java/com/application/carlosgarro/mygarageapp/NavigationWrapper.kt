package com.application.carlosgarro.mygarageapp

import Historial
import Home
import Initial
import Login
import Notificacion
import SignUp
import Vehiculo
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.application.carlosgarro.mygarageapp.presentation.historial.HistorialScreen
import com.application.carlosgarro.mygarageapp.presentation.home.HomeScreen
import com.application.carlosgarro.mygarageapp.presentation.initial.InitialScreen
import com.application.carlosgarro.mygarageapp.presentation.login.LoginScreen
import com.application.carlosgarro.mygarageapp.presentation.notificaciones.NotificacionesScreen
import com.application.carlosgarro.mygarageapp.presentation.singup.SignUpScreen
import com.application.carlosgarro.mygarageapp.presentation.vehiculo.VehiculoScreen
import com.google.firebase.auth.FirebaseAuth


@Composable
fun NavigationWrapper(navHostController: NavHostController,  auth: FirebaseAuth) {

    NavHost(navController = navHostController, startDestination = Initial) {
        composable<Initial> {
            InitialScreen(
                navigateToLogin = { navHostController.navigate(Login) },
                navigateToSignUp = { navHostController.navigate(SignUp) }
            )
        }
        composable<Login> {
            LoginScreen(
                auth = auth,
                navigateToHome = { navHostController.navigate(Home(name = "")) }
            )
        }
        composable<SignUp> {
            SignUpScreen(
                auth = auth,
                navigateToHome = { navHostController.navigate(Home(name = "")) }
            )
        }

        composable<Home> {
            HomeScreen(
                navigateToVehiculo = { id ->
                    navHostController.navigate(Vehiculo(id = id))
                },
                navigateToHistorial = { idVehiculo: Long, nombreVehiculo: String ->
                    navHostController.navigate(Historial(id = idVehiculo, vehiculo = nombreVehiculo))
                },
            )
        }
        composable<Vehiculo> { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            VehiculoScreen(
                navigateToHome = { navHostController.navigate(Home(name = "")) },
                navigateToHistorial = { idVehiculo: Long, nombreVehiculo: String ->
                    navHostController.navigate(Historial(id = idVehiculo, vehiculo = nombreVehiculo))
                },
                navigateToNotificacion = { idVehiculo: Long, nombreVehiculo: String ->
                    navHostController.navigate(Notificacion(id = idVehiculo, vehiculo = nombreVehiculo))
                },
                id = id,
            )
        }

        composable<Historial> { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            val nombreVehiculo = backStackEntry.arguments?.getString("vehiculo") ?: ""
            HistorialScreen(
                id = id,
                vehiculo = nombreVehiculo,
            )
        }

        composable<Notificacion> {backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            val nombreVehiculo = backStackEntry.arguments?.getString("vehiculo") ?: ""
            NotificacionesScreen(
                vehiculoId = id,
                nombreVehiculo = nombreVehiculo
            )
        }

    }

}


