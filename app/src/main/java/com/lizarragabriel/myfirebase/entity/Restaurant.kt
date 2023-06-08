package com.lizarragabriel.myfirebase.entity

import java.io.Serializable

data class Restaurant(
    var name: String? = null,
    var url: String? = null,
    var id: String? = null,
    var geo: Geo? = null,
    var descripcion: String? = null,
    var contacto: Contacto? = null): Serializable

data class Geo(
    var latitude: Double? = -34.0,
    var longitude: Double? = 151.0,
    var colonia: String? = null)

data class Contacto(
    var tel: String? = "6699",
    var web: String? = "www.com.mx")
