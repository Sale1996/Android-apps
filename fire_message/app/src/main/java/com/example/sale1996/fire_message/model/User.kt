package com.example.sale1996.fire_message.model

data class User(val name: String,
                val bio: String,
                val profilePicturePath: String?,
                val registrationTokens: MutableList<String>) {

    constructor(): this("","",null, mutableListOf())
}