package com.example.britishcoffee.screens

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.R
import com.example.britishcoffee.viewmodels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController, totalAmount: Double, viewModel: OrderViewModel) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("Av. Mohammed V, Guéliz, Marrakech, Morocco") }

    var selectedPaymentMethod by remember { mutableStateOf("Cash") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showEditAddressDialog by remember { mutableStateOf(false) }
    var tempAddress by remember { mutableStateOf("") }

    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val darkBrown = colorResource(id = R.color.primary_dark_brown)

    Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 120.dp)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.clip(CircleShape).background(surfaceColor)
                ) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier.size(18.dp),
                        tint = onBackgroundColor
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Checkout",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = onBackgroundColor
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text("Recipient Details", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = onBackgroundColor)
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        placeholder = { Text("e.g. Ahmed Alami") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = primaryOrange) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryOrange,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = primaryOrange
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        placeholder = { Text("+212 6XX XXX XXX") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = primaryOrange) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryOrange,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = primaryOrange
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Delivery Address", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = onBackgroundColor)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = primaryOrange, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Default Address", fontWeight = FontWeight.Bold, color = onBackgroundColor)
                        Text(address, color = Color.Gray, fontSize = 13.sp)
                    }
                    IconButton(onClick = { tempAddress = address; showEditAddressDialog = true }) {
                        Icon(Icons.Default.Edit, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Payment Method", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = onBackgroundColor)
            Spacer(modifier = Modifier.height(12.dp))

            PaymentOptionItem("Cash on Delivery", Icons.Default.Payments, selectedPaymentMethod == "Cash") { selectedPaymentMethod = "Cash" }
            Spacer(modifier = Modifier.height(12.dp))
            PaymentOptionItem("Credit Card", Icons.Default.CreditCard, selectedPaymentMethod == "Card") { selectedPaymentMethod = "Card" }
        }

        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = surfaceColor,
            shadowElevation = 15.dp
        ) {
            Row(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Price", color = Color.Gray, fontSize = 14.sp)
                    Text("$${String.format("%.2f", totalAmount)}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = onBackgroundColor)
                }
                Button(
                    onClick = {
                        if(fullName.isNotBlank() && phoneNumber.isNotBlank()) {
                            viewModel.placeOrder(totalAmount)
                            showSuccessDialog = true
                        }
                    },
                    enabled = fullName.isNotBlank() && phoneNumber.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(56.dp).width(160.dp)
                ) {
                    Text("Order Now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { },
                containerColor = surfaceColor,
                shape = RoundedCornerShape(28.dp),
                icon = { Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(60.dp)) },
                title = { Text("Payment Successful", fontWeight = FontWeight.Bold, color = onBackgroundColor) },
                text = { Text("Thank you $fullName, your order has been placed successfully.", textAlign = TextAlign.Center, color = onBackgroundColor) },
                confirmButton = {
                    Button(
                        onClick = { showSuccessDialog = false; navController.navigate("orders") },
                        colors = ButtonDefaults.buttonColors(containerColor = darkBrown),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Track My Order") }
                }
            )
        }

        if (showEditAddressDialog) {
            AlertDialog(
                onDismissRequest = { showEditAddressDialog = false },
                containerColor = surfaceColor,
                title = { Text("Update Address", color = onBackgroundColor) },
                text = {
                    OutlinedTextField(
                        value = tempAddress,
                        onValueChange = { tempAddress = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = primaryOrange, focusedLabelColor = primaryOrange)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { address = tempAddress; showEditAddressDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryOrange)
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    }
}

@Composable
fun PaymentOptionItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val onSurface = MaterialTheme.colorScheme.onSurface
    val surface = MaterialTheme.colorScheme.surface

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, if (isSelected) primaryOrange else Color.Transparent, RoundedCornerShape(16.dp))
            .background(if (isSelected) primaryOrange.copy(alpha = 0.05f) else surface)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(if(isSelected) primaryOrange else Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = if(isSelected) Color.White else Color.Gray, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.weight(1f), color = onSurface)
        RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = primaryOrange))
    }
}
