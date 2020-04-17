package com.example.sale1996.fire_message.model

import java.util.*

data class ImageMessage(val imagePath: String,
                       override val time: Date,
                       override val senderId: String,
                       override val type: String = MessageType.IMAGE)
    : Message {
    constructor(): this("", Date(0), "")
}