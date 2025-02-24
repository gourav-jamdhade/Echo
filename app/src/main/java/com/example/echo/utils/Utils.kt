package com.example.echo.utils

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.echo.R


class Utils{
    private val fontFamily = FontFamily(
        Font(R.font.poppins, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.poppins_bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium, FontStyle.Normal),
        Font(R.font.poppins_light, FontWeight.Light, FontStyle.Normal),
        Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic)
    )


    fun getFontFamily(): FontFamily {
        return fontFamily
    }

}
