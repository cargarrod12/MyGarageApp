package com.application.carlosgarro.mygarageapp.presentation.notificaciones

import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.presentation.components.BottomBar
import com.application.carlosgarro.mygarageapp.presentation.components.TopBar
import com.application.carlosgarro.mygarageapp.ui.theme.Blue

@Composable
fun NotificacionesScreen(
    vehiculoId: Long,
    nombreVehiculo: String,
    viewModel: NotificacionesViewModel = hiltViewModel(),
    navigateToHome: () -> Unit = {},
    navigateToMapa: () -> Unit = {},
) {
    val notificaciones = viewModel.notificaciones.observeAsState(emptyList()).value

    LaunchedEffect(vehiculoId) {
        Log.i("ID", "ID: $vehiculoId")
        viewModel.setVehiculoId(vehiculoId)
    }

    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.eventoMensaje.collect { mensaje ->
            val toast =  Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 200)
            toast.show()
        }
    }

    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(0,navigateToHome, navigateToMapa)
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, top = 25.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Icono de notificaciones",
                    modifier = Modifier.padding(end = 8.dp),
                    tint  = Blue
                )
                Text(
                    text = "Notificaciones",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
            }
            Text(
                modifier = Modifier.padding(20.dp, top = 15.dp),
                text = nombreVehiculo,
                style = MaterialTheme.typography.titleMedium,
            )

            TablaNotificaciones(notificaciones, viewModel)


        }
    }
}

@Composable
fun TablaNotificaciones(notificaciones: List<NotificacionModel>, viewModel: NotificacionesViewModel){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LazyColumn {
            items(notificaciones) { notificacion ->
                var isChecked by remember { mutableStateOf(notificacion.activo) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = notificacion.tipoServicio.toString())
                    Switch(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            notificacion.activo = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green,
                            uncheckedThumbColor = Color.Red
                        )
                    )
                }
            }
            if (notificaciones.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay notificaciones disponibles")
                    }
                }
            }else{
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Button(
                            onClick = {
                                viewModel.updateNotificaciones(notificaciones)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Blue),
                        ) {
                            Icon(
                                imageVector = Icons.Default.SaveAlt,
                                contentDescription = "Guardar",
                                tint = Color.White
                            )
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}

