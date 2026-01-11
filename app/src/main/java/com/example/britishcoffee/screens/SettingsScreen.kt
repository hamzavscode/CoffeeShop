package com.example.britishcoffee.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.britishcoffee.R
import com.example.britishcoffee.viewmodels.OrderViewModel

@Composable
fun SettingsScreen(navController: NavController, viewModel: OrderViewModel) {
    var showThemeDialog by remember { mutableStateOf(false) }

    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val textSecondary = colorResource(id = R.color.text_secondary)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.clip(CircleShape).background(surfaceColor)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = stringResource(id = R.string.back_label),
                    modifier = Modifier.size(18.dp),
                    tint = onSurfaceColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = R.string.settings),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = onBackgroundColor
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = stringResource(id = R.string.general_section),
                color = textSecondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            SettingCard(
                icon = Icons.Default.Person,
                title = stringResource(id = R.string.edit_profile),
                subtitle = stringResource(id = R.string.edit_profile_sub)
            ) {
                navController.navigate("profile")
            }

            SettingCard(
                icon = Icons.Default.Palette,
                title = stringResource(id = R.string.appearance),
                subtitle = stringResource(id = R.string.appearance_sub)
            ) {
                showThemeDialog = true
            }

            SettingCard(
                icon = Icons.Default.Notifications,
                title = stringResource(id = R.string.notifications),
                subtitle = stringResource(id = R.string.notifications_sub)
            ) { }

            SettingCard(
                icon = Icons.Default.Language,
                title = stringResource(id = R.string.language),
                subtitle = stringResource(id = R.string.language_sub)
            ) { }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.support_section),
                color = textSecondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp)
            )
            SettingCard(
                icon = Icons.Default.Info,
                title = stringResource(id = R.string.about_us),
                subtitle = stringResource(id = R.string.version_info)
            ) { }
        }
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text(stringResource(id = R.string.choose_appearance), fontWeight = FontWeight.Bold, color = onSurfaceColor) },
            text = {
                Column {
                    ThemeOption(stringResource(id = R.string.light_mode), Icons.Default.LightMode) {
                        viewModel.setAppTheme(false)
                        showThemeDialog = false
                    }
                    ThemeOption(stringResource(id = R.string.dark_mode), Icons.Default.DarkMode) {
                        viewModel.setAppTheme(true)
                        showThemeDialog = false
                    }
                    ThemeOption(stringResource(id = R.string.system_default), Icons.Default.SettingsSuggest) {
                        viewModel.setAppTheme(null)
                        showThemeDialog = false
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text(stringResource(id = R.string.cancel), color = primaryOrange)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = surfaceColor
        )
    }
}

@Composable
fun SettingCard(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    val primaryOrange = colorResource(id = R.color.primary_orange)
    val textSecondary = colorResource(id = R.color.text_secondary)

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(primaryOrange.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = primaryOrange, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(subtitle, fontSize = 12.sp, color = textSecondary)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

@Composable
fun ThemeOption(text: String, icon: ImageVector, onClick: () -> Unit) {
    val primaryOrange = colorResource(id = R.color.primary_orange)
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = primaryOrange)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}
