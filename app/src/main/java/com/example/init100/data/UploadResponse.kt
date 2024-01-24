package com.example.init100.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    @SerialName("aid")
    val aid: Int,
    @SerialName("hash")
    val hash: String,
    @SerialName("photos_list")
    val photosList: String,
    @SerialName("server")
    val server: Int
)