package com.example.britishcoffee.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.example.britishcoffee.models.DataProvider
import com.example.britishcoffee.models.Order
import com.example.britishcoffee.viewmodels.OrderViewModel

@Composable
fun MyOrderScreen(navController: NavController, viewModel: OrderViewModel) {
    val realOrders = viewModel.orders

    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val textSecondary = colorResource(id = R.color.text_secondary)

    val mockCompletedOrders = remember {
        listOf(
            Order(
                id = "BC-8821",
                items = listOf(CartItem(DataProvider.coffeeList[0], mutableIntStateOf(1), "M")),
                totalAmount = 4.20,
                date = "24 Dec, 09:20 AM",
                status = "Delivered"
            ),
            Order(
                id = "BC-8825",
                items = listOf(CartItem(DataProvider.coffeeList[7], mutableIntStateOf(1), "L")),
                totalAmount = 4.50,
                date = "22 Dec, 04:15 PM",
                status = "Delivered"
            )
        )
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val activeOrders = realOrders.filter { it.status == "Processing" }
    val completedOrders = realOrders.filter { it.status == "Delivered" } + mockCompletedOrders

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(surfaceColor)
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = stringResource(id = R.string.back_label),
                    modifier = Modifier.size(18.dp),
                    tint = onSurfaceColor
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.my_order),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = onBackgroundColor
            )
            Spacer(modifier = Modifier.weight(1.2f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(surfaceColor.copy(alpha = 0.5f))
                .border(1.dp, Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                .padding(4.dp)
        ) {
            TabItem(
                text = stringResource(id = R.string.tab_active),
                isSelected = selectedTab == 0,
                modifier = Modifier.weight(1f)
            ) { selectedTab = 0 }

            TabItem(
                text = stringResource(id = R.string.tab_completed),
                isSelected = selectedTab == 1,
                modifier = Modifier.weight(1f)
            ) { selectedTab = 1 }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val listToShow = if (selectedTab == 0) activeOrders else completedOrders

        if (listToShow.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.coffee_placeholder),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = textSecondary.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.no_orders),
                        color = textSecondary,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 80.dp)) {
                items(listToShow) { order ->
                    val firstItem = order.items.firstOrNull()
                    val coffeeName = if (order.items.size > 1) {
                        "${firstItem?.coffee?.name} + ${order.items.size - 1} more"
                    } else {
                        firstItem?.coffee?.name ?: "Coffee Order"
                    }

                    OrderCardItem(
                        name = coffeeName,
                        price = order.totalAmount,
                        date = order.date,
                        status = order.status,
                        imageRes = firstItem?.coffee?.imageRes ?: R.drawable.coffee_placeholder,
                        orderId = order.id
                    )
                }
            }
        }
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val primaryOrange = colorResource(id = R.color.primary_orange)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) primaryOrange else Color.Transparent)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.Gray,
            fontSize = 15.sp
        )
    }
}

@Composable
fun OrderCardItem(name: String, price: Double, date: String, status: String, imageRes: Int, orderId: String) {
    val onSurface = MaterialTheme.colorScheme.onSurface
    val surface = MaterialTheme.colorScheme.surface
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val textSecondary = colorResource(id = R.color.text_secondary)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = onSurface)
                Text(text = "Order ID: $orderId", color = textSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = date, color = textSecondary, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(8.dp))

                val statusColor = if (status == "Delivered") Color(0xFF4CAF50) else Color(0xFFFF9800)

                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = status, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "$${String.format("%.2f", price)}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = primaryOrange
            )
        }
    }
}
