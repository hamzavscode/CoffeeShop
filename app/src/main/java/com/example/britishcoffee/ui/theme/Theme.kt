package com.example.britishcoffee.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.britishcoffee.R

@Composable
fun BritishCoffeeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colorResource(id = R.color.primary_orange),

            background = Color(0xFF0C0908),

            surface = Color(0xFF17100E),

            onBackground = Color.White,
            onSurface = Color.White,

            onSurfaceVariant = Color(0xFFB8B8B8)
        )
    } else {
        lightColorScheme(
            primary = colorResource(id = R.color.primary_orange),
            background = colorResource(id = R.color.background_beige),
            surface = colorResource(id = R.color.surface_white),
            onBackground = colorResource(id = R.color.text_primary),
            onSurface = colorResource(id = R.color.text_primary)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
