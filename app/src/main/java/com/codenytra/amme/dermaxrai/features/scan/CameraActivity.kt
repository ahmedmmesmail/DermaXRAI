package com.codenytra.amme.dermaxrai.features.scan

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.FlipCameraAndroid
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.NoPhotography
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.rounded.WbTwilight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.R
import com.codenytra.amme.dermaxrai.features.settings.SettingsActivity
import com.codenytra.amme.dermaxrai.ui.Dot
import com.codenytra.amme.dermaxrai.ui.IconCircle
import com.codenytra.amme.dermaxrai.ui.Panel
import com.codenytra.amme.dermaxrai.ui.dermaType
import com.codenytra.amme.dermaxrai.ui.theme.DermaXRAITheme
import com.codenytra.amme.dermaxrai.ui.theme.derma

class CameraActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            DermaXRAITheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Image(
                                    painter = painterResource(R.drawable.banner),
                                    contentDescription = "DermaXRAI",
                                    modifier = Modifier.height(50.dp)
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    finish()
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowBackIos,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = {

                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.FlashOn,
                                        contentDescription = "Flash"
                                    )
                                }
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    CameraScreen(
                        quality = CameraQuality.GOOD,
                        onCapture = {
                            context.startActivity(Intent(context, ScanResultActivity::class.java))
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


enum class CameraQuality { GOOD, BAD }

// الـ viewport دايماً غامق (هو الكاميرا نفسها) في الـ light والـ dark
private val ViewportTop = Color(0xFF4C382E)
private val ViewportBottom = Color(0xFF1E0E0A)
private val Peach = Color(0xFFFFB59A)
private val ErrorChip = Color(0xFF622A24)

/**
 * شاشة الكاميرا بحالتيها.
 *
 * @param preview هنا تحط CameraX PreviewView (AndroidView)؛ الشاشة بترسم الـ overlay فوقه.
 * @param quality GOOD = وجه معروف وإضاءة ممتازة، BAD = إضاءة ضعيفة.
 */
@Composable
fun CameraScreen(
    quality: CameraQuality,
    modifier: Modifier = Modifier,
    lightPercent: Int = 14,
    distanceCm: Int = 35,
    logo: androidx.compose.ui.graphics.painter.Painter? = null,
    preview: @Composable () -> Unit = {},
    onClose: () -> Unit = {},
    onFlashToggle: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onFlipCamera: () -> Unit = {},
    onCapture: () -> Unit = {},
    onGallery: () -> Unit = {},
    onGuide: () -> Unit = {},
    onRetry: () -> Unit = {},
    onFlashRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            // الكاميرا + تدرج
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(ViewportTop, ViewportBottom)))
            ) { preview() }

            when (quality) {
                CameraQuality.GOOD -> GoodOverlay(onFlipCamera)
                CameraQuality.BAD -> BadOverlay(lightPercent, distanceCm)
            }

            if (quality == CameraQuality.BAD) {
                BadSheet(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onRetry = onRetry,
                    onFlashRetry = onFlashRetry,
                    onGallery = onGallery
                )
            }
        }

        if (quality == CameraQuality.GOOD) {
            GoodControls(onCapture = onCapture, onGallery = onGallery, onGuide = onGuide)
        }
    }
}

// ───────────────────────── Top bar ─────────────────────────

@Composable
private fun RoundIconButton(icon: ImageVector, onClick: () -> Unit, size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.14f))
            .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = Color(0xFFFFF8F6), modifier = Modifier.size(22.dp))
    }
}

// ───────────────────────── Good state ─────────────────────────

@Composable
private fun GoodOverlay(onFlipCamera: () -> Unit) {
    var tipVisible by rememberSaveable { mutableStateOf(true) }

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Pill(modifier = Modifier.weight(1f)) {
                    Dot(color = Color(0xFF7CC24D), size = 12.dp)
                    Text(
                        text = stringResource(R.string.camera_face_detected),
                        style = dermaType(12, 16),
                        color = Color(0xFFFFF8F6)
                    )
                }
                RoundIconButton(Icons.Rounded.FlipCameraAndroid, onFlipCamera, 40.dp)
            }
            if (tipVisible) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE2D7))
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Rounded.LightMode,
                        null,
                        tint = Color(0xFF914A2D),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.camera_tip_stable),
                        modifier = Modifier.weight(1f),
                        style = dermaType(13, 18),
                        color = Color(0xFF29170F)
                    )
                    Icon(
                        Icons.Rounded.Close, null, tint = Color(0xFF53433F),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { tipVisible = false }
                    )
                }
            }
        }

        FaceGuide(
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 224.dp, height = 280.dp),
            showFaceLines = true
        )

        // شريحة المميزات تحت
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FeatureTag(Icons.Rounded.LightMode, R.string.camera_tag_light)
            FeatureTag(Icons.Rounded.Face, R.string.camera_tag_makeup)
            FeatureTag(Icons.Rounded.CenterFocusStrong, R.string.camera_tag_front)
            FeatureTag(Icons.Rounded.Visibility, R.string.camera_tag_eyes)
        }
    }
}

@Composable
private fun FeatureTag(icon: ImageVector, @StringRes text: Int) {
    Pill {
        Icon(icon, null, tint = Peach, modifier = Modifier.size(16.dp))
        Text(stringResource(text), style = dermaType(11, 16), color = Color(0xFFFFE2D7))
    }
}

@Composable
private fun Pill(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFF241209).copy(alpha = 0.78f))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) { content() }
}

@Composable
private fun GoodControls(onCapture: () -> Unit, onGallery: () -> Unit, onGuide: () -> Unit) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SideAction(Icons.Rounded.HelpOutline, R.string.camera_guide, onGuide)
            // زرار الالتقاط
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .shadow(
                        16.dp,
                        CircleShape,
                        ambientColor = derma.buttonGlow,
                        spotColor = derma.buttonGlow
                    )
                    .clip(CircleShape)
                    .background(derma.heroGlow.copy(alpha = 0.4f))
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(derma.ctaStart, derma.ctaEnd)))
                    .clickable(role = Role.Button, onClick = onCapture)
            )
            SideAction(Icons.Rounded.PhotoLibrary, R.string.camera_gallery, onGallery)
        }
        Spacer(Modifier.height(16.dp))
        PrivacyNote()
    }
}

@Composable
private fun SideAction(icon: ImageVector, @StringRes label: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable(role = Role.Button, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconCircle(
            icon = icon,
            size = 52.dp,
            container = MaterialTheme.derma.chipStrong,
            tint = MaterialTheme.derma.heading,
            iconSize = 24.dp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            stringResource(label),
            style = dermaType(12, 16),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PrivacyNote() {
    val derma = MaterialTheme.derma
    Panel(
        shape = RoundedCornerShape(24.dp),
        color = derma.zoneBox,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.camera_privacy),
                modifier = Modifier.weight(1f),
                style = dermaType(12, 18),
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(Icons.Rounded.Lock, null, tint = derma.iconMuted, modifier = Modifier.size(18.dp))
        }
    }
}

// ───────────────────────── Face guide ─────────────────────────

@Composable
private fun FaceGuide(modifier: Modifier, showFaceLines: Boolean) {
    val bracket = Color(0xFFFFD9CC)
    val dash = Peach

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val d = 1.dp.toPx()
        val ovalPad = 24 * d

        // بيضاوي منقط
        drawOval(
            color = dash,
            topLeft = Offset(ovalPad, 0f),
            size = Size(w - ovalPad * 2, h),
            style = Stroke(
                2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f * d, 6f * d))
            )
        )

        // أقواس الزوايا
        val arm = 26 * d
        val st = Stroke(3.dp.toPx(), cap = StrokeCap.Round)
        val corners = listOf(
            Triple(Offset(0f, 0f), 1f, 1f), Triple(Offset(w, 0f), -1f, 1f),
            Triple(Offset(0f, h), 1f, -1f), Triple(Offset(w, h), -1f, -1f)
        )
        corners.forEach { (o, sx, sy) ->
            val p = Path().apply {
                moveTo(o.x, o.y + sy * arm); lineTo(o.x, o.y); lineTo(o.x + sx * arm, o.y)
            }
            drawPath(p, bracket, style = st)
        }

        if (showFaceLines) {
            val cx = w / 2
            val thin = Stroke(
                1.2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * d, 3f * d))
            )
            drawOval(
                color = Color.White.copy(alpha = 0.35f),
                topLeft = Offset(cx - 60 * d, h * 0.22f),
                size = Size(120 * d, h * 0.62f),
                style = thin
            )
            val eyeY = h * 0.5f
            drawLine(
                Color.White.copy(alpha = 0.3f),
                Offset(cx - 70 * d, eyeY),
                Offset(cx + 70 * d, eyeY),
                1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f * d, 3f * d))
            )
            drawCircle(dash, 3.dp.toPx(), Offset(cx - 44 * d, eyeY))
            drawCircle(dash, 3.dp.toPx(), Offset(cx + 44 * d, eyeY))
            drawLine(
                Color.White.copy(alpha = 0.4f),
                Offset(cx, eyeY - 18 * d),
                Offset(cx, eyeY + 40 * d),
                1.dp.toPx()
            )
            val smile = Path().apply {
                moveTo(cx - 20 * d, h * 0.7f); quadraticBezierTo(
                cx,
                h * 0.73f,
                cx + 20 * d,
                h * 0.7f
            )
            }
            drawPath(
                smile,
                Color.White.copy(alpha = 0.45f),
                style = Stroke(1.2.dp.toPx(), cap = StrokeCap.Round)
            )
            // خط المسح
            drawLine(
                Brush.horizontalGradient(listOf(Color.Transparent, dash, Color.Transparent)),
                Offset(0f, 18 * d), Offset(w, 18 * d), 2.dp.toPx()
            )
        }
    }
}

// ───────────────────────── Bad state ─────────────────────────

@Composable
private fun BadOverlay(lightPercent: Int, distanceCm: Int) {
    val derma = MaterialTheme.derma

    Box(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Pill {
                Dot(color = Peach, size = 10.dp)
                Text(
                    text = stringResource(R.string.camera_light_sensor, lightPercent),
                    style = dermaType(12, 16),
                    color = Color(0xFFFFF8F6)
                )
            }
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ErrorChip)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Rounded.Warning,
                    null,
                    tint = Color(0xFFFFB4AB),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    stringResource(R.string.camera_accuracy_low),
                    style = dermaType(11, 16),
                    color = Color(0xFFFFDAD6)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 200.dp)
                .size(width = 224.dp, height = 280.dp),
            contentAlignment = Alignment.Center
        ) {
            FaceGuide(Modifier.fillMaxSize(), showFaceLines = false)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Rounded.LocationOn,
                    null,
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Rounded.NoPhotography,
                        null,
                        tint = Peach,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        stringResource(R.string.camera_landmarks_unclear),
                        style = dermaType(11, 16),
                        color = Peach
                    )
                }
            }
        }

        // شريط القياسات
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 292.dp)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Metric(Icons.Rounded.WbTwilight, stringResource(R.string.camera_metric_light))
            Metric(Icons.Rounded.CenterFocusStrong, stringResource(R.string.camera_metric_focus))
            Metric(
                Icons.Rounded.LocationOn,
                stringResource(R.string.camera_metric_distance, distanceCm)
            )
        }
    }
}

@Composable
private fun Metric(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, null, tint = Peach, modifier = Modifier.size(16.dp))
        Text(text, style = dermaType(11, 16), color = Color(0xFFFFE2D7))
    }
}

@Composable
private fun BadSheet(
    modifier: Modifier,
    onRetry: () -> Unit,
    onFlashRetry: () -> Unit,
    onGallery: () -> Unit
) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(derma.privacyCard)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 48.dp, height = 5.dp)
                .clip(CircleShape)
                .background(derma.pagerInactive)
        )
        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconCircle(
                Icons.Rounded.WbTwilight,
                48.dp,
                derma.chipStrong,
                derma.peach,
                iconSize = 24.dp
            )
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.camera_bad_title),
                        style = dermaType(20, 26, FontWeight.Medium),
                        color = colors.onSurface
                    )
                    Panel(shape = CircleShape, color = derma.chipSoft) {
                        Text(
                            text = stringResource(R.string.camera_bad_chip),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                            style = dermaType(10, 14),
                            color = derma.peach
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.camera_bad_desc),
                    style = dermaType(13, 22),
                    color = colors.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Panel(
            shape = RoundedCornerShape(28.dp),
            color = derma.zoneBox,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Rounded.Lightbulb,
                        null,
                        tint = derma.peach,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.camera_tips_title),
                        style = dermaType(14, 20, FontWeight.Medium),
                        color = derma.heading
                    )
                }
                NumberedTip(1, R.string.camera_tip_1)
                NumberedTip(2, R.string.camera_tip_2)
                NumberedTip(3, R.string.camera_tip_3)
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(
                    14.dp,
                    CircleShape,
                    ambientColor = derma.buttonGlow,
                    spotColor = derma.buttonGlow
                )
                .clip(CircleShape)
                .background(Brush.horizontalGradient(listOf(derma.ctaStart, derma.ctaEnd)))
                .clickable(role = Role.Button, onClick = onFlashRetry),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Rounded.FlashOn,
                null,
                tint = derma.buttonText,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.camera_flash_retry),
                style = dermaType(15, 22, FontWeight.Bold),
                color = derma.buttonText
            )
        }

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecondaryButton(
                Icons.Rounded.Refresh,
                R.string.camera_retry_now,
                onRetry,
                Modifier.weight(1f)
            )
            SecondaryButton(
                Icons.Rounded.PhotoLibrary,
                R.string.camera_pick_gallery,
                onGallery,
                Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider(color = colors.outlineVariant)
        Spacer(Modifier.height(14.dp))

        // زر الالتقاط المعطل
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(derma.chipStrong)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.camera_capture_disabled),
                    style = dermaType(14, 20, FontWeight.Medium),
                    color = colors.onSurface
                )
                Text(
                    text = stringResource(R.string.camera_capture_disabled_hint),
                    style = dermaType(11, 16),
                    color = colors.onSurfaceVariant
                )
            }
            IconCircle(
                Icons.Rounded.NoPhotography,
                44.dp,
                derma.chipStrong,
                derma.heading,
                iconSize = 22.dp
            )
        }

        Spacer(Modifier.height(14.dp))
        PrivacyNote()
    }
}

@Composable
private fun NumberedTip(number: Int, @StringRes text: Int) {
    val derma = MaterialTheme.derma
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(derma.chipStrong),
            contentAlignment = Alignment.Center
        ) {
            Text("$number", style = dermaType(11, 14, FontWeight.Bold), color = derma.peach)
        }
        Text(
            text = stringResource(text),
            modifier = Modifier.weight(1f),
            style = dermaType(12, 18),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SecondaryButton(
    icon: ImageVector,
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val derma = MaterialTheme.derma
    Panel(
        shape = CircleShape,
        color = derma.chipSoft,
        onClick = onClick,
        modifier = modifier.height(44.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = derma.peach, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                stringResource(text),
                style = dermaType(13, 18, FontWeight.Medium),
                color = derma.heading
            )
        }
    }
}
