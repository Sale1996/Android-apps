package com.example.sale1996.fire_message.model

import java.util.*

/*
* Imace funkcionalnost koja se nalazi i u text message i image message
* */
object MessageType {
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}

interface Message {
    val time: Date
    val senderId: String
    val type: String
}