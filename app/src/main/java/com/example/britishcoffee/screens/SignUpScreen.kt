package com.example.britishcoffee.screens

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.R
import com.example.britishcoffee.viewmodels.OrderViewModel

@Composable
fun SignUpScreen(navController: NavController, viewModel: OrderViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val darkBrown = colorResource(id = R.color.primary_dark_brown)
    val textSecondary = colorResource(id = R.color.text_secondary)

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val offsetY by animateDpAsState(targetValue = if (visible) 0.dp else 100.dp, animationSpec = tween(600), label = "")

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, label = "")

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        Image(
            painter = painterResource(id = R.drawable.coffee_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(280.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 230.dp)
                .offset(y = offsetY)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(surfaceColor)
                .verticalScroll(rememberScrollState())
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.create_account),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = onBackgroundColor
            )
            Text(
                text = stringResource(id = R.string.signup_subtitle),
                fontSize = 14.sp,
                color = textSecondary
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.full_name)) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryOrange,
                    focusedLabelColor = primaryOrange,
                    unfocusedTextColor = onBackgroundColor,
                    focusedTextColor = onBackgroundColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(id = R.string.email_address)) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryOrange,
                    focusedLabelColor = primaryOrange,
                    unfocusedTextColor = onBackgroundColor,
                    focusedTextColor = onBackgroundColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(id = R.string.password)) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryOrange,
                    focusedLabelColor = primaryOrange,
                    unfocusedTextColor = onBackgroundColor,
                    focusedTextColor = onBackgroundColor
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, R.string.error_fill_all_fields, Toast.LENGTH_SHORT).show()
                    } else {
                        val isSuccess = viewModel.registerUser(name, email, password)
                        if (isSuccess) {
                            navController.navigate("login") { popUpTo("signup") { inclusive = true } }
                        } else {
                            Toast.makeText(context, R.string.error_email_registered, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(containerColor = darkBrown),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .graphicsLayer(scaleX = scale, scaleY = scale)
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up_btn),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.clickable { navController.navigate("login") }) {
                Text(
                    text = stringResource(id = R.string.already_have_account),
                    color = textSecondary
                )
                Text(
                    text = stringResource(id = R.string.login_btn),
                    color = primaryOrange,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
