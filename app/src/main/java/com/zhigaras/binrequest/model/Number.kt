package com.zhigaras.binrequest.model


import com.google.gson.annotations.SerializedName

data class Number(
    @SerializedName("length")
    val length: Int?,
    @SerializedName("luhn")
    val luhn: Boolean?
)