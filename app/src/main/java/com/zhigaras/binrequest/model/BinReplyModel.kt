package com.zhigaras.binrequest.model


import com.google.gson.annotations.SerializedName

data class BinReplyModel(
    @SerializedName("bank")
    val bank: Bank?,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("country")
    val country: Country?,
    @SerializedName("number")
    val number: Number?,
    @SerializedName("prepaid")
    val prepaid: Boolean?,
    @SerializedName("scheme")
    val scheme: String?,
    @SerializedName("type")
    val type: String?
)