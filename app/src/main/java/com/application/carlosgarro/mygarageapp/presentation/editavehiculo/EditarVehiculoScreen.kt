package com.application.carlosgarro.mygarageapp.presentation.editavehiculo

import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.carlosgarro.mygarageapp.core.enums.EstadoVehiculo
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.presentation.components.BottomBar
import com.application.carlosgarro.mygarageapp.presentation.components.SelectorImagen
import com.application.carlosgarro.mygarageapp.presentation.components.TopBar
import com.application.carlosgarro.mygarageapp.presentation.home.DropdownSelector
import com.application.carlosgarro.mygarageapp.ui.theme.Blue
import com.application.carlosgarro.mygarageapp.ui.theme.Red


@Composable
fun EditarVehiculoScreen(
    vehiculoId: Long,
    navigateToHome: () -> Unit,
    navigateToMapa: () -> Unit,
    navigateToVehiculo: (Long) -> Unit,
    viewModel: EditarVehiculoViewModel = hiltViewModel()
) {

    val contexto = LocalContext.current
    val isLoading by viewModel.isLoading.observeAsState(true)
    val vehiculo by viewModel.vehiculo.observeAsState(VehiculoPersonalModel())
    val imagenSeleccionada by viewModel.imagenSeleccionada.observeAsState(null)

    var kilometros by remember { mutableStateOf(vehiculo.kilometros.toString()) }
    var estado by remember { mutableStateOf(vehiculo.estado) }

    LaunchedEffect(vehiculoId) {
        Log.i("ID", "ID: $vehiculoId")
        viewModel.setVehiculoId(vehiculoId)
    }

    LaunchedEffect(vehiculo.id) {
        vehiculo?.let {
            kilometros = it.kilometros.toString()
            estado = it.estado
        }
    }

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
        val context = LocalContext.current

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                viewModel.setImagenDesdeUri(context, it)
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                color = Color.Blue
            )
        } else {

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp, top = 25.dp)
                    ) {
                        Text(
                            text = "Editar Vehículo",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Blue
                        )
                    }
                    Text(
                        modifier = Modifier.padding(20.dp, top = 15.dp),
                        text = vehiculo.modelo.toString(),
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Spacer(modifier = Modifier.height(8.dp))



                    SelectorImagen(
                        imagenSeleccionada = imagenSeleccionada,
                        onSeleccionarImagen = { launcher.launch("image/*") }
                    )
                    Log.i("EDITAR VEHICULO", "Vehiculo: $vehiculo")
                    Log.i("EDITAR VEHICULO", "kilometros: $kilometros")
                    Log.i("EDITAR VEHICULO", "estado: $estado")
                    Spacer(modifier = Modifier.height(8.dp))


                    OutlinedTextField(
                        value = kilometros,
                        onValueChange = {
                            kilometros = it
                            vehiculo.kilometros = it.toIntOrNull() ?: 0
//                    onChange()
                        },
                        label = { Text("Kilómetros") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DropdownSelector("Estado", EstadoVehiculo.entries, estado) {
                        estado = it
//                onChange()
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Button(
                            onClick = {
                                viewModel.actualizarVehiculoPersonal(vehiculo)
                                navigateToVehiculo(vehiculo.id ?: 0L)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Blue),
                        ) {
                            Text("Guardar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.deleteVehiculoPersonal(vehiculo.id ?: 0L)
                                navigateToHome()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Red)
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
        }

}