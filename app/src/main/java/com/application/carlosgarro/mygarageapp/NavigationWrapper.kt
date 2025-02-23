package com.application.carlosgarro.mygarageapp

import Home
import Initial
import Login
import SignUp
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.application.carlosgarro.mygarageapp.presentation.home.HomeScreen
import com.application.carlosgarro.mygarageapp.presentation.initial.InitialScreen
import com.application.carlosgarro.mygarageapp.presentation.login.LoginScreen
import com.application.carlosgarro.mygarageapp.presentation.singup.SignUpScreen
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
            HomeScreen()
        }

    }

}


