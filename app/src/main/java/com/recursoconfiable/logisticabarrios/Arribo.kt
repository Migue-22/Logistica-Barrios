package com.recursoconfiable.logisticabarrios

data class Arribo(
    val id: Int,
    val folio: String,
    val numero_de_tarimas: Int,
    val numero_de_cajas: Int,
    val linea_de_transporte: String,
    val horario_de_entrega: String,
    val estatus:String

)
