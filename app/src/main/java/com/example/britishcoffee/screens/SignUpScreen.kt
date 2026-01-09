package com.example.britishcoffee.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.britishcoffee.viewmodels.OrderViewModel

@Composable
fun SignUpScreen(navController: NavController, viewModel: OrderViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.coffee_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 230.dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = CoffeeDarkBrown
            )
            Text(
                text = "Sign up to start your coffee journey",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                placeholder = { Text("e.g. Hamza Jou") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeeOrange,
                    focusedLabelColor = CoffeeOrange
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                placeholder = { Text("example@gmail.com") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeeOrange,
                    focusedLabelColor = CoffeeOrange
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoffeeOrange,
                    focusedLabelColor = CoffeeOrange
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                    else if (!email.contains("@")) {
                        Toast.makeText(context, "Please enter a valid email (@)", Toast.LENGTH_SHORT).show()
                    }
                    else if (password.length < 6) {
                        Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        // 2. Register user in shared ViewModel
                        val isSuccess = viewModel.registerUser(name, email, password)
                        if (isSuccess) {
                            Toast.makeText(context, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") {
                                popUpTo("signup") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Email is already registered", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeDarkBrown),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.clickable { navController.navigate("login") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account? ", color = Color.Gray)
                Text(
                    text = "Login",
                    color = CoffeeOrange,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
