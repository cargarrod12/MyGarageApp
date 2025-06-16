//package com.application.carlosgarro.mygarageapp.domain.model.consejo
//
//import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
//import com.application.carlosgarro.mygarageapp.data.local.entity.ConsejoEntity
//
//data class ConsejoModel(
//    val tipoServicio: TipoServicio,
//    val consejo: String = ""
//)
//
//fun ConsejoModel.toEntity(): ConsejoEntity {
//    return ConsejoEntity(
//        tipoServicio = tipoServicio,
//        consejo = consejo
//    )
//}
//
//fun ConsejoEntity.toModel(): ConsejoModel {
//    return ConsejoModel(
//        tipoServicio = tipoServicio,
//        consejo = consejo
//    )
//}