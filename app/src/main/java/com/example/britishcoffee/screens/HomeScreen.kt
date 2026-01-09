package com.example.britishcoffee.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.R
import com.example.britishcoffee.models.CoffeeItem
import com.example.britishcoffee.models.DataProvider
import com.example.britishcoffee.navigation.CoffeeOrange

val BeigeBackground = Color(0xFFF9F4EE)
val CoffeeDarkBrown = Color(0xFF3C2318)
val SearchBarColor = Color(0xFFFFFFFF)

@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Cappuccino") }

    val coffeeList = DataProvider.coffeeList
    val categories = listOf("Espresso", "Cappuccino", "Americano", "Cortado","Flat White","Café Crème","Turkish Coffee","Iced Latte","Macchiato","Pumpkin Latte" )

    val searchResults = coffeeList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeBackground)
            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Welcome to",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Coffee Shop",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CoffeeDarkBrown,
                    letterSpacing = 1.sp
                )
            }


        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search your coffee...", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, "Search", tint = Color.Black) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SearchBarColor,
                    unfocusedContainerColor = SearchBarColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).height(52.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CoffeeDarkBrown)
                    .clickable { navController.navigate("settings") },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Tune, "Filter", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (searchQuery.isNotEmpty()) {
            Text(text = "Search Results", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
            if (searchResults.isEmpty()) {
                Text("No coffee found.", color = Color.Gray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            } else {
                Box(modifier = Modifier.height(600.dp)) {
                    LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(searchResults) { coffee ->
                            CoffeeCardItem(coffee = coffee) { navController.navigate("detail/${coffee.id}") }
                        }
                    }
                }
            }
        } else {
            PromoBanner(navController)
            Spacer(modifier = Modifier.height(24.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(categories.size) { index ->
                    val categoryName = categories[index]
                    val isSelected = categoryName == selectedCategory
                    Button(
                        onClick = {
                            selectedCategory = categoryName
                            val coffeeToOpen = DataProvider.coffeeList.find { it.name.contains(categoryName, ignoreCase = true) }
                            coffeeToOpen?.let { navController.navigate("detail/${it.id}") }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) CoffeeDarkBrown else Color.White,
                            contentColor = if (isSelected) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(categoryName)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Special Offers", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(coffeeList) { coffee ->
                    NewStyleCoffeeCard(coffee = coffee) { navController.navigate("detail/${coffee.id}") }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Popular Coffees", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "See all",
                    color = CoffeeOrange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("menu") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.height(550.dp)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(coffeeList) { coffee ->
                        CoffeeCardItem(coffee = coffee) {
                            navController.navigate("detail/${coffee.id}")
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun NewStyleCoffeeCard(coffee: CoffeeItem, onClick: () -> Unit) {
    Box(modifier = Modifier.width(160.dp).height(260.dp).clickable { onClick() }) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().height(210.dp).align(Alignment.BottomCenter)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(top = 60.dp, start = 12.dp, end = 12.dp, bottom = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = coffee.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Text(text = coffee.shortDesc, color = Color.Gray, fontSize = 12.sp, maxLines = 1)
                Spacer(modifier = Modifier.weight(1f))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$${coffee.price}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(CoffeeDarkBrown), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
        Image(painter = painterResource(id = coffee.imageRes), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(100.dp).clip(CircleShape).align(Alignment.TopCenter))
    }
}

@Composable
fun CoffeeCardItem(coffee: CoffeeItem, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.height(110.dp).fillMaxWidth()) {
                Image(painter = painterResource(id = coffee.imageRes), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)))
                Box(modifier = Modifier.padding(6.dp).background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(horizontal = 6.dp, vertical = 2.dp).align(Alignment.TopStart)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = Color.Yellow, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${coffee.rating}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(coffee.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(coffee.shortDesc, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("$${coffee.price}0", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Box(modifier = Modifier.size(30.dp).clip(RoundedCornerShape(8.dp)).background(CoffeeDarkBrown), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Add, "", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun PromoBanner(navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(24.dp)).clickable { navController.navigate("promo") }) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color(0xFF9E6445), Color(0xFF5E3928)))))
        Row(modifier = Modifier.fillMaxSize().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Promo", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("Buy one, Get one\nfor Free", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Button(onClick = { navController.navigate("promo") }, colors = ButtonDefaults.buttonColors(containerColor = CoffeeOrange), shape = RoundedCornerShape(10.dp)) {
                    Text("Order Now", color = Color.White)
                }
            }
            Image(painter = painterResource(id = R.drawable.cappoccino), contentDescription = null, modifier = Modifier.size(110.dp))
        }
    }
}
