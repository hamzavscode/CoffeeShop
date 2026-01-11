package com.example.britishcoffee.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.R
import com.example.britishcoffee.models.CartItem
import com.example.britishcoffee.viewmodels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: OrderViewModel) {
    val cartItems = viewModel.cartItems

    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val darkBrown = colorResource(id = R.color.primary_dark_brown)
    val textSecondary = colorResource(id = R.color.text_secondary)

    Column(modifier = Modifier.fillMaxSize().background(backgroundColor).padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBackIosNew,
                    contentDescription = stringResource(id = R.string.back_label),
                    modifier = Modifier.size(20.dp),
                    tint = onBackgroundColor
                )
            }
            Text(
                text = stringResource(id = R.string.back_label),
                fontSize = 16.sp,
                color = onBackgroundColor,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.cart),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = onBackgroundColor
            )
            Spacer(modifier = Modifier.weight(1.2f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(items = cartItems, key = { "${it.coffee.id}-${it.size}-${it.isPromo}" }) { item ->
                val dismissState = rememberDismissState(
                    confirmValueChange = {
                        if (it == DismissValue.DismissedToStart) {
                            viewModel.removeFromCart(item)
                            true
                        } else false
                    }
                )
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    background = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFF3B30))
                                .padding(end = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(Icons.Default.Delete, "Delete", tint = Color.White)
                        }
                    },
                    dismissContent = { CartItemCard(item, viewModel) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(surfaceColor)
                .padding(24.dp)
        ) {
            val subtotal = viewModel.calculateTotal()
            val delivery = 10.0
            val tax = 5.0
            val total = subtotal + delivery + tax

            RowSummary("Subtotal", subtotal, onSurfaceColor)
            Spacer(modifier = Modifier.height(12.dp))
            RowSummary("Fee Delivery", delivery, onSurfaceColor)
            Spacer(modifier = Modifier.height(12.dp))
            RowSummary("Total Tax", tax, onSurfaceColor)
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = onSurfaceColor)
                Text("$${String.format("%.2f", total)}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = onSurfaceColor)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("checkout/${total.toFloat()}") },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = darkBrown),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Proceed to Checkout", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, viewModel: OrderViewModel) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val textSecondary = colorResource(id = R.color.text_secondary)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth().height(100.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = item.coffee.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.coffee.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = onSurfaceColor)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Size: ${item.size}", color = textSecondary, fontSize = 12.sp)
                    if (item.isPromo) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("PROMO", color = Color.Red, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                val displayPrice = if (item.isPromo) item.coffee.price * 0.5 else item.coffee.price
                Text("$${String.format("%.2f", displayPrice)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = onSurfaceColor)
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .border(1.dp, primaryOrange, CircleShape)
                        .clickable { viewModel.decrementQuantity(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Remove, "", tint = primaryOrange, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("${item.quantity.value}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = onSurfaceColor)
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(primaryOrange)
                        .clickable { viewModel.incrementQuantity(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, "", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun RowSummary(label: String, price: Double, textColor: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = FontWeight.Medium, color = textColor)
        Text("$${String.format("%.2f", price)}", fontWeight = FontWeight.Bold, color = textColor)
    }
}
