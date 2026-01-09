package com.example.britishcoffee.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.R
import com.example.britishcoffee.navigation.CoffeeDarkBrown
import com.example.britishcoffee.navigation.CoffeeOrange
import com.example.britishcoffee.viewmodels.OrderViewModel // Import ViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: OrderViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.coffee_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 250.dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color.White)
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome Back!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = CoffeeDarkBrown)
            Text("Login to continue", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email Address") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CoffeeOrange, focusedLabelColor = CoffeeOrange)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CoffeeOrange, focusedLabelColor = CoffeeOrange)
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text("Forgot Password?", fontSize = 12.sp, color = CoffeeOrange, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.End))

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                    }
                    else if (!email.contains("@")) {
                        Toast.makeText(context, "Invalid Email format", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val isAuthenticated = viewModel.loginUser(email, password)
                        if (isAuthenticated) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Wrong Email or Password, Please Sign Up first.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeDarkBrown),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.clickable { navController.navigate("signup") }) {
                Text("Don't have an account? ", color = Color.Gray)
                Text("Sign Up", color = CoffeeOrange, fontWeight = FontWeight.Bold)
            }
        }
    }
}
