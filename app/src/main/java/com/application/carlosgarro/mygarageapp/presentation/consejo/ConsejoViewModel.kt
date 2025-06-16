package com.application.carlosgarro.mygarageapp.presentation.consejo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.carlosgarro.mygarageapp.data.external.consultasia.api.ConsejosApi
import com.application.carlosgarro.mygarageapp.data.external.consultasia.request.ChatGPTRequest
import com.application.carlosgarro.mygarageapp.data.external.consultasia.request.Message
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoPersonalByUserUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ConsejoViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val getVehiculoPersonalByUser: getVehiculoPersonalByUserUseCase,
): ViewModel(){

    private val _eventoMensaje = MutableSharedFlow<String>()
    val eventoMensaje: SharedFlow<String> = _eventoMensaje

    private val _listaVehiculoPersonal = MutableLiveData<List<VehiculoPersonalModel>>(emptyList())
    val listaVehiculoPersonal: LiveData<List<VehiculoPersonalModel>> = _listaVehiculoPersonal

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _respuesta = MutableLiveData("")
    val respuesta : LiveData<String> = _respuesta

    init {
        cargaVechiculos()
    }

    fun cargaVechiculos() {
        viewModelScope.launch {
            val usuarioEmail = firebaseAuth.currentUser?.email
            if (usuarioEmail != null) {
                println(usuarioEmail)
                getVehiculoPersonalByUser(usuarioEmail).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _listaVehiculoPersonal.value = emptyList()
                            _isLoading.value = true
//                            //Log.i("CONSEJO", "Loading VehiculosPersonalesByUSer")
                        }
                        is Resource.Success -> {
                            _listaVehiculoPersonal.value = resource.data ?: emptyList()
                            _isLoading.value = false
//                            //Log.i("CONSEJO", "Success: ${resource.data}")
                        }
                        is Resource.Error -> {
                            _listaVehiculoPersonal.value = emptyList()
                            _isLoading.value = false
//                            //Log.e("CONSEJO", "Error: ${resource.message}")
                        }
                    }
                }
            }else {
                _listaVehiculoPersonal.value = emptyList()
            }
        }

    }

    fun consultarIA(vehiculoSeleccionado: VehiculoPersonalModel, pregunta: String, api: ConsejosApi) {
        val mensajes = listOf(Message("user", formatearPregunta(vehiculoSeleccionado, pregunta)),
//                              Message("system", "Eres un experto en mecánica automotriz y estás aquí para ayudar a los usuarios con sus problemas de vehículos.")
        )
        val request = ChatGPTRequest(messages = mensajes)

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val respose = api.enviarPregunta(request)
                //Log.i("CONSEJO", "Respuesta de la IA: $respose")
                if (respose.choices.isNotEmpty()) {
                    val respuestaIA = respose.choices[0].message.content
                    _respuesta.value = respuestaIA
                    _isLoading.value = false
                    //Log.i("CONSEJO", "Consejo de la IA: $respuestaIA")

                } else {
                    _respuesta.value = "No se obtuvo una respuesta válida de la IA."
                    //Log.e("CONSEJO", "No se obtuvo una respuesta válida de la IA.")
                    _isLoading.value = false
                }
            } catch (e: Exception) {
               //Log.e("CONSEJO", "Error al enviar pregunta: ${e.message}")
                _eventoMensaje.emit("Ha ocurrido un error al realizar la consulta. Por favor, inténtelo de nuevo más tarde.")
                println(_eventoMensaje)
                _isLoading.value = false
            }
        }

    }

    private fun formatearPregunta(vehiculo: VehiculoPersonalModel, pregunta: String): String {
        return """
        Basandote en conocimiento sobre los vehiculos. 
        Mi vehículo es un ${vehiculo.modelo}, fabricado en el año ${vehiculo.anyo} y actualmente tiene ${vehiculo.kilometros} kilómetros recorridos. 
        
        Basado en estos datos, responde detalladamente a la siguiente consulta:
        "${pregunta}"

        Proporciona una respuesta clara, estructurada y fácil de entender.
    """.trimIndent()
    }

    fun setRespuestaVacia() {
        _respuesta.value = ""
    }

}
