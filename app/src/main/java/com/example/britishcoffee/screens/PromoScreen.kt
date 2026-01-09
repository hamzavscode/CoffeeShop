package com.example.britishcoffee.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.models.DataProvider
import com.example.britishcoffee.navigation.CoffeeDarkBrown
import com.example.britishcoffee.navigation.CoffeeOrange
import com.example.britishcoffee.viewmodels.OrderViewModel

@Composable
fun PromoScreen(navController: NavController, viewModel: OrderViewModel) {
    val promoCoffees = DataProvider.coffeeList.take(3)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F4EE))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF9E6445), CoffeeDarkBrown)
                    )
                )
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 40.dp, start = 20.dp).align(Alignment.TopStart)
            ) {
                Icon(Icons.Default.ArrowBackIosNew, "Back", tint = Color.White)
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Special Offer", color = CoffeeOrange, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.background(Color.White, RoundedCornerShape(20.dp)).padding(horizontal = 12.dp, vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("Buy 1 Get 1 Free!", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Text("Or get 50% Off on single items", color = Color.White.copy(0.8f), fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Applicable Drinks", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
        ) {
            items(promoCoffees) { coffee ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.height(240.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = coffee.imageRes), contentDescription = null,
                            contentScale = ContentScale.Crop, modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(coffee.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("With Promo", color = CoffeeOrange, fontSize = 12.sp)

                        Spacer(modifier = Modifier.weight(1f))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("$${coffee.price}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("50% OFF", color = Color.Red, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    viewModel.addToCart(coffee, "M", 1, isPromo = true)
                                    Toast.makeText(context, "Promo Applied!", Toast.LENGTH_SHORT).show()
                                },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.height(30.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CoffeeOrange)
                            ) {
                                Text("Get Offer", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
