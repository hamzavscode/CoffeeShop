package com.example.britishcoffee.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.R
import com.example.britishcoffee.navigation.CoffeeDarkBrown
import com.example.britishcoffee.navigation.CoffeeOrange
import com.example.britishcoffee.viewmodels.OrderViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: OrderViewModel) {
    val user = viewModel.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F4EE))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(CoffeeDarkBrown, Color(0xFF5E3928))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.double_chocolate),
                        contentDescription = null,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .background(Color.White),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CoffeeOrange)
                            .border(2.dp, Color.White, CircleShape)
                            .clickable { /* Edit Photo */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = user?.name ?: "Guest User",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CoffeeDarkBrown
                )
                Text(
                    text = user?.email ?: "guest@example.com",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))



        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {

            SectionHeader("Account Settings")

            ProfileMenuItem(
                icon = Icons.Outlined.Person,
                title = "Personal Information",
                subtitle = "Name, Email, Phone number"
            ) { }

            ProfileMenuItem(
                icon = Icons.Outlined.LocationOn,
                title = "Delivery Addresses",
                subtitle = "Home, Office, etc."
            ) { }

            ProfileMenuItem(
                icon = Icons.Outlined.Payment,
                title = "Payment Methods",
                subtitle = "Cards, Wallets"
            ) { }

            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader("Preference")

            ProfileMenuItem(
                icon = Icons.Outlined.History,
                title = "My Orders",
                subtitle = "History and active tracking"
            ) {
                navController.navigate("orders")
            }

            ProfileMenuItem(
                icon = Icons.Outlined.Settings,
                title = "App Settings",
                subtitle = "Theme, Notifications, Language"
            ) {
                navController.navigate("settings")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Logout Button ---
            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate("login") { popUpTo("home") { inclusive = true } }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Logout, null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoffeeOrange)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
    )
}

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CoffeeOrange.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = CoffeeOrange, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = CoffeeDarkBrown)
                Text(text = subtitle, fontSize = 11.sp, color = Color.Gray)
            }

            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
        }
    }
}
