package com.example.britishcoffee.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.R
import com.example.britishcoffee.models.DataProvider
import com.example.britishcoffee.viewmodels.OrderViewModel

@Composable
fun DetailScreen(navController: NavController, coffeeId: Int, viewModel: OrderViewModel) {
    val coffee = DataProvider.coffeeList.find { it.id == coffeeId } ?: return

    var quantity by remember { mutableIntStateOf(1) }
    var selectedSize by remember { mutableStateOf("M") }

    val context = LocalContext.current
    val isFavorite = viewModel.isFavorite(coffee)

    val cartInteraction = remember { MutableInteractionSource() }
    val isCartPressed by cartInteraction.collectIsPressedAsState()
    val cartScale by animateFloatAsState(if (isCartPressed) 0.95f else 1f, label = "")

    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val darkBrown = colorResource(id = R.color.primary_dark_brown)

    Column(modifier = Modifier.fillMaxSize().background(backgroundColor)) {

        Box(modifier = Modifier.fillMaxWidth().height(380.dp)) {
            Image(
                painter = painterResource(id = coffee.imageRes),
                contentDescription = coffee.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Black.copy(0.3f))
                ) {
                    Icon(Icons.Default.ArrowBackIosNew, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                IconButton(
                    onClick = { viewModel.toggleFavorite(coffee) },
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Black.copy(0.3f))
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) primaryOrange else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-30).dp)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(modifier = Modifier.width(40.dp).height(4.dp).clip(CircleShape).background(Color.LightGray).align(Alignment.CenterHorizontally))

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = coffee.name, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = onBackgroundColor)
                        Text(text = coffee.shortDesc, fontSize = 14.sp, color = colorResource(id = R.color.text_secondary))
                    }
                    Surface(
                        color = surfaceColor,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Icon(Icons.Default.Star, null, tint = colorResource(id = R.color.star_rating), modifier = Modifier.size(18.dp))
                            Text(" ${coffee.rating}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = onSurfaceColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(id = R.string.coffee_size), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = onBackgroundColor)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(surfaceColor).padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        IconButton(onClick = { if (quantity > 1) quantity-- }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Remove, null, tint = onSurfaceColor)
                        }
                        Text(text = quantity.toString(), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp), color = onSurfaceColor)
                        IconButton(onClick = { quantity++ }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Add, null, tint = onSurfaceColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("S", "M", "L").forEach { size ->
                        SizeOptionItem(size, selectedSize == size, Modifier.weight(1f)) { selectedSize = size }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(stringResource(id = R.string.description_label), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = onBackgroundColor)
                Text(
                    text = coffee.description,
                    color = colorResource(id = R.color.text_secondary),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(120.dp))
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(75.dp)
                    .graphicsLayer(scaleX = cartScale, scaleY = cartScale),
                shape = RoundedCornerShape(24.dp),
                color = darkBrown,
                shadowElevation = 10.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(stringResource(id = R.string.total_price), color = Color.LightGray, fontSize = 12.sp)
                        Text(
                            text = "$${String.format("%.2f", coffee.price * quantity)}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Button(
                        onClick = {
                            viewModel.addToCart(coffee, selectedSize, quantity)
                            Toast.makeText(context, "$quantity ${coffee.name} added", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryOrange),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.height(48.dp),
                        interactionSource = cartInteraction
                    ) {
                        Text(stringResource(id = R.string.add_to_cart), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun SizeOptionItem(label: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val onSurface = MaterialTheme.colorScheme.onSurface
    val surface = MaterialTheme.colorScheme.surface

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(45.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) primaryOrange.copy(alpha = 0.1f) else surface)
            .border(
                width = 1.dp,
                color = if (isSelected) primaryOrange else Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (isSelected) primaryOrange else onSurface,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
