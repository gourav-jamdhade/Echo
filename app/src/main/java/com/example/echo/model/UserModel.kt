package com.example.echo.model

data class UserModel(
    val userId: String? = "",
    val email: String = "",
    val name: String = "",
    val bio: String = "",
    val userName: String = "",
    val imageUrl: String? = "",
)