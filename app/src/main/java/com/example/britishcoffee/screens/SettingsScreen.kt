package com.example.britishcoffee.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.navigation.CoffeeDarkBrown
import com.example.britishcoffee.navigation.CoffeeOrange

val SettingsBg = Color(0xFFF9F4EE)

@Composable
fun SettingsScreen(navController: NavController) {
    var showThemeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SettingsBg)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    modifier = Modifier.size(18.dp),
                    tint = CoffeeDarkBrown
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = CoffeeDarkBrown
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {

            Text("General", color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp))

            SettingCard(Icons.Default.Person, "Edit Profile", "Change your name and photo") {
                navController.navigate("profile")
            }

            SettingCard(Icons.Default.Palette, "Appearance", "Dark mode, Light mode") {
                showThemeDialog = true
            }

            SettingCard(Icons.Default.Notifications, "Notifications", "Manage your alerts") { }

            SettingCard(Icons.Default.Language, "Language", "English, Arabic, French") { }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Privacy & Security", color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp))

            SettingCard(Icons.Default.Lock, "Security", "Password and Biometrics") { }

            SettingCard(Icons.Default.Shield, "Privacy Policy", "How we protect your data") { }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Support", color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp))

            SettingCard(Icons.Default.Info, "About Us", "Version 1.0.0") { }

            SettingCard(Icons.Default.Help, "Help Center", "FAQs and contact support") { }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Appearance", fontWeight = FontWeight.Bold, color = CoffeeDarkBrown) },
            text = {
                Column {
                    ThemeOption("Light Mode", Icons.Default.LightMode) { showThemeDialog = false }
                    ThemeOption("Dark Mode", Icons.Default.DarkMode) { showThemeDialog = false }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cancel", color = CoffeeOrange, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun SettingCard(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CoffeeOrange.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = CoffeeOrange, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CoffeeDarkBrown)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

@Composable
fun ThemeOption(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = CoffeeDarkBrown)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, color = Color.Black)
    }
}

