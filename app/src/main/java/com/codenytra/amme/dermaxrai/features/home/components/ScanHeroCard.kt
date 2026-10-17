package com.codenytra.amme.dermaxrai.features.home.components

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
import androidx.compose.material.icons.rounded.FaceRetouchingNatural
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.R
import com.codenytra.amme.dermaxrai.ui.IconCircle
import com.codenytra.amme.dermaxrai.ui.Panel
import com.codenytra.amme.dermaxrai.ui.dermaType
import com.codenytra.amme.dermaxrai.ui.theme.derma

@Composable
fun ScanHeroCard(onScanClick: () -> Unit) {
    val derma = MaterialTheme.derma
    val onSurface = MaterialTheme.colorScheme.onSurface
    val isLight = derma.border == Color.Transparent

    Panel(
        shape = RoundedCornerShape(32.dp),
        color = Color.Transparent,
        withShadow = true
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(derma.heroGradient))
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconCircle(
                        icon = Icons.Rounded.FaceRetouchingNatural,
                        size = 44.dp,
                        container = derma.circleStrong,
                        tint = derma.iconTint
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.home_scan_title),
                            style = dermaType(18, 24, FontWeight.Medium),
                            color = onSurface
                        )
                        Text(
                            text = stringResource(R.string.home_scan_subtitle),
                            style = dermaType(12, 16),
                            color = derma.bodyText
                        )
                    }
                    Panel(
                        shape = CircleShape,
                        color = if (isLight) MaterialTheme.colorScheme.background else derma.chipStrong
                    ) {
                        Text(
                            text = stringResource(R.string.home_scan_badge),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                            style = dermaType(11, 14),
                            color = derma.accentText
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(derma.buttonStart, derma.buttonEnd)))
                        .clickable(role = Role.Button, onClick = onScanClick)
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FaceRetouchingNatural,
                            contentDescription = stringResource(R.string.home_cd_scan),
                            tint = derma.buttonText,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = stringResource(R.string.home_scan_button),
                            style = dermaType(15, 22, FontWeight.SemiBold),
                            color = derma.buttonText
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = derma.buttonText,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}