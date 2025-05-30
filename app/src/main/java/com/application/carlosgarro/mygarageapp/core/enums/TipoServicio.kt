package com.application.carlosgarro.mygarageapp.core.enums

enum class TipoServicio(private val texto: String) {
    CAMBIO_ACEITE("Cambio de aceite"),
    REVISION_FRENOS("Revisión de frenos"),
    CAMBIO_NEUMATICOS("Cambio de neumáticos"),
    REVISION_GENERAL("Revisión general"),
    ALINEACION("Alineación"),
    OTRO("Otro");

    override fun toString(): String = texto
}