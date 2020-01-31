package com.example.entryticketsales.vos

import java.io.Serializable
data class Ticket(
    val id: String,
    val location: String,
    val name: String,
    val price: String
) : Serializable