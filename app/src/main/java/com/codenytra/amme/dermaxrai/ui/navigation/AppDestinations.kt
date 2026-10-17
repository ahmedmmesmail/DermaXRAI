package com.codenytra.amme.dermaxrai.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    HOME("Home", Icons.Outlined.Home, Icons.Rounded.Home),
    SKIN("Skin", Icons.Outlined.Spa, Icons.Rounded.Spa),
}