package com.codenytra.amme.dermaxrai.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codenytra.amme.dermaxrai.ui.theme.derma

/** بياخد الخط من Typography، وبيصفّر letterSpacing عشان الحروف العربي ماتتفكش. */
@Composable
fun dermaType(
    size: Int,
    lineHeight: Int,
    weight: FontWeight = FontWeight.Normal
): TextStyle = MaterialTheme.typography.bodyMedium.copy(
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    fontWeight = weight,
    letterSpacing = 0.sp
)

/** سطح بحدود (dark) وظل (light) زي التصميم. */
@Composable
fun Panel(
    shape: Shape,
    color: Color,
    modifier: Modifier = Modifier,
    withShadow: Boolean = false,
    borderColor: Color = MaterialTheme.derma.border,
    shadowColor: Color = MaterialTheme.derma.shadow,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shaped = if (withShadow) {
        modifier.shadow(10.dp, shape, ambientColor = shadowColor, spotColor = shadowColor)
    } else modifier
    val border = BorderStroke(1.dp, borderColor)

    if (onClick != null) {
        Surface(onClick = onClick, modifier = shaped, shape = shape, color = color, border = border, content = content)
    } else {
        Surface(modifier = shaped, shape = shape, color = color, border = border, content = content)
    }
}

@Composable
fun IconCircle(
    icon: ImageVector,
    size: Dp,
    container: Color,
    tint: Color,
    iconSize: Dp = 22.dp,
    borderColor: Color = MaterialTheme.derma.border
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(container)
            .border(1.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(iconSize))
    }
}

@Composable
fun Dot(color: Color, size: Dp = 8.dp) {
    Spacer(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}