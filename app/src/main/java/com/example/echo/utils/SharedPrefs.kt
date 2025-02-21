package com.example.echo.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPrefs {

    fun storeData(
        name: String,
        email: String,
        userName: String,
        bio: String,
        imageUrl: String,
        context: Context
    ) {

        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("userName", userName)
        editor.putString("bio", bio)
        editor.putString("imageUrl", imageUrl)
        editor.apply()

    }

    fun getUserName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("userName", "")!!

    }

    fun getEmail(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("email", "")!!

    }

    fun getBio(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("bio", "")!!

    }

    fun getName(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("name", "")!!

    }

    fun getImageUrl(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl", "")!!

    }
}