package com.codenytra.amme.dermaxrai.features.scan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.TableChart
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.R
import com.codenytra.amme.dermaxrai.ui.Dot
import com.codenytra.amme.dermaxrai.ui.IconCircle
import com.codenytra.amme.dermaxrai.ui.Panel
import com.codenytra.amme.dermaxrai.ui.dermaType
import com.codenytra.amme.dermaxrai.ui.theme.DermaXRAITheme
import com.codenytra.amme.dermaxrai.ui.theme.derma

class ScanResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DermaXRAITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScanResultScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/** نتيجة واحدة في قائمة "التحديات المرصودة". */
data class Finding(
    val percent: Int,
    @StringRes val name: Int,
    @StringRes val explanation: Int
)

/** نقطة حرارية على الخريطة (إحداثيات 0..1 جوه مربع الخريطة). */
data class HeatSpot(val x: Float, val y: Float, val strong: Boolean = false)

val sampleFindings = listOf(
    Finding(82, R.string.result_f1_name, R.string.result_f1_expl),
    Finding(64, R.string.result_f2_name, R.string.result_f2_expl),
    Finding(45, R.string.result_f3_name, R.string.result_f3_expl)
)

private val sampleSpots = listOf(
    HeatSpot(0.38f, 0.20f), HeatSpot(0.35f, 0.52f, strong = true), HeatSpot(0.40f, 0.82f)
)

@Composable
fun ScanResultScreen(
    modifier: Modifier = Modifier,
    scanId: String = "DX-8492",
    findings: List<Finding> = sampleFindings,
    spots: List<HeatSpot> = sampleSpots,
    modelAccuracy: String = "96.4%",
    logo: Painter? = null,
    onBack: () -> Unit = {},
    onInfoClick: () -> Unit = {},
    onExportPdf: () -> Unit = {},
    onOpenRoutine: () -> Unit = {}
) {
    var mapVisible by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ResultTopBar(logo = logo, onBack = onBack, onInfoClick = onInfoClick)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp, bottom = 24.dp)
        ) {
            StatusRow(scanId)
            Spacer(Modifier.height(16.dp))
            MapCard(
                visible = mapVisible,
                onToggle = { mapVisible = !mapVisible },
                spots = spots,
                accuracy = modelAccuracy
            )
            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.result_findings_title),
                    style = dermaType(20, 28, FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.result_findings_count, findings.size),
                    style = dermaType(11, 16),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                findings.forEachIndexed { i, f -> FindingCard(i + 1, f) }
            }

            Spacer(Modifier.height(16.dp))
            ClinicalAlert()
            Spacer(Modifier.height(8.dp))
            DisclaimerBar()
            Spacer(Modifier.height(16.dp))

            ActionButtons(onExportPdf, onOpenRoutine)
        }
    }
}

// ───────────────────────── Header ─────────────────────────

@Composable
private fun ResultTopBar(logo: Painter?, onBack: () -> Unit, onInfoClick: () -> Unit) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth().background(colors.surface).statusBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null,
                tint = colors.onSurface,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(role = Role.Button, onClick = onBack)
            )
            Box(
                modifier = Modifier.size(28.dp).clip(RoundedCornerShape(7.dp)).background(Color(0xFF7A3E2B)),
                contentAlignment = Alignment.Center
            ) {
                if (logo != null) androidx.compose.foundation.Image(logo, null, Modifier.fillMaxSize())
                else Text("D", style = dermaType(14, 18, FontWeight.Bold), color = Color(0xFFFFF8F6))
            }
            Text(
                text = stringResource(R.string.result_screen_title),
                modifier = Modifier.weight(1f),
                style = dermaType(16, 22, FontWeight.Medium),
                color = colors.onSurface
            )
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(derma.headerCircle)
                    .clickable(role = Role.Button, onClick = onInfoClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Info, null, tint = derma.peach, modifier = Modifier.size(22.dp))
            }
        }
        androidx.compose.material3.HorizontalDivider(color = colors.outlineVariant.copy(alpha = 0.6f))
    }
}

@Composable
private fun StatusRow(scanId: String) {
    val derma = MaterialTheme.derma
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Dot(color = derma.drynessDot, size = 8.dp)
            Text(
                text = stringResource(R.string.result_scan_complete),
                style = dermaType(11, 16),
                color = derma.peach
            )
        }
        Panel(shape = CircleShape, color = derma.zoneBox) {
            Text(
                text = stringResource(R.string.result_scan_id, scanId),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = dermaType(11, 16),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ───────────────────────── Map card ─────────────────────────

@Composable
private fun MapCard(visible: Boolean, onToggle: () -> Unit, spots: List<HeatSpot>, accuracy: String) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Panel(
        shape = RoundedCornerShape(32.dp),
        color = derma.card,
        withShadow = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Rounded.TableChart, null, tint = derma.peach, modifier = Modifier.size(20.dp))
                    Text(
                        text = stringResource(R.string.result_map_title),
                        style = dermaType(18, 24, FontWeight.Medium),
                        color = colors.onSurface
                    )
                }
                Panel(shape = CircleShape, color = derma.chipSoft, onClick = onToggle) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Rounded.Layers, null, tint = derma.peach, modifier = Modifier.size(18.dp))
                        Text(
                            text = stringResource(if (visible) R.string.result_map_hide else R.string.result_map_show),
                            style = dermaType(12, 16),
                            color = colors.onSurface
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // المربع بتاع الخريطة
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(284.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(derma.mapBase)
            ) {
                if (visible) HeatMap(spots, Modifier.fillMaxSize())
                HeatLegend(Modifier.align(Alignment.BottomCenter).padding(8.dp))
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.result_inferred_type),
                    style = dermaType(12, 18),
                    color = colors.onSurfaceVariant
                )
                Panel(shape = CircleShape, color = derma.chipStrong) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.result_skin_type),
                            style = dermaType(13, 18, FontWeight.Medium),
                            color = derma.heading
                        )
                        Icon(Icons.Rounded.WaterDrop, null, tint = derma.peach, modifier = Modifier.size(16.dp))
                    }
                }
                Text(
                    text = stringResource(R.string.result_model_accuracy, accuracy),
                    style = dermaType(10, 14),
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun HeatMap(spots: List<HeatSpot>, modifier: Modifier) {
    val derma = MaterialTheme.derma
    val faceFill = derma.chipStrong.copy(alpha = 0.55f)
    val outline = derma.peach.copy(alpha = 0.4f)
    val hot = MaterialTheme.colorScheme.tertiary
    val core = MaterialTheme.colorScheme.error

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val d = 1.dp.toPx()

        // بروفايل الوجه
        val face = Path().apply {
            moveTo(w * 0.40f, h * 0.10f)
            cubicTo(w * 0.52f, h * 0.08f, w * 0.64f, h * 0.18f, w * 0.65f, h * 0.36f)
            cubicTo(w * 0.66f, h * 0.44f, w * 0.69f, h * 0.50f, w * 0.69f, h * 0.55f)
            cubicTo(w * 0.69f, h * 0.60f, w * 0.64f, h * 0.62f, w * 0.64f, h * 0.66f)
            cubicTo(w * 0.64f, h * 0.76f, w * 0.52f, h * 0.84f, w * 0.46f, h * 0.90f)
            cubicTo(w * 0.40f, h * 0.96f, w * 0.34f, h * 0.90f, w * 0.34f, h * 0.76f)
            cubicTo(w * 0.32f, h * 0.55f, w * 0.30f, h * 0.30f, w * 0.40f, h * 0.10f)
            close()
        }
        drawPath(face, faceFill)
        drawPath(face, outline, style = Stroke(1.dp.toPx()))

        spots.forEach { s ->
            val c = Offset(w * (0.30f + s.x * 0.35f), h * s.y)
            val r = (if (s.strong) 30 else 20) * d
            drawCircle(
                brush = Brush.radialGradient(listOf(hot.copy(alpha = if (s.strong) 0.95f else 0.7f), core.copy(alpha = 0.25f), Color.Transparent), c, r),
                radius = r, center = c
            )
            drawCircle(Color.White, 3.dp.toPx(), c + Offset(-6 * d, 2 * d))
        }
    }
}

@Composable
private fun HeatLegend(modifier: Modifier) {
    val derma = MaterialTheme.derma
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(derma.card.copy(alpha = 0.92f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(stringResource(R.string.result_legend_title), style = dermaType(10, 14), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(stringResource(R.string.result_legend_low), style = dermaType(10, 14), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Box(
            Modifier
                .width(64.dp)
                .height(8.dp)
                .clip(CircleShape)
                .background(Brush.horizontalGradient(listOf(derma.acneEnd, derma.pigmentStart)))
        )
        Text(stringResource(R.string.result_legend_high), style = dermaType(10, 14, FontWeight.Medium), color = derma.peach)
    }
}

// ───────────────────────── Findings ─────────────────────────

@Composable
private fun FindingCard(index: Int, finding: Finding) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Panel(
        shape = RoundedCornerShape(28.dp),
        color = derma.card,
        withShadow = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconCircleNumber(index)
                Text(
                    text = stringResource(finding.name),
                    modifier = Modifier.weight(1f),
                    style = dermaType(14, 20, FontWeight.Medium),
                    color = colors.onSurface
                )
                Text(
                    text = "${finding.percent}%",
                    style = dermaType(15, 20, FontWeight.Bold),
                    color = derma.percentText
                )
            }

            Spacer(Modifier.height(8.dp))

            // الشريط: بيملأ من بداية الاتجاه
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(derma.track)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(finding.percent / 100f)
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(derma.acneEnd, derma.acneStart)))
                )
            }

            Spacer(Modifier.height(10.dp))

            Panel(shape = RoundedCornerShape(24.dp), color = derma.zoneBox, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Rounded.Search, null, tint = derma.peach, modifier = Modifier.size(18.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.onSurface)) {
                                append(stringResource(R.string.result_xrai_label))
                            }
                            append(" ")
                            append(stringResource(finding.explanation))
                        },
                        modifier = Modifier.weight(1f),
                        style = dermaType(12, 20),
                        color = colors.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun IconCircleNumber(n: Int) {
    val derma = MaterialTheme.derma
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(derma.chipSoft),
        contentAlignment = Alignment.Center
    ) {
        Text("$n", style = dermaType(12, 16, FontWeight.Bold), color = derma.peach)
    }
}

// ───────────────────────── Alert + actions ─────────────────────────

@Composable
private fun ClinicalAlert() {
    val derma = MaterialTheme.derma

    Panel(
        shape = RoundedCornerShape(28.dp),
        color = derma.alertCard,
        borderColor = derma.noticeBorder.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.result_alert_title),
                    style = dermaType(16, 22, FontWeight.Bold),
                    color = derma.alertTitle
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.result_alert_body),
                    style = dermaType(12, 20),
                    color = derma.alertBody
                )
            }
            IconCircle(
                icon = Icons.Rounded.Warning,
                size = 40.dp,
                container = derma.card,
                tint = derma.alertTitle,
                iconSize = 22.dp,
                borderColor = derma.noticeBorder
            )
        }
    }
}

@Composable
private fun DisclaimerBar() {
    val derma = MaterialTheme.derma
    Panel(shape = CircleShape, color = derma.disclaimer, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.result_disclaimer),
                modifier = Modifier.weight(1f),
                style = dermaType(11, 16),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Icon(Icons.Rounded.VerifiedUser, null, tint = derma.iconMuted, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun ActionButtons(onExportPdf: () -> Unit, onOpenRoutine: () -> Unit) {
    val derma = MaterialTheme.derma

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(CircleShape)
            .background(Brush.horizontalGradient(listOf(derma.ctaStart, derma.ctaEnd)))
            .clickable(role = Role.Button, onClick = onExportPdf),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Rounded.PictureAsPdf, null, tint = derma.buttonText, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.result_export_pdf),
            style = dermaType(15, 22, FontWeight.Medium),
            color = derma.buttonText
        )
    }

    Spacer(Modifier.height(8.dp))

    Panel(
        shape = CircleShape,
        color = derma.chipSoft,
        borderColor = MaterialTheme.derma.border,
        onClick = onOpenRoutine,
        modifier = Modifier.fillMaxWidth().height(56.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Rounded.Eco, null, tint = derma.peach, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.result_open_routine),
                style = dermaType(15, 22, FontWeight.Medium),
                color = derma.heading
            )
        }
    }
}

// ───────────────────────── Preview ─────────────────────────

@Preview(showBackground = true, widthDp = 390, heightDp = 1600, locale = "ar")
@Composable
private fun ResultLightPreview() { DermaXRAITheme(darkTheme = false) { ScanResultScreen() } }

@Preview(showBackground = true, widthDp = 390, heightDp = 1600, locale = "ar")
@Composable
private fun ResultDarkPreview() { DermaXRAITheme(darkTheme = true) { ScanResultScreen() } }