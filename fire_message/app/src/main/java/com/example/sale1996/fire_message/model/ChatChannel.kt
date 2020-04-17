package com.example.sale1996.fire_message.model

data class ChatChannel(val userIds: MutableList<String>){
    constructor(): this(mutableListOf())
}