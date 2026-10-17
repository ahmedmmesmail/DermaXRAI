package com.codenytra.amme.dermaxrai.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.R
import com.codenytra.amme.dermaxrai.ui.Panel
import com.codenytra.amme.dermaxrai.ui.dermaType
import com.codenytra.amme.dermaxrai.ui.theme.derma

@Composable
fun WelcomeHeader() {
    val derma = MaterialTheme.derma
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val isLight = derma.border == Color.Transparent

    Column {
        Panel(shape = CircleShape, color = derma.chipStrong) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Rounded.Spa,
                    contentDescription = null,
                    tint = derma.iconTint,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(R.string.home_badge_personalized_care),
                    style = dermaType(12, 16),
                    color = if (isLight) derma.bodyText else derma.accentText
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.home_welcome_title),
            style = dermaType(32, 40, FontWeight.Bold),
            color = derma.heading
        )
        Text(
            text = stringResource(R.string.app_name),
            style = dermaType(32, 40, FontWeight.Bold),
            color = derma.heading
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = stringResource(R.string.home_welcome_subtitle),
            style = dermaType(14, 23),
            color = onSurfaceVariant
        )
    }
}