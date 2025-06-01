package com.application.carlosgarro.mygarageapp.core.enums

enum class TipoServicio(private val texto: String) {

    CAMBIO_DE_ACEITE("Cambio de aceite"),
    REVISION_DE_FRENOS("Revisión de frenos"),
    PRESION_DE_NEUMATICOS("Presión de neumáticos"),
    ALINEACION_Y_BALANCEO("Alineación y balanceo"),
    CAMBIO_DE_FILTRO_DE_AIRE("Cambio de filtro de aire"),
    REVISION_REFRIGERANTE("Revisión del refrigerante"),
    MANTENIMIENTO_DE_BATERIA("Mantenimiento de batería"),
    CAMBIO_DE_BUJIAS("Cambio de bujías"),
    LIQUIDO_DE_TRANSMISION("Líquido de transmisión"),
    SISTEMA_DE_ESCAPE("Sistema de escape"),
    REVISION_DE_LUCES("Revisión de luces"),
    LIMPIEZA_DEL_PARABRISAS("Limpieza del parabrisas"),
    INSPECCION_DEL_CINTURON_DE_DISTRIBUCION("Inspección del cinturón de distribución"),
    REVISION_DE_SUSPENSION("Revisión de suspensión"),
    LIQUIDO_DE_FRENOS("Líquido de frenos"),
    SISTEMA_DE_DIRECCION("Sistema de dirección"),
    CAMBIO_DE_FILTRO_DE_COMBUSTIBLE("Cambio de filtro de combustible"),
    REVISION_DEL_ALTERNADOR("Revisión del alternador"),
    MANTENIMIENTO_DEL_AIRE_ACONDICIONADO("Mantenimiento del aire acondicionado"),
    REVISION_DE_MANGUERAS_Y_CORREAS("Revisión de mangueras y correas"),
    CHEQUEO_DEL_SISTEMA_ELECTRICO("Chequeo del sistema eléctrico"),
    COMPROBACION_DEL_CONSUMO_DE_COMBUSTIBLE("Comprobación del consumo de combustible"),
    LAVADO_Y_ENCERADO("Lavado y encerado"),
    REVISION_DEL_SISTEMA_DE_SEGURIDAD("Revisión del sistema de seguridad"),
    REVISION_DEL_SISTEMA_DE_DIRECCION("Revisión del sistema de dirección"),
    OTRO("Otro");


    override fun toString(): String = texto
}