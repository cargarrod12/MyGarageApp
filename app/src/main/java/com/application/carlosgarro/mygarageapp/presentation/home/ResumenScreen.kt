package com.application.carlosgarro.mygarageapp.presentation.home


import android.graphics.BitmapFactory
import android.util.Log
import android.view.Gravity
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.carlosgarro.mygarageapp.core.calcularProximosMantenimientos
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.presentation.components.BottomBar
import com.application.carlosgarro.mygarageapp.presentation.components.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ResumenViewModel = hiltViewModel(),
    navigateToVehiculo: (Long) -> Unit = {},
    navigateToHistorial: (Long, String) -> Unit,
    navigateToHome: () -> Unit = {},
    navigateToMapa: () -> Unit = {},
    navigateToEditarVehiculo: (Long) -> Unit = {}
    ) {

    val tabTitles = listOf("Resumen", "Mis Coches")
    var selectedTab by remember { mutableStateOf(0) }
    val listaVehiculosPersonales by viewModel.listaVehiculoPersonal.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    var mostrarFormulario by remember { mutableStateOf(false) }
    val vehiculo by viewModel.vehiculoPersonal.observeAsState(VehiculoPersonalModel())
    val listaVehiculos by viewModel.listaVehiculo.observeAsState(emptyList())
    val imagenUri by viewModel.imagenSeleccionadaUri.observeAsState(null)


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
                        contentDescription = "Agregar coche",
                        tint = Color.Blue
                    )
                }
            }
        }
    ) { padding ->

        if (isLoading == true) {
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
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = Color.Blue,
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color.Blue
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
                if (mostrarFormulario) {
                    FormularioNuevoCoche(
                        viewModel,
                        vehiculo,
                        listaVehiculos,
                        imagenUri,
                        onChange = {
                            Log.i("FORMULARIO VEHICULO", "onChange: ${vehiculo}")
                        },
                        onGuardar = {
                            viewModel.addVehiculoPersonal(vehiculo)
                            mostrarFormulario = false
                        },
                        onCancelar = {
                            mostrarFormulario = false
                        },
                    )
                }else{
                    if (selectedTab == 0) {
                        ResumenTab(padding, listaVehiculosPersonales, navigateToVehiculo, navigateToHistorial)
                    } else {
                        MisCochesTab(padding, listaVehiculosPersonales, navigateToEditarVehiculo)
                    }
                }




            }
        }
    }
}

@Composable
fun CocheCard(
    vehiculo : VehiculoPersonalModel,
    mantenimientos: List<NotificacionModel>,
    navigateToVehiculo: (Long) -> Unit = {},
    navigateToHistorial: (Long, String) -> Unit
) {
    Column(
        modifier = Modifier.clickable {
            Log.i("CocheCard", "CocheCard: ${vehiculo.id}")
            navigateToVehiculo(vehiculo.id?: 0L)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text(vehiculo.modelo.toString(), style = MaterialTheme.typography.titleMedium)
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "Actualizar",
                tint = Color.Blue,
                modifier = Modifier
                    .clickable {
                        navigateToHistorial(vehiculo.id?: 0L, vehiculo.modelo.toString())
                        Log.d("Icono", "Icono clickeado, acción de actualización")
                    }
            )
        }

        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(top = 8.dp)) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.LightGray)
            ) {
                if (vehiculo.imagen != null) {
                    val bitmap = BitmapFactory.decodeByteArray(vehiculo.imagen, 0, vehiculo.imagen.size)
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    // Si no hay imagen, mostramos un icono por defecto
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Imagen coche",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Row {
                    Text("Estado: ", fontWeight = FontWeight.Bold)
                    Text(vehiculo.estado.name)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(vehiculo.kilometros.toString() + "Km")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Proximos Mantenimientos:", color = Color.Blue)
                if (mantenimientos.isEmpty()) {
                    Text("No hay mantenimientos pendientes")
                }else {
                    mantenimientos.forEach {
                        Text("- ${it.tipoServicio}: ${it.kilometrosProximoServicio}Km")
                    }
                }
            }
        }
    }
}


@Composable
fun ResumenTab(padding: PaddingValues, registros: List<VehiculoPersonalModel>, navigateToVehiculo: (Long) -> Unit, navigateToHistorial: (Long, String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        items(registros) { vechiulo ->
            CocheCard(
                vehiculo = vechiulo,
                mantenimientos = calcularProximosMantenimientos(vechiulo.notificaciones, vechiulo.kilometros),
                navigateToVehiculo = navigateToVehiculo,
                navigateToHistorial = navigateToHistorial
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}






