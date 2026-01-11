package com.example.britishcoffee.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.R
import com.example.britishcoffee.models.CoffeeItem
import com.example.britishcoffee.models.DataProvider
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Cappuccino") }

    val coffeeList = DataProvider.coffeeList
    val categories = listOf("Espresso", "Cappuccino", "Americano", "Cortado", "Flat White", "Café Crème", "Turkish Coffee", "Iced Latte", "Macchiato", "Pumpkin Latte")

    val searchResults = coffeeList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    val backgroundColor = MaterialTheme.colorScheme.background
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                    text = stringResource(id = R.string.welcome_to),
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.text_secondary),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = stringResource(id = R.string.coffee_shop),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = onBackgroundColor,
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
                placeholder = { Text(stringResource(id = R.string.search_hint), color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = onSurfaceColor) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = onSurfaceColor,
                    unfocusedTextColor = onSurfaceColor
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            val filterInteraction = remember { MutableInteractionSource() }
            val isFilterPressed by filterInteraction.collectIsPressedAsState()
            val filterScale by animateFloatAsState(if (isFilterPressed) 0.9f else 1f, label = "")

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .graphicsLayer(scaleX = filterScale, scaleY = filterScale)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(id = R.color.primary_dark_brown))
                    .clickable(interactionSource = filterInteraction, indication = null) {
                        navController.navigate("settings")
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Tune, "Filter", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (searchQuery.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.search_results),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = onBackgroundColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (searchResults.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_results),
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                Box(modifier = Modifier.height(600.dp)) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(searchResults) { index, coffee ->
                            EntranceAnimation(index = index) {
                                CoffeeCardItem(coffee = coffee) { navController.navigate("detail/${coffee.id}") }
                            }
                        }
                    }
                }
            }
        } else {
            PromoBanner(navController)

            Spacer(modifier = Modifier.height(24.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(categories) { index, categoryName ->
                    EntranceAnimation(index = index) {
                        val isSelected = categoryName == selectedCategory
                        Button(
                            onClick = {
                                selectedCategory = categoryName
                                val coffeeToOpen = coffeeList.find { it.name.contains(categoryName, ignoreCase = true) }
                                coffeeToOpen?.let {
                                    navController.navigate("detail/${it.id}")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) colorResource(id = R.color.primary_dark_brown) else surfaceColor,
                                contentColor = if (isSelected) Color.White else onSurfaceColor
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(categoryName)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.special_offers),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = onBackgroundColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(coffeeList) { index, coffee ->
                    EntranceAnimation(index = index) {
                        NewStyleCoffeeCard(coffee = coffee) { navController.navigate("detail/${coffee.id}") }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.popular_coffees),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = onBackgroundColor
                )
                Text(
                    text = stringResource(id = R.string.see_all),
                    color = colorResource(id = R.color.primary_orange),
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
                    itemsIndexed(coffeeList) { index, coffee ->
                        EntranceAnimation(index = index) {
                            CoffeeCardItem(coffee = coffee) {
                                navController.navigate("detail/${coffee.id}")
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun EntranceAnimation(index: Int, content: @Composable () -> Unit) {
    val animationState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }
    AnimatedVisibility(
        visibleState = animationState,
        enter = slideInVertically(
            initialOffsetY = { 40 },
            animationSpec = tween(durationMillis = 500, delayMillis = index * 80)
        ) + fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = index * 80))
    ) {
        content()
    }
}

@Composable
fun PromoBanner(navController: NavController) {
    val promoCoffees = remember { DataProvider.coffeeList.take(3) }
    var currentIdx by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIdx = (currentIdx + 1) % promoCoffees.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable { navController.navigate("promo") }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF4E342E), Color(0xFF21130D))
                    )
                )
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = 200f,
                center = androidx.compose.ui.geometry.Offset(size.width * 0.9f, size.height * 0.2f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.2f)) {
                Surface(
                    color = colorResource(id = R.color.primary_orange).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.limited_offer),
                        color = colorResource(id = R.color.primary_orange),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.promo_header),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("promo") },
                    modifier = Modifier
                        .height(45.dp)
                        .wrapContentWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.primary_orange),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.order_now),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = promoCoffees[currentIdx].imageRes,
                    transitionSpec = {
                        fadeIn(tween(800)) + scaleIn(initialScale = 0.8f) togetherWith fadeOut(tween(800))
                    },
                    label = "PromoImage"
                ) { targetImage ->
                    Image(
                        painter = painterResource(id = targetImage),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun NewStyleCoffeeCard(coffee: CoffeeItem, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "")

    Box(modifier = Modifier
        .width(160.dp)
        .height(260.dp)
        .graphicsLayer(scaleX = scale, scaleY = scale)
        .clickable(interactionSource = interactionSource, indication = null) { onClick() }
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp, start = 12.dp, end = 12.dp, bottom = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = coffee.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, color = MaterialTheme.colorScheme.onSurface)
                Text(text = coffee.shortDesc, color = Color.Gray, fontSize = 12.sp, maxLines = 1)
                Spacer(modifier = Modifier.weight(1f))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$${coffee.price}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colorResource(id = R.color.primary_orange))
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(colorResource(id = R.color.primary_dark_brown)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = coffee.imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .align(Alignment.TopCenter)
        )
    }
}

@Composable
fun CoffeeCardItem(coffee: CoffeeItem, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f, label = "")

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(if (isPressed) 1.dp else 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .height(110.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = coffee.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = colorResource(id = R.color.star_rating), modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${coffee.rating}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(coffee.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, color = MaterialTheme.colorScheme.onSurface)
            Text(coffee.shortDesc, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("$${coffee.price}0", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorResource(id = R.color.primary_dark_brown)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, "", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
