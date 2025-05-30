package com.application.carlosgarro.mygarageapp.presentation.historial

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.mantenimiento.MantenimientoModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.presentation.components.BottomBar
import com.application.carlosgarro.mygarageapp.presentation.components.TopBar
import com.application.carlosgarro.mygarageapp.ui.theme.Blue
import java.time.LocalDate


@Composable
fun HistorialScreen(
    id: Long,
    vehiculo: String,
    viewModel: HistorialViewModel = hiltViewModel(),
) {

    val mantenimientos = viewModel.mantenimientos.observeAsState(emptyList()).value
    var mostrarFormulario by remember { mutableStateOf(false) }
    val mantenimiento by viewModel.mantenimiento.observeAsState(MantenimientoModel(vehiculoId = id))

    LaunchedEffect(id) {
        Log.i("ID", "ID: $id")
        viewModel.setVehiculoId(id)
    }

    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar()

        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarFormulario = !mostrarFormulario }) {
                if (mostrarFormulario) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar formulario",
                        tint = Color.Blue
                    )
                } else {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Nueva Entrada",
                        tint = Color.Blue
                    )
                }
            }
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            // Título del vehículo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, top = 25.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Icono de historial",
                    modifier = Modifier.padding(end = 8.dp),
                    tint  = Blue
                )
                Text(
                    text = "Historial",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
            }
            Text(
                modifier = Modifier.padding(20.dp, top = 15.dp),
                text = vehiculo,
                style = MaterialTheme.typography.titleMedium,
            )


            if (mostrarFormulario){
            FormularioNuevaEntrada(
                mantenimiento = mantenimiento,
                onChange = { Log.i("FORMULARIO HISTORIAL", "onChange: ${mantenimiento}")},
                onGuardar = {
                    viewModel.addEntradaMantenimiento(mantenimiento)
                    mostrarFormulario = false
                },
                onCancelar = {
                    mostrarFormulario = false
                }
            )
            }
            HistorialTab(padding = padding, nombreVehiculo = vehiculo, mantenimientos = mantenimientos)
        }
    }
}


@Composable
fun HistorialTab(
    padding: PaddingValues,
    nombreVehiculo: String,
    mantenimientos: List<MantenimientoModel>
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        // Cabecera de la tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(vertical = 8.dp),
        ) {
            Text("Servicio", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold)
            Text("Km", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("Fecha", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold)
            Text("Precio", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        }

        HorizontalDivider(thickness = 1.dp, color = Color.Gray)

        // Filas de la tabla
        LazyColumn {
            items(mantenimientos) { mantenimiento ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                ) {
                    Text(
                        mantenimiento.tipoServicio.toString() ?: "-",
                        modifier = Modifier.weight(1.0f)
                    )
                    Text(
                        mantenimiento.kilometrosServicio.toString() ?: "-",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        mantenimiento.fechaServicio.toString() ?: "-",
                        modifier = Modifier.weight(1.5f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "${mantenimiento.precio ?: "-"}€",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
                HorizontalDivider(color = Color.LightGray)
            }
        }
    }
}


