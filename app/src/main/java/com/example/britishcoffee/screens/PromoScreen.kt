package com.example.britishcoffee.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.R
import com.example.britishcoffee.models.CoffeeItem
import com.example.britishcoffee.models.DataProvider
import com.example.britishcoffee.viewmodels.OrderViewModel

@Composable
fun PromoScreen(navController: NavController, viewModel: OrderViewModel) {
    val promoCoffees = DataProvider.coffeeList.take(3)
    val context = LocalContext.current

    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val darkBrown = colorResource(id = R.color.primary_dark_brown)

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(colorResource(id = R.color.primary_orange).copy(alpha = 0.8f), darkBrown)
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 50.dp, start = 20.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowBackIosNew, stringResource(id = R.string.back_label), tint = Color.White, modifier = Modifier.size(18.dp))
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(1000)) + expandVertically()
                ) {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.limited_offer),
                            color = primaryOrange,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "UP TO 50% OFF",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = "ON YOUR FAVORITE COFFEE",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Applicable Drinks",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = onBackgroundColor,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
        ) {
            itemsIndexed(promoCoffees) { index, coffee ->
                val state = remember { MutableTransitionState(false) }.apply { targetState = true }
                AnimatedVisibility(
                    visibleState = state,
                    enter = slideInVertically(
                        animationSpec = tween(durationMillis = 500, delayMillis = index * 100),
                        initialOffsetY = { it / 2 }
                    ) + fadeIn(animationSpec = tween(500, index * 100))
                ) {
                    PromoCard(coffee, viewModel, context)
                }
            }
        }
    }
}

@Composable
fun PromoCard(coffee: CoffeeItem, viewModel: OrderViewModel, context: android.content.Context) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.94f else 1f, label = "buttonScale")

    val onSurface = MaterialTheme.colorScheme.onSurface
    val surface = MaterialTheme.colorScheme.surface
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val darkBrown = colorResource(id = R.color.primary_dark_brown)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = surface),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                Image(
                    painter = painterResource(id = coffee.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(18.dp))
                )

                Surface(
                    color = Color.Red,
                    shape = RoundedCornerShape(bottomStart = 12.dp, topEnd = 12.dp)
                ) {
                    Text(
                        text = "50% OFF",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = coffee.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = onSurface,
                maxLines = 1
            )

            Text(
                text = "Promo Applied",
                color = primaryOrange,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "$${String.format("%.2f", coffee.price * 0.5)}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = primaryOrange
                    )
                    Text(
                        text = "$${String.format("%.2f", coffee.price)}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        style = TextStyle(textDecoration = TextDecoration.LineThrough)
                    )
                }

                Button(
                    onClick = {
                        viewModel.addToCart(coffee, "M", 1, isPromo = true)
                        Toast.makeText(context, "Promo Applied!", Toast.LENGTH_SHORT).show()
                    },
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .width(75.dp)
                        .graphicsLayer(scaleX = scale, scaleY = scale),
                    colors = ButtonDefaults.buttonColors(containerColor = darkBrown),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(stringResource(id = R.string.order_now), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
