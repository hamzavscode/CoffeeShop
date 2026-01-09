package com.example.britishcoffee.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.britishcoffee.screens.*
import com.example.britishcoffee.screensimport.DetailScreen
import com.example.britishcoffee.viewmodels.OrderViewModel

val CoffeeDarkBrown = Color(0xFF3C2318)
val CoffeeOrange = Color(0xFFC67C4E)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val sharedViewModel: OrderViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val showBottomBar = currentRoute in listOf("home", "cart", "profile", "menu", "wishlist", "orders")
            if (showBottomBar) {
                NavigationBar(containerColor = CoffeeDarkBrown, contentColor = Color.White) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Explore, "Explorer") },
                        label = { Text("Explorer") },
                        selected = currentRoute == "home",
                        onClick = { navController.navigate("home") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = CoffeeOrange, unselectedIconColor = Color.Gray, selectedTextColor = CoffeeOrange, indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.ShoppingCart, "Cart") },
                        label = { Text("Cart") },
                        selected = currentRoute == "cart",
                        onClick = { navController.navigate("cart") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = CoffeeOrange, unselectedIconColor = Color.Gray, selectedTextColor = CoffeeOrange, indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Favorite, "Wishlist") },
                        label = { Text("Wishlist") },
                        selected = currentRoute == "wishlist",
                        onClick = { navController.navigate("wishlist") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = CoffeeOrange, unselectedIconColor = Color.Gray, selectedTextColor = CoffeeOrange, indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.ReceiptLong, "My Order") },
                        label = { Text("My Order") },
                        selected = currentRoute == "orders",
                        onClick = { navController.navigate("orders") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = CoffeeOrange, unselectedIconColor = Color.Gray, selectedTextColor = CoffeeOrange, indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Person, "Profile") },
                        label = { Text("Profile") },
                        selected = currentRoute == "profile",
                        onClick = { navController.navigate("profile") },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = CoffeeOrange, unselectedIconColor = Color.Gray, selectedTextColor = CoffeeOrange, indicatorColor = Color.Transparent)
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = "splash", modifier = Modifier.padding(paddingValues)) {
            composable("splash") { SplashScreen(navController) }
            composable("login") { LoginScreen(navController, sharedViewModel) }
            composable("signup") { SignUpScreen(navController, sharedViewModel) }
            composable("home") { HomeScreen(navController) }
            composable("menu") { MenuScreen(navController) }

            composable("cart") { CartScreen(navController, sharedViewModel) }
            composable("wishlist") { WishlistScreen(navController, sharedViewModel) }
            composable("orders") { MyOrderScreen(navController, sharedViewModel) }
            composable("profile") { ProfileScreen(navController, sharedViewModel) }
            composable("promo") { PromoScreen(navController, sharedViewModel) }
            composable("settings") { SettingsScreen(navController) }

            composable(
                route = "checkout/{totalAmount}",
                arguments = listOf(navArgument("totalAmount") { type = NavType.FloatType })
            ) { backStackEntry ->
                val totalAmount = backStackEntry.arguments?.getFloat("totalAmount")?.toDouble() ?: 0.0
                CheckoutScreen(navController, totalAmount, sharedViewModel)
            }

            composable(
                route = "detail/{coffeeId}",
                arguments = listOf(navArgument("coffeeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val coffeeId = backStackEntry.arguments?.getInt("coffeeId") ?: 1
                DetailScreen(navController, coffeeId, sharedViewModel)
            }
        }
    }
}
