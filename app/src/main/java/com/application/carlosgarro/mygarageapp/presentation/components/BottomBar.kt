package com.application.carlosgarro.mygarageapp.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.application.carlosgarro.mygarageapp.ui.theme.Blue

@Composable
fun BottomBar(){
    NavigationBar(containerColor = Blue) {
        NavigationBarItem(
            selected = true,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.White) },
            label = { Text("Resumen", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Place, contentDescription = null, tint = Color.White) },
            label = { Text("Mapa", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* TODO */ },
            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = Color.White) },
            label = { Text("Consejos", color = Color.White) }
        )
    }
}