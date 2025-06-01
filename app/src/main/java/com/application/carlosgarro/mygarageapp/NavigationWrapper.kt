package com.application.carlosgarro.mygarageapp

import EditarVehiculo
import Historial
import Home
import Initial
import Login
import Mapa
import Notificacion
import SignUp
import Vehiculo
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.application.carlosgarro.mygarageapp.presentation.editavehiculo.EditarVehiculoScreen
import com.application.carlosgarro.mygarageapp.presentation.historial.HistorialScreen
import com.application.carlosgarro.mygarageapp.presentation.home.HomeScreen
import com.application.carlosgarro.mygarageapp.presentation.initial.InitialScreen
import com.application.carlosgarro.mygarageapp.presentation.login.LoginScreen
import com.application.carlosgarro.mygarageapp.presentation.mapa.MapaScreen
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
                navigateToHome = { navHostController.navigate(Home(name = "")) },
                navigateToMapa = { navHostController.navigate(Mapa) },
                navigateToEditarVehiculo = { id ->
                    navHostController.navigate(EditarVehiculo(id = id))
                }
            )
        }
        composable<Vehiculo> { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            VehiculoScreen(
                id = id,
                navigateToHome = { navHostController.navigate(Home(name = "")) },
                navigateToHistorial = { idVehiculo: Long, nombreVehiculo: String ->
                    navHostController.navigate(Historial(id = idVehiculo, vehiculo = nombreVehiculo))
                },
                navigateToNotificacion = { idVehiculo: Long, nombreVehiculo: String ->
                    navHostController.navigate(Notificacion(id = idVehiculo, vehiculo = nombreVehiculo))
                },
                navigateToMapa = { navHostController.navigate(Mapa) },
                navigateToEditarVehiculo = { id ->
                    navHostController.navigate(EditarVehiculo(id = id))
                }
            )
        }

        composable<Historial> { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            val nombreVehiculo = backStackEntry.arguments?.getString("vehiculo") ?: ""
            HistorialScreen(
                id = id,
                vehiculo = nombreVehiculo,
                navigateToHome = { navHostController.navigate(Home(name = ""))},
                navigateToMapa = { navHostController.navigate(Mapa) },
            )
        }

        composable<Notificacion> {backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            val nombreVehiculo = backStackEntry.arguments?.getString("vehiculo") ?: ""
            NotificacionesScreen(
                vehiculoId = id,
                nombreVehiculo = nombreVehiculo,
                navigateToHome = { navHostController.navigate(Home(name = ""))},
                navigateToMapa = { navHostController.navigate(Mapa) },
            )
        }

        composable<Mapa> {
             MapaScreen(
                navigateToHome = { navHostController.navigate(Home(name = "")) },
                 navigateToMapa = {}
             )
        }

        composable<EditarVehiculo> {backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            EditarVehiculoScreen(
                navigateToHome = { navHostController.navigate(Home(name = "")) },
                navigateToMapa = { navHostController.navigate(Mapa) },
                navigateToVehiculo = {navHostController.navigate(Vehiculo(id = id))
                },
                vehiculoId = id,
            )
        }

    }

}


