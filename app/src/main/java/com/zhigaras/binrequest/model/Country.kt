package com.zhigaras.binrequest.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Country(
    @Json(name = "alpha2")
    val alpha2: String?,
    @Json(name = "currency")
    val currency: String?,
    @Json(name = "emoji")
    val emoji: String?,
    @Json(name = "latitude")
    var latitude: String?,
    @Json(name = "longitude")
    var longitude: String?,
    @Json(name = "name")
    var name: String,
    @Json(name = "numeric")
    val numeric: String?
)