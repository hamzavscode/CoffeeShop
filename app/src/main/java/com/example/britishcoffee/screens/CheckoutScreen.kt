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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.navigation.CoffeeDarkBrown
import com.example.britishcoffee.navigation.CoffeeOrange
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

    val lightBeige = Color(0xFFF9F4EE)

    Box(modifier = Modifier.fillMaxSize().background(lightBeige)) {
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
                    modifier = Modifier.clip(CircleShape).background(Color.White)
                ) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Checkout", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = CoffeeDarkBrown)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text("Recipient Details", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoffeeDarkBrown)
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
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
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = CoffeeOrange) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = CoffeeOrange,
                            unfocusedBorderColor = Color.LightGray
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
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = CoffeeOrange) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = CoffeeOrange,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Delivery Address", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoffeeDarkBrown)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = CoffeeOrange, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Default Address", fontWeight = FontWeight.Bold)
                        Text(address, color = Color.Gray, fontSize = 13.sp)
                    }
                    IconButton(onClick = { tempAddress = address; showEditAddressDialog = true }) {
                        Icon(Icons.Default.Edit, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Payment Method", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoffeeDarkBrown)
            Spacer(modifier = Modifier.height(12.dp))

            PaymentOptionItem("Cash on Delivery", Icons.Default.Payments, selectedPaymentMethod == "Cash") { selectedPaymentMethod = "Cash" }
            Spacer(modifier = Modifier.height(12.dp))
            PaymentOptionItem("Credit Card", Icons.Default.CreditCard, selectedPaymentMethod == "Card") { selectedPaymentMethod = "Card" }
        }

        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White,
            shadowElevation = 15.dp
        ) {
            Row(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Price", color = Color.Gray, fontSize = 14.sp)
                    Text("$${String.format("%.2f", totalAmount)}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = CoffeeDarkBrown)
                }
                Button(
                    onClick = {
                        if(fullName.isNotBlank() && phoneNumber.isNotBlank()) {
                            viewModel.placeOrder(totalAmount)
                            showSuccessDialog = true
                        }
                    },
                    enabled = fullName.isNotBlank() && phoneNumber.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeOrange),
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
                containerColor = Color.White,
                shape = RoundedCornerShape(28.dp),
                icon = { Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(60.dp)) },
                title = { Text("Payment Successful", fontWeight = FontWeight.Bold) },
                text = { Text("Thank you $fullName, your order has been placed successfully.", textAlign = TextAlign.Center) },
                confirmButton = {
                    Button(
                        onClick = { showSuccessDialog = false; navController.navigate("orders") },
                        colors = ButtonDefaults.buttonColors(containerColor = CoffeeDarkBrown),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Track My Order") }
                }
            )
        }

        // --- Edit Address Dialog ---
        if (showEditAddressDialog) {
            AlertDialog(
                onDismissRequest = { showEditAddressDialog = false },
                containerColor = Color.White,
                title = { Text("Update Address") },
                text = { OutlinedTextField(value = tempAddress, onValueChange = { tempAddress = it }, modifier = Modifier.fillMaxWidth()) },
                confirmButton = {
                    Button(onClick = { address = tempAddress; showEditAddressDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = CoffeeOrange)) {
                        Text("Save")
                    }
                }
            )
        }
    }
}

@Composable
fun PaymentOptionItem(label: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, if (isSelected) CoffeeOrange else Color.Transparent, RoundedCornerShape(16.dp))
            .background(if (isSelected) CoffeeOrange.copy(alpha = 0.05f) else Color.White)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(if(isSelected) CoffeeOrange else Color(0xFFF5F5F5), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = if(isSelected) Color.White else Color.Gray, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.weight(1f), color = CoffeeDarkBrown)
        RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = CoffeeOrange))
    }
}
