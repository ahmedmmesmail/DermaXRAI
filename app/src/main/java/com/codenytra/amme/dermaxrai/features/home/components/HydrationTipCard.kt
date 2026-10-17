package com.codenytra.amme.dermaxrai.features.home.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.R
import com.codenytra.amme.dermaxrai.ui.IconCircle
import com.codenytra.amme.dermaxrai.ui.Panel
import com.codenytra.amme.dermaxrai.ui.dermaType
import com.codenytra.amme.dermaxrai.ui.theme.derma

@Composable
fun HydrationTipCard() {
    val derma = MaterialTheme.derma

    Panel(
        shape = RoundedCornerShape(2.dp),
        color = derma.tipCard,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconCircle(
                icon = Icons.Rounded.Lightbulb,
                size = 40.dp,
                container = derma.circleSoft,
                tint = derma.accentText
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.home_tip_title),
                    style = dermaType(14, 20, FontWeight.SemiBold),
                    color = derma.heading
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.home_tip_body),
                    style = dermaType(12, 20),
                    color = derma.bodyText
                )
            }
        }
    }
}

@Composable
fun DisclaimerPill() {
    val derma = MaterialTheme.derma

    Panel(
        shape = CircleShape,
        color = derma.disclaimer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = null,
                tint = derma.iconMuted,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(R.string.home_disclaimer),
                style = dermaType(12, 16),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}