package com.application.carlosgarro.mygarageapp.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.application.carlosgarro.mygarageapp.ui.theme.Blue

@Composable
fun BottomBar(indice: Int, navigateToHome: () -> Unit, navigateToMapa: () -> Unit) {

    val colorSeleccionado = Color.White
    val colorNoSeleccionado = Color(0x9FCCCCCC)
    NavigationBar(
        containerColor = Blue,
        tonalElevation = 0.dp,
        ) {
        NavigationBarItem(
            selected = indice == 0,
            onClick = {
                navigateToHome()
            },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null,
                    tint = if (indice == 0) colorSeleccionado else colorNoSeleccionado)
                   },
            label = { Text("Resumen", color =if (indice == 0) colorSeleccionado else colorNoSeleccionado) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = indice == 1,
            onClick = {
                navigateToMapa()
            },
            icon = { Icon(Icons.Default.Place, contentDescription = null, tint = if (indice == 1) colorSeleccionado else colorNoSeleccionado) },
            label = { Text("Mapa", color = if (indice == 1) colorSeleccionado else colorNoSeleccionado) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected =  indice == 2,
            onClick = {
//                navigateToConsejo()
            },
            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = if (indice == 2) colorSeleccionado else colorNoSeleccionado)  },
            label = { Text("Consejos", color = if (indice == 2) colorSeleccionado else colorNoSeleccionado) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
    }
}