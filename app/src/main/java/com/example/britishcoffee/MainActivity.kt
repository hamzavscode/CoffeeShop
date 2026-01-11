package com.example.britishcoffee

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.britishcoffee.navigation.AppNavigation
import com.example.britishcoffee.ui.theme.BritishCoffeeTheme
import com.example.britishcoffee.viewmodels.OrderViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val sharedViewModel: OrderViewModel = viewModel()



            val darkTheme = when (sharedViewModel.isDarkMode) {
                true -> true
                false -> false
                else -> isSystemInDarkTheme()
            }

            BritishCoffeeTheme(darkTheme = darkTheme) {
                AppNavigation(sharedViewModel)
            }
        }
    }
}
