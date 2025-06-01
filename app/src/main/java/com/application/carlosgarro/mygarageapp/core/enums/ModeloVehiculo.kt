package com.application.carlosgarro.mygarageapp.core.enums

enum class ModeloVehiculo(private val texto: String) {
    COROLLA("Corolla"),
    CIVIC("Civic"),
    FOCUS("Focus"),
    MALIBU("Malibu"),
    GOLF("Golf"),
    ALTIMA("Altima"),
    ELANTRA("Elantra"),
    SERIE_3("Serie 3"),
    CLASE_C("Clase C"),
    A4("A4"),
    SPORTAGE("Sportage"),
    IMPREZA("Impreza"),
    MODEL_3("Model 3"),
    DISCOVERY("Discovery"),
    XE("XE"),
    WRANGLER("Wrangler"),
    _308("308"),
    CLIO("Clio"),
    PUNTO("Punto"),
    ASTRA("Astra"),
    C3("C3"),
    SWIFT("Swift"),
    RX("RX"),
    CHARGER("Charger"),
    S60("S60"),
    _911("911"),
    LANCER("Lancer"),
    GIULIA("Giulia"),
    _488_SPIDER("488 Spider"),
    HURACAN("Huracán"),
    CONTINENTAL_GT("Continental GT");

    override fun toString(): String = texto
}