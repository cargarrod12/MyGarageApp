package com.application.carlosgarro.mygarageapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.carlosgarro.mygarageapp.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    viewModel: TopBarViewModel = hiltViewModel(),
    navigateToInitial: () -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("MyGarage", color = Color.White) },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        actions = {
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Opciones de cuenta",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        onClick = {
                            expanded = false
                            viewModel.signOut()
                            navigateToInitial()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar cuenta") },
                        onClick = {
                            expanded = false
                            viewModel.deleteUser()
                            navigateToInitial()
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
        )
    )
}