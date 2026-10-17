package com.codenytra.amme.dermaxrai.features.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Grain
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.R
import com.codenytra.amme.dermaxrai.ui.Dot
import com.codenytra.amme.dermaxrai.ui.IconCircle
import com.codenytra.amme.dermaxrai.ui.Panel
import com.codenytra.amme.dermaxrai.ui.dermaType
import com.codenytra.amme.dermaxrai.ui.theme.derma


enum class IndicatorStyle { ACNE, PIGMENT, DRYNESS }

data class SkinIndicator(
    @StringRes val name: Int,
    val percent: Int,
    val style: IndicatorStyle
)

val sampleIndicators = listOf(
    SkinIndicator(R.string.home_indicator_acne, 78, IndicatorStyle.ACNE),
    SkinIndicator(R.string.home_indicator_pigmentation, 52, IndicatorStyle.PIGMENT),
    SkinIndicator(R.string.home_indicator_dryness, 34, IndicatorStyle.DRYNESS)
)

@Composable
fun LastScanSection(onDetailsClick: () -> Unit) {
    val derma = MaterialTheme.derma
    val onSurface = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    Column {
        SectionHeader(
            title = R.string.home_last_scan_title,
            icon = Icons.Rounded.BarChart
        ) {
            Panel(shape = CircleShape, color = derma.chipSoft) {
                Text(
                    text = stringResource(R.string.home_last_scan_time),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = dermaType(11, 16),
                    color = onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Panel(
            shape = RoundedCornerShape(32.dp),
            color = derma.card,
            withShadow = true
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // النمط التشخيصي + الحالة
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.home_skin_type_label),
                            style = dermaType(12, 16),
                            color = onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.home_skin_type_value),
                            style = dermaType(22, 30, FontWeight.Bold),
                            color = derma.heading
                        )
                    }
                    Panel(shape = CircleShape, color = derma.chipStrong) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Dot(color = derma.accentText)
                            Text(
                                text = stringResource(R.string.home_skin_status),
                                style = dermaType(12, 16, FontWeight.Medium),
                                color = derma.heading
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))
                ActiveZonesBox()
                Spacer(Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.home_indicators_title),
                    style = dermaType(14, 20, FontWeight.Medium),
                    color = onSurface
                )
                Spacer(Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sampleIndicators.forEach { IndicatorRow(it) }
                }

                Spacer(Modifier.height(28.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(role = Role.Button, onClick = onDetailsClick),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.home_view_details),
                        style = dermaType(15, 22, FontWeight.Bold),
                        color = derma.heading
                    )
                    IconCircle(
                        icon = Icons.AutoMirrored.Rounded.ArrowForward,
                        size = 32.dp,
                        container = derma.circleSoft,
                        tint = derma.heading,
                        iconSize = 18.dp
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveZonesBox() {
    val derma = MaterialTheme.derma
    val onSurface = MaterialTheme.colorScheme.onSurface

    Panel(
        shape = RoundedCornerShape(2.dp),
        color = derma.zoneBox,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconCircle(
                icon = Icons.Rounded.Face,
                size = 44.dp,
                container = derma.circleStrong,
                tint = derma.iconTint
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.home_zones_title),
                    style = dermaType(14, 20, FontWeight.Medium),
                    color = onSurface
                )
                Text(
                    text = stringResource(R.string.home_zones_subtitle),
                    style = dermaType(12, 17),
                    color = derma.bodyText
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.home_zones_map),
                    style = dermaType(11, 14, FontWeight.Medium),
                    color = derma.iconTint
                )
                Icon(
                    imageVector = Icons.Rounded.Grain,
                    contentDescription = null,
                    tint = derma.iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun IndicatorRow(indicator: SkinIndicator) {
    val derma = MaterialTheme.derma
    val onSurface = MaterialTheme.colorScheme.onSurface

    val brush = when (indicator.style) {
        IndicatorStyle.ACNE -> Brush.horizontalGradient(listOf(derma.acneStart, derma.acneEnd))
        IndicatorStyle.PIGMENT -> Brush.horizontalGradient(
            listOf(
                derma.pigmentStart,
                derma.pigmentEnd
            )
        )

        IndicatorStyle.DRYNESS -> Brush.horizontalGradient(
            listOf(
                derma.drynessFill,
                derma.drynessFill
            )
        )
    }
    val dotColor = when (indicator.style) {
        IndicatorStyle.ACNE -> derma.acneDot
        IndicatorStyle.PIGMENT -> derma.pigmentDot
        IndicatorStyle.DRYNESS -> derma.drynessDot
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Dot(color = dotColor)
                Text(
                    text = stringResource(indicator.name),
                    style = dermaType(14, 20),
                    color = onSurface
                )
            }
            Text(
                text = stringResource(R.string.home_indicator_percent, indicator.percent),
                style = dermaType(14, 20, FontWeight.Bold),
                color = derma.percentText
            )
        }

        Spacer(Modifier.height(3.dp))

        // الشريط بيملأ من بداية الاتجاه (يمين في العربي) تلقائياً
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(9.dp)
                .clip(CircleShape)
                .background(derma.track)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(indicator.percent / 100f)
                    .height(9.dp)
                    .clip(CircleShape)
                    .background(brush)
            )
        }
    }
}
