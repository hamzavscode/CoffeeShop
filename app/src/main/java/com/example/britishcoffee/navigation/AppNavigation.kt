package com.example.britishcoffee.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.britishcoffee.R
import com.example.britishcoffee.screens.*
import com.example.britishcoffee.viewmodels.OrderViewModel

@Composable
fun AppNavigation(sharedViewModel: OrderViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val orangeColor = colorResource(id = R.color.primary_orange)

    Scaffold(
        bottomBar = {
            val bottomBarScreens = listOf("home", "cart", "profile", "menu", "wishlist", "orders")
            val showBottomBar = currentRoute in bottomBarScreens

            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = NavigationBarDefaults.Elevation
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Explore, null) },
                        label = { Text(stringResource(id = R.string.explorer)) },
                        selected = currentRoute == "home",
                        onClick = { navController.navigate("home") },
                        colors = navigationItemColors(orangeColor)
                    )

                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.ShoppingCart, null) },
                        label = { Text(stringResource(id = R.string.cart)) },
                        selected = currentRoute == "cart",
                        onClick = { navController.navigate("cart") },
                        colors = navigationItemColors(orangeColor)
                    )

                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Favorite, null) },
                        label = { Text(stringResource(id = R.string.wishlist)) },
                        selected = currentRoute == "wishlist",
                        onClick = { navController.navigate("wishlist") },
                        colors = navigationItemColors(orangeColor)
                    )

                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.ReceiptLong, null) },
                        label = { Text(stringResource(id = R.string.my_order)) },
                        selected = currentRoute == "orders",
                        onClick = { navController.navigate("orders") },
                        colors = navigationItemColors(orangeColor)
                    )

                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Person, null) },
                        label = { Text(stringResource(id = R.string.profile)) },
                        selected = currentRoute == "profile",
                        onClick = { navController.navigate("profile") },
                        colors = navigationItemColors(orangeColor)
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(paddingValues)
        ) {
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
            composable("settings") { SettingsScreen(navController, sharedViewModel) }

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


@Composable
private fun navigationItemColors(selectedColor: Color): NavigationBarItemColors {
    return NavigationBarItemDefaults.colors(
        selectedIconColor = selectedColor,
        unselectedIconColor = Color.Gray,
        selectedTextColor = selectedColor,
        unselectedTextColor = Color.Gray,
        indicatorColor = Color.Transparent
    )
}
