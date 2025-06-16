package com.application.carlosgarro.mygarageapp.presentation.consejo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.carlosgarro.mygarageapp.data.external.consultasia.client.ConsejosClient
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.presentation.components.BottomBar
import com.application.carlosgarro.mygarageapp.presentation.components.TopBar
import com.application.carlosgarro.mygarageapp.presentation.components.TopBarViewModel

@Composable
fun ConsejoScreen(
    viewModel: ConsejoViewModel = hiltViewModel(),
    navigateToHome: () -> Unit = {},
    navigateToMapa: () -> Unit = {},
    navigateToInitial: () -> Unit = {},
    topBarViewModel: TopBarViewModel = hiltViewModel(),
    navigateToConsejo: () -> Unit = {},
) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()
    val listaVehiculosPersonales by viewModel.listaVehiculoPersonal.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    var vehiculoSeleccionado by remember { mutableStateOf<VehiculoPersonalModel?>(null) }
    var pregunta by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val respuesta by viewModel.respuesta.observeAsState("")
    val api = remember { ConsejosClient.create() }

    Scaffold(
        topBar = {
            TopBar(
                viewModel = topBarViewModel,
                navigateToInitial = navigateToInitial,
            )
        },
        bottomBar = {
            BottomBar(2, navigateToHome, navigateToMapa, navigateToConsejo)
        },
    ) { padding ->

        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp), color = Color.Blue)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Cargando datos...", color = Color.Black)
            }
        } else {
            if (respuesta.isBlank()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Selecciona tu vehículo")
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Text(
                                text = vehiculoSeleccionado?.modelo?.toString()
                                    ?: "Selecciona un vehículo"
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listaVehiculosPersonales.forEach { vehiculo ->
                                DropdownMenuItem(
                                    text = { Text(vehiculo.modelo.toString()) },
                                    onClick = {
                                        vehiculoSeleccionado = vehiculo
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Escribe tu consulta")
                    OutlinedTextField(
                        value = pregunta,
                        onValueChange = { pregunta = it },
                        label = { Text("Pregunta") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            vehiculoSeleccionado?.let {
                                viewModel.consultarIA(it, pregunta, api)
                            }
                        },
                        enabled = vehiculoSeleccionado != null && pregunta.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Consultar IA")
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Tu pregunta:", fontWeight = FontWeight.Bold)
                        Text(text = pregunta, modifier = Modifier.padding(8.dp), fontStyle = FontStyle.Italic)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Respuesta:", fontWeight = FontWeight.Bold)
                        Text(text = respuesta ?: "", modifier = Modifier.padding(8.dp))

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                pregunta = ""
                                viewModel.setRespuestaVacia()
                                vehiculoSeleccionado = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Hacer otra consulta")
                        }
                    }
                }
            }
        }
    }
}
