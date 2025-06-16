package com.application.carlosgarro.mygarageapp.presentation.mapa

//import com.application.carlosgarro.mygarageapp.data.external.maps.client.LugaresClient
//import com.application.carlosgarro.mygarageapp.data.external.maps.response.LugaresResponse
//import com.application.carlosgarro.mygarageapp.data.external.maps.response.PlaceResult
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.carlosgarro.mygarageapp.core.construirFotoUrl
import com.application.carlosgarro.mygarageapp.core.getApiKeyFromManifest
import com.application.carlosgarro.mygarageapp.data.external.maps.client.PlacesService
import com.application.carlosgarro.mygarageapp.data.external.maps.response.Lugar
import com.application.carlosgarro.mygarageapp.data.external.maps.response.ReviewsResponse
import com.application.carlosgarro.mygarageapp.presentation.components.BottomBar
import com.application.carlosgarro.mygarageapp.presentation.components.TopBar
import com.application.carlosgarro.mygarageapp.presentation.components.TopBarViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


@Composable
fun MapaScreen(
    navigateToHome: () -> Unit = {},
    navigateToMapa: () -> Unit = {},
    navigateToInitial: () -> Unit = {},
    topBarViewModel: TopBarViewModel = hiltViewModel(),
    navigateToConsejo: () -> Unit = {},

) {
    val context = LocalContext.current
    val apiKey = remember { getApiKeyFromManifest(context) }

    Scaffold(
        topBar = {
            TopBar(topBarViewModel, navigateToInitial)
        },
        bottomBar = {
            BottomBar(1, navigateToHome, navigateToMapa,navigateToConsejo)
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {

            MapaTalleresScreen(
                apiKey = apiKey
            )
        }
    }
}


@Composable
fun MapaTalleresScreen(apiKey: String) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val scope = rememberCoroutineScope()

    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState()
    var talleres by remember { mutableStateOf<List<Lugar>>(emptyList()) }
    var selectedTaller by remember { mutableStateOf<Lugar?>(null) }

    var reviews by remember { mutableStateOf<List<ReviewsResponse>>(emptyList()) }

    val placesApi = remember { PlacesService.create() }

    // Permiso
    val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)


        var hasLocationPermission by remember {
        mutableStateOf(
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Launcher para pedir el permiso
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasLocationPermission = isGranted
            if (isGranted) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        userLocation = latLng
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 16f)
                    }
                }
            }
        }
    )

    // Solo se ejecuta una vez al entrar
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    userLocation = latLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 16f)
                    // Llamada a la API
                    if(apiKey.isNotEmpty()) {
                        Log.i("MAPA", "API Key: $apiKey")
                        // Llamada a la API de Google Places para obtener talleres cercanos
                        scope.launch {
                            val locationString = "${latLng.latitude},${latLng.longitude}"
                            Log.i("MAPA", "Obteniendo talleres cercanos a: $locationString")
                            try {
                                val response =
                                    placesApi.getNearbyCarRepair(locationString, apiKey = apiKey)
                                talleres = response.results!!
                                Log.i("MAPA", "Talleres encontrados: $response")
                                Log.i("MAPA", "Talleres encontrados: ${response.status}")
                            } catch (e: Exception) {
                                Log.e("MAPA", "Error: ${e.localizedMessage}")
                            }
                        }
                    }
                }
            }
        }
    }
    if(!hasLocationPermission) {
        // Mostrar un mensaje si no se tienen permisos
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Esperando permiso de ubicación...")
        }
    }else {
        Column(Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.weight(1f),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = permission == PackageManager.PERMISSION_GRANTED),
                onMapClick = {
                    selectedTaller = null
                }
            ) {
                // Marcadores de talleres
                talleres.forEach { taller ->
                    val lat = taller.geometry?.location?.lat
                    val lng = taller.geometry?.location?.lng
                    if (lat != null || lng != null) {
                        Marker(
                            state = MarkerState(position = LatLng(lat!!, lng!!)),
                            title = taller.name,
                            snippet = taller.vicinity,
                            onClick = {
                                selectedTaller = taller
                                false
                            }
                        )
                    }
                }

            }

            // Info del taller seleccionado
            selectedTaller?.let { taller ->
                Log.i("MAPA", "Taller seleccionado: $taller")
                val fotoUrl = taller.photos
                    ?.firstOrNull()
                    ?.photoReference
                    ?.let { construirFotoUrl(it, apiKey) }
                scope.launch {
                    try {
                        Log.i("MAPA", "Obteniendo reseñas para el taller: ${taller.placeId}")
                        val response =
                            taller.placeId?.let {
                                placesApi.getPlaceReviews(
                                    placeId = it,
                                    apiKey = apiKey
                                )
                            }
                        if (response != null) {
                            reviews = response.result?.reviews ?: emptyList()
                            Log.i("MAPA", "Reviews: ${response.result?.reviews?.size ?: 0}")
                        }
                    } catch (e: Exception) {
                        Log.e("MAPA", "Error: ${e.localizedMessage}")
                    }
                }
                InfoTaller(
                    placeId = taller.placeId?: "",
                    nombre = taller.name ?: "Sin nombre",
                    direccion = taller.vicinity ?: "Sin dirección",
                    abierto = taller.openingHours?.openNow ?: false,
                    rating = taller.rating,
                    fotoUrl = fotoUrl,
                    reviews = reviews,
                    context = context
                )
            }
        }
    }
}
