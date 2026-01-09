package com.example.britishcoffee.screensimport

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.models.DataProvider // IMPORT MOHIM
import com.example.britishcoffee.viewmodels.OrderViewModel

val DetailBg = Color(0xFFF9F4EE)
val CoffeeBrown = Color(0xFFC67C4E)
val DarkBtn = Color(0xFF2F1B12)

@Composable
fun DetailScreen(navController: NavController, coffeeId: Int, viewModel: OrderViewModel) {
    val coffee = DataProvider.coffeeList.find { it.id == coffeeId } ?: return

    var quantity by remember { mutableIntStateOf(1) }
    var selectedSize by remember { mutableStateOf("M") }
    val context = LocalContext.current
    val isFavorite = viewModel.isFavorite(coffee)

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(modifier = Modifier.fillMaxWidth().height(380.dp)) {
            Image(
                painter = painterResource(id = coffee.imageRes),
                contentDescription = coffee.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(top = 40.dp, start = 20.dp).align(Alignment.TopStart)) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
            }
            IconButton(onClick = { viewModel.toggleFavorite(coffee) }, modifier = Modifier.padding(top = 40.dp, end = 20.dp).align(Alignment.TopEnd)) {
                Icon(if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "Like", tint = if (isFavorite) CoffeeBrown else Color.White)
            }
        }

        Box(modifier = Modifier.fillMaxSize().offset(y = (-20).dp).clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)).background(DetailBg)) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 20.dp).verticalScroll(rememberScrollState())) {
                Box(modifier = Modifier.width(40.dp).height(4.dp).clip(CircleShape).background(Color.LightGray).align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = coffee.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Coffee Size", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SizeOption(label = "Small", isSelected = selectedSize == "S") { selectedSize = "S" }
                    Spacer(modifier = Modifier.width(10.dp))
                    SizeOption(label = "Medium", isSelected = selectedSize == "M") { selectedSize = "M" }
                    Spacer(modifier = Modifier.width(10.dp))
                    SizeOption(label = "Large", isSelected = selectedSize == "L") { selectedSize = "L" }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Qty.", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(modifier = Modifier.border(1.dp, Color.Gray, RoundedCornerShape(20.dp)).padding(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp).clickable { if (quantity > 1) quantity-- })
                                Text("$quantity", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
                                Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp).clickable { quantity++ })
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${coffee.rating}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.Star, contentDescription = null, tint = CoffeeBrown, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Description", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = coffee.description, color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp, textAlign = TextAlign.Justify)
                Spacer(modifier = Modifier.height(100.dp))
            }
            Box(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(20.dp).height(80.dp).clip(RoundedCornerShape(30.dp)).background(DarkBtn)) {
                val totalPrice = coffee.price * quantity
                Row(
                    modifier = Modifier.fillMaxSize().clickable {
                        viewModel.addToCart(coffee, selectedSize, quantity)
                        Toast.makeText(context, "Added $quantity items to Cart!", Toast.LENGTH_SHORT).show()
                    }.padding(horizontal = 30.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Add to cart", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color.Gray))
                    Text("$${String.format("%.2f", totalPrice)}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun RowScope.SizeOption(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f).height(40.dp).clip(RoundedCornerShape(20.dp)).background(if (isSelected) Color(0xFFF8E8DC) else Color.White).border(1.dp, if (isSelected) CoffeeBrown else Color.LightGray, RoundedCornerShape(20.dp)).clickable { onClick() }) {
        Text(text = label, color = if (isSelected) CoffeeBrown else Color.Black, fontSize = 14.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}
