package com.codenytra.amme.dermaxrai.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FaceRetouchingNatural
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun CradleBottomBar(
    currentDestination: AppDestinations,
    onDestinationClick: (AppDestinations) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    cradleMargin: Dp = 8.dp
) {
    val fabSize = 56.dp
    val barHeight = 80.dp

    val cradleShape = remember(cradleMargin) {
        FabCradleShape(cradleRadius = fabSize / 2 + cradleMargin)
    }

    val destinations = AppDestinations.entries
    val half = destinations.size / 2

    Box(modifier = modifier.fillMaxWidth()) {

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = fabSize / 2) // نص الـ FAB اللي طالع فوق البار
                .fillMaxWidth(),
            shape = cradleShape,
            color = MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                    .height(barHeight),
                verticalAlignment = Alignment.CenterVertically
            ) {
                destinations.take(half).forEach { destination ->
                    BarItem(destination, destination == currentDestination, onDestinationClick)
                }

                // مكان الـ FAB في النص
                Spacer(Modifier.width(fabSize + cradleMargin * 2))

                destinations.drop(half).forEach { destination ->
                    BarItem(destination, destination == currentDestination, onDestinationClick)
                }
            }
        }

        FloatingActionButton(
            onClick = onFabClick,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.TopCenter)
                .padding(bottom = barHeight - 8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.FaceRetouchingNatural,
                contentDescription = "Scan"
            )
        }
    }
}

@Composable
private fun RowScope.BarItem(
    destination: AppDestinations,
    selected: Boolean,
    onClick: (AppDestinations) -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = { onClick(destination) },
        icon = {
            Icon(
                imageVector = if (selected) destination.selectedIcon else destination.icon,
                contentDescription = destination.label
            )
        },
        label = { Text(destination.label) }
    )
}

private class FabCradleShape(
    private val cradleRadius: Dp
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val radius = with(density) { cradleRadius.toPx() }
        val centerX = size.width / 2f

        val bar = Path().apply { addRect(Rect(0f, 0f, size.width, size.height)) }
        val cutout = Path().apply { addOval(Rect(Offset(centerX, 0f), radius)) }

        return Outline.Generic(Path.combine(PathOperation.Difference, bar, cutout))
    }
}