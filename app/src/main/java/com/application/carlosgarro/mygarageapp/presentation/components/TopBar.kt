package com.application.carlosgarro.mygarageapp.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.application.carlosgarro.mygarageapp.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(){
    TopAppBar(
        title = { Text("MyGararge", color = Color.White) },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        colors = TopAppBarColors(
            containerColor = Blue,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            scrolledContainerColor = Color.White,
        )
    )
}