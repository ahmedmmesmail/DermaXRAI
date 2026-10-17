package com.codenytra.amme.dermaxrai.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.features.home.components.*

enum class RoutineType { MORNING, EVENING }

// ───────────────────────── Screen ─────────────────────────

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onScanClick: () -> Unit = {},
    onDetailsClick: () -> Unit = {},
    onRoutineClick: (RoutineType) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = contentPadding.calculateBottomPadding() + 12.dp,)
    ) {
        WelcomeHeader()
        Spacer(Modifier.height(24.dp))
        ScanHeroCard(onScanClick = onScanClick)
        Spacer(Modifier.height(26.dp))
        LastScanSection(onDetailsClick = onDetailsClick)
        Spacer(Modifier.height(26.dp))
        QuickRoutineSection(onRoutineClick = onRoutineClick)
        Spacer(Modifier.height(24.dp))
        HydrationTipCard()
        Spacer(Modifier.height(24.dp))
        DisclaimerPill()
    }
}