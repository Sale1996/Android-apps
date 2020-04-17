package com.example.sale1996.fire_message.model

import java.util.*

data class TextMessage(val text: String,
                       override val time: Date,
                       override val senderId: String,
                       override val type: String = MessageType.TEXT)
    : Message {
    constructor(): this("", Date(0), "")
}