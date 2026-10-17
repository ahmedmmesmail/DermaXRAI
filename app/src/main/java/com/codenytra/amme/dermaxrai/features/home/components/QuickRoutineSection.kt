package com.codenytra.amme.dermaxrai.features.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.R
import com.codenytra.amme.dermaxrai.features.home.RoutineType
import com.codenytra.amme.dermaxrai.ui.IconCircle
import com.codenytra.amme.dermaxrai.ui.Panel
import com.codenytra.amme.dermaxrai.ui.dermaType
import com.codenytra.amme.dermaxrai.ui.theme.derma

@Composable
fun QuickRoutineSection(onRoutineClick: (RoutineType) -> Unit) {
    val derma = MaterialTheme.derma

    Column {
        SectionHeader(
            title = R.string.home_routine_title,
            icon = Icons.Rounded.CalendarToday
        ) {
            Text(
                text = stringResource(R.string.home_routine_today),
                style = dermaType(11, 16),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            RoutineCard(
                title = R.string.home_routine_morning_title,
                subtitle = R.string.home_routine_morning_subtitle,
                icon = Icons.Rounded.WbSunny,
                statusIcon = Icons.Rounded.Check,
                statusTint = derma.iconTint,
                chipText = stringResource(R.string.home_routine_morning_steps, 3),
                onClick = { onRoutineClick(RoutineType.MORNING) }
            )
            RoutineCard(
                title = R.string.home_routine_evening_title,
                subtitle = R.string.home_routine_evening_subtitle,
                icon = Icons.Rounded.NightsStay,
                statusIcon = Icons.Rounded.Schedule,
                statusTint = derma.iconMuted,
                chipText = stringResource(R.string.home_routine_pending),
                onClick = { onRoutineClick(RoutineType.EVENING) }
            )
        }
    }
}

@Composable
fun RowScope.RoutineCard(
    @StringRes title: Int,
    @StringRes subtitle: Int,
    icon: ImageVector,
    statusIcon: ImageVector,
    statusTint: Color,
    chipText: String,
    onClick: () -> Unit
) {
    val derma = MaterialTheme.derma

    Panel(
        shape = RoundedCornerShape(28.dp),
        color = derma.card,
        withShadow = true,
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .height(160.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconCircle(icon, 32.dp, derma.circleStrong, derma.iconTint, iconSize = 18.dp)
                IconCircle(statusIcon, 24.dp, derma.circleSoft, statusTint, iconSize = 14.dp)
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = stringResource(title),
                style = dermaType(20, 28, FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(subtitle),
                style = dermaType(13, 18),
                color = derma.bodyText
            )

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Panel(shape = CircleShape, color = derma.chipSoft) {
                    Text(
                        text = chipText,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp),
                        style = dermaType(12, 16),
                        color = derma.accentText
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = derma.chevron,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
