package com.application.carlosgarro.mygarageapp.presentation.vehiculo


import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.carlosgarro.mygarageapp.core.calcularProximosMantenimientos
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.presentation.components.BottomBar
import com.application.carlosgarro.mygarageapp.presentation.components.TopBar
import com.application.carlosgarro.mygarageapp.ui.theme.Blue


@Composable
fun VehiculoScreen(
    id: Long,
    viewModel: VehiculoViewModel = hiltViewModel(),
    navigateToHome: () -> Unit = {},
    navigateToHistorial: (Long, String) -> Unit,
    navigateToNotificacion: (Long, String) -> Unit,
    navigateToMapa: () -> Unit = {},
    navigateToEditarVehiculo: (Long) -> Unit = {}
                   ) {

    val isLoading by viewModel.isLoading.observeAsState(true)
    val vehiculo by viewModel.vehiculo.observeAsState(VehiculoPersonalModel())

    LaunchedEffect(id) {
        Log.i("ID", "ID: $id")
        viewModel.setVehiculoId(id)
    }

    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(0,navigateToHome, navigateToMapa)

        },
    ) { padding ->

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                color = Color.Blue
            )
        } else {
            Column(
                modifier = Modifier.padding(padding)
            ) {


                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    // Header Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.CenterStart) // Contenido principal
                        ) {
                            if (vehiculo.imagen != null) {
                                val bitmap = BitmapFactory.decodeByteArray(vehiculo.imagen, 0, vehiculo.imagen!!.size)
                                bitmap?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "Imagen seleccionada",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = "Imagen coche",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(text = vehiculo.modelo.toString(), fontWeight = FontWeight.Bold)
                                Text(text = "Estado: ${vehiculo.estado}", fontWeight = FontWeight.Bold)
                                Text(text = "Año: ${vehiculo.anyo}")
                                Text(text = "KMs: ${vehiculo.kilometros}")
                            }
                        }

                        // Ícono de edición en esquina superior derecha
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar Vehiculo",
                            tint = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(24.dp)
                                .clickable {
                                    navigateToEditarVehiculo(vehiculo.id!!)
                                    Log.d("Icono", "Icono clickeado, editar vehiculo con ID: ${vehiculo.id}")
                                }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                navigateToNotificacion(vehiculo.id!!, vehiculo.modelo.toString())
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Blue),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Campana",
                                tint = Color.White // o el color que quieras
                            )
                            Text("Notificaciones")
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Button(
                            onClick = {
                                navigateToHistorial(vehiculo.id!!, vehiculo.modelo.toString())
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Blue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "Campana",
                                tint = Color.White // o el color que quieras
                            )
                            Text("Historial")
                        }
                    }


                    Spacer(modifier = Modifier.height(24.dp))
                    Spacer(modifier = Modifier.height(24.dp))

                    // Next Maintenance Section
                    Text(text = "Proximos Mantenimientos", fontWeight = FontWeight.Bold)
                    if(calcularProximosMantenimientos(vehiculo.notificaciones, vehiculo.kilometros).isEmpty()) {
                        Text(text = "No hay notificaciones proximas")
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(calcularProximosMantenimientos(vehiculo.notificaciones, vehiculo.kilometros)) { notificacion ->
                                ListItem(
                                    labelServicio = notificacion.tipoServicio.toString(),
                                    labelKm = "Prox. Rev: ",
                                    Km = "${notificacion.kilometrosProximoServicio}Km",
                                    labelAux = "",
                                    aux= ""
                                )
//                                HorizontalDivider(thickness = 2.dp, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // History Section
                    Text(text = "Mantenimientos", fontWeight = FontWeight.Bold)
                   if (vehiculo.notificaciones.isEmpty()) {
                       Text(
                           text = "No hay mantenimientos registrados",
                       )
                   }
                    LazyColumn {
                        items(vehiculo.notificaciones) { notificacion ->
                            ListItem(
                                labelServicio = notificacion.tipoServicio.toString(),
                                labelKm = "Prox. Rev: ",
                                Km = "${notificacion.kilometrosProximoServicio}Km",
                                labelAux = "Ult. Rev: ",
                                aux = "${notificacion.kilometrosUltimoServicio}Km"
                            )
                            Spacer(modifier = Modifier.height(5.dp)) // "
//                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                        }

                    }
                }


            }
        }
    }
}

@Composable
fun ListItem(labelServicio: String, labelKm: String,  Km: String, labelAux: String, aux: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Color(0xFFF1F1F1))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val color = Color.Black

                // Línea superior
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )

                // Línea inferior
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
                        },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = labelServicio)
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "$labelKm $Km")
            if(labelAux != ""){
            Text(text = "$labelAux $aux", style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray))
            }
        }
    }
}







