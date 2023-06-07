package com.example.intranetredm.io.response

import com.example.intranetredm.model.User

data class LoginResponse(
    val success: Boolean,
    val user: User?,
    val token: String
)