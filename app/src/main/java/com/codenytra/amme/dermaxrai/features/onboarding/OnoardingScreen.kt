package com.codenytra.amme.dermaxrai.features.onboarding

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.FaceRetouchingNatural
import androidx.compose.material.icons.rounded.GppGood
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.R
import com.codenytra.amme.dermaxrai.ui.*
import com.codenytra.amme.dermaxrai.ui.theme.derma
import com.codenytra.amme.dermaxrai.ui.theme.DermaXRAITheme
import kotlinx.coroutines.launch

private const val PAGE_COUNT = 3

// ═══════════════════════════ Entry ═══════════════════════════

/**
 * شاشة الـ Onboarding (3 صفحات).
 *
 * @param onFinish بيتنادى لما المستخدم يضغط "ابدأ الآن" (بعد الإقرار في الصفحة الأخيرة).
 * @param onSkip بيتنادى عند "تخطي".
 * @param logo أيقونة التطبيق (اختياري، وإلا بيظهر حرف D مؤقتاً).
 * @param wordmark صورة اللوجو النصي DERMA-X-R{AI} في الصفحة الأولى (اختياري).
 * @param scanImage الصورة اللي في منتصف هيرو الصفحة الأولى (اختياري).
 */
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
    onSkip: () -> Unit = onFinish,
    logo: Painter? = null,
    wordmark: Painter? = null,
    scanImage: Painter? = null
) {
    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })
    val scope = rememberCoroutineScope()
    var acknowledged by rememberSaveable { mutableStateOf(false) }

    val page = pagerState.currentPage
    val horizontal = if (page == 0) 16.dp else 20.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { index ->
            when (index) {
                0 -> ScanPage(wordmark = wordmark, scanImage = scanImage, onSkip = onSkip)
                1 -> TransparencyPage(logo = logo, onSkip = onSkip)
                else -> DisclaimerPage(
                    logo = logo,
                    acknowledged = acknowledged,
                    onAcknowledgedChange = { acknowledged = it }
                )
            }
        }

        BottomControls(
            page = page,
            horizontal = horizontal,
            canFinish = acknowledged,
            onNext = { scope.launch { pagerState.animateScrollToPage(page + 1) } },
            onPrevious = { scope.launch { pagerState.animateScrollToPage(page - 1) } },
            onFinish = onFinish
        )
    }
}

// ═══════════════════════════ Pages ═══════════════════════════

@Composable
private fun PageColumn(
    horizontal: Dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = horizontal)
            .padding(top = 12.dp, bottom = 12.dp),
        content = content
    )
}

// ─────────── Page 1: المسح ───────────

@Composable
private fun ScanPage(wordmark: Painter?, scanImage: Painter?, onSkip: () -> Unit) {
    val derma = MaterialTheme.derma

    PageColumn(horizontal = 16.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (wordmark != null) {
                Image(
                    painter = wordmark,
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.height(28.dp)
                )
            } else {
                Text(
                    text = "DERMA-X-R{AI}",
                    style = dermaType(20, 26, FontWeight.Black).copy(fontStyle = FontStyle.Italic),
                    color = derma.brandText
                )
            }
            SkipPill(onSkip)
        }

        Spacer(Modifier.height(26.dp))

        // Hero
        Panel(
            shape = RoundedCornerShape(32.dp),
            color = derma.heroBase,
            modifier = Modifier
                .fillMaxWidth()
                .height(267.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.radialGradient(listOf(derma.heroGlow, derma.heroBase), radius = 520f))
            ) {
                if (scanImage != null) {
                    Image(
                        painter = scanImage,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(129.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(88.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(derma.heroTile),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FaceRetouchingNatural,
                            contentDescription = null,
                            tint = derma.iconTint,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Panel(
                    shape = CircleShape,
                    color = derma.heroTile,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.QrCodeScanner,
                            contentDescription = null,
                            tint = derma.peach,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = stringResource(R.string.onboarding_p1_chip),
                            style = dermaType(11, 16, FontWeight.SemiBold),
                            color = derma.peach
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        StepIndicator(step = 1, filled = false)
        Spacer(Modifier.height(6.dp))
        PageTitle(R.string.onboarding_p1_title, size = 32, lineHeight = 42)
        Spacer(Modifier.height(8.dp))
        PageBody(R.string.onboarding_p1_body)
        Spacer(Modifier.height(15.dp))

        Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
            FeatureRow(Icons.Rounded.Timer, R.string.onboarding_p1_feature1)
            FeatureRow(Icons.Rounded.GppGood, R.string.onboarding_p1_feature2)
        }
    }
}

@Composable
private fun FeatureRow(icon: ImageVector, @StringRes text: Int) {
    val derma = MaterialTheme.derma

    Panel(
        shape = RoundedCornerShape(2.dp),
        color = derma.zoneBox,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconCircle(
                icon = icon,
                size = 36.dp,
                container = derma.chipStrong,
                tint = derma.peach,
                iconSize = 20.dp
            )
            Text(
                text = stringResource(text),
                style = dermaType(12, 18),
                color = derma.heading
            )
        }
    }
}

// ─────────── Page 2: الشفافية ───────────

@Composable
private fun TransparencyPage(logo: Painter?, onSkip: () -> Unit) {
    val derma = MaterialTheme.derma

    PageColumn(horizontal = 20.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LogoTile(logo, size = 40.dp)
                Text(
                    text = stringResource(R.string.app_name),
                    style = dermaType(16, 22, FontWeight.Medium),
                    color = derma.brandText
                )
            }
            SkipPill(onSkip)
        }

        Spacer(Modifier.height(20.dp))
        AttributionHero()
        Spacer(Modifier.height(22.dp))

        StepIndicator(step = 2, filled = false, icon = Icons.Rounded.QrCodeScanner)
        Spacer(Modifier.height(8.dp))
        PageTitle(R.string.onboarding_p2_title, size = 28, lineHeight = 34)
        Spacer(Modifier.height(8.dp))
        PageBody(R.string.onboarding_p2_body)
        Spacer(Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            FeatureChip(Icons.Rounded.LocalFireDepartment, R.string.onboarding_p2_feature1, derma.heading)
            FeatureChip(Icons.Rounded.Psychology, R.string.onboarding_p2_feature2, derma.peach)
        }
    }
}

@Composable
private fun FeatureChip(icon: ImageVector, @StringRes text: Int, color: Color) {
    Panel(shape = RoundedCornerShape(22.dp), color = MaterialTheme.derma.zoneBox) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Text(
                text = stringResource(text),
                style = dermaType(13, 20, FontWeight.Medium),
                color = color
            )
        }
    }
}

/** هيرو الصفحة التانية: وجه + بؤر حرارية + بطاقات توضيحية (رسم يدوي). */
@Composable
private fun AttributionHero() {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme
    val heat = colors.error
    val faceFill = derma.chipSoft
    val line = derma.peach.copy(alpha = 0.55f)
    val glow = derma.heroGlow
    val blob = colors.outline.copy(alpha = 0.22f)
    val bracket = derma.pagerInactive

    Panel(
        shape = RoundedCornerShape(28.dp),
        color = derma.card,
        withShadow = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        ScaledIllustration(designWidth = 350.dp, designHeight = 262.dp) {
            Canvas(Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                // خلفيات ناعمة
                drawCircle(
                    brush = Brush.radialGradient(listOf(glow.copy(alpha = 0.9f), Color.Transparent), Offset(w - 20.dp.toPx(), 30.dp.toPx()), 120.dp.toPx()),
                    radius = 120.dp.toPx(), center = Offset(w - 20.dp.toPx(), 30.dp.toPx())
                )
                drawCircle(
                    brush = Brush.radialGradient(listOf(blob, Color.Transparent), Offset(45.dp.toPx(), h - 45.dp.toPx()), 105.dp.toPx()),
                    radius = 105.dp.toPx(), center = Offset(45.dp.toPx(), h - 45.dp.toPx())
                )

                // أقواس الزوايا
                val arc = 24.dp.toPx()
                val inset = 13.dp.toPx()
                val st = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                drawArc(bracket, 180f, 90f, false, Offset(inset, inset), Size(arc, arc), style = st)
                drawArc(bracket, 270f, 90f, false, Offset(w - inset - arc, inset), Size(arc, arc), style = st)
                drawArc(bracket, 0f, 90f, false, Offset(w - inset - arc, h - inset - arc), Size(arc, arc), style = st)
                drawArc(bracket, 90f, 90f, false, Offset(inset, h - inset - arc), Size(arc, arc), style = st)

                // الوجه (بروفايل)
                val d = 1.dp.toPx()
                val face = Path().apply {
                    moveTo(168 * d, 70 * d)
                    cubicTo(185 * d, 52 * d, 232 * d, 54 * d, 250 * d, 96 * d)
                    cubicTo(262 * d, 130 * d, 252 * d, 170 * d, 236 * d, 196 * d)
                    lineTo(244 * d, h)
                    lineTo(176 * d, h)
                    lineTo(180 * d, 208 * d)
                    cubicTo(166 * d, 204 * d, 152 * d, 196 * d, 150 * d, 186 * d)
                    cubicTo(142 * d, 184 * d, 140 * d, 176 * d, 144 * d, 170 * d)
                    lineTo(130 * d, 162 * d)
                    cubicTo(126 * d, 158 * d, 128 * d, 152 * d, 134 * d, 150 * d)
                    cubicTo(138 * d, 140 * d, 142 * d, 128 * d, 146 * d, 118 * d)
                    cubicTo(150 * d, 100 * d, 156 * d, 84 * d, 168 * d, 70 * d)
                    close()
                }
                drawPath(face, faceFill)

                // خطوط العين والحاجب
                val thin = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
                val brow = Path().apply {
                    moveTo(150 * d, 112 * d); quadraticTo(164 * d, 104 * d, 178 * d, 110 * d)
                }
                val eye = Path().apply {
                    moveTo(152 * d, 134 * d); quadraticTo(165 * d, 128 * d, 176 * d, 136 * d)
                }
                drawPath(brow, line, style = thin)
                drawPath(eye, line, style = thin)

                // بؤر الحرارة
                fun spot(cx: Float, cy: Float, r: Float, a: Float) = drawCircle(
                    brush = Brush.radialGradient(listOf(heat.copy(alpha = a), Color.Transparent), Offset(cx * d, cy * d), r * d),
                    radius = r * d, center = Offset(cx * d, cy * d)
                )
                spot(186f, 80f, 17f, 0.45f)
                spot(184f, 122f, 28f, 0.55f)
                spot(172f, 166f, 15f, 0.35f)
                drawCircle(heat, 4.dp.toPx(), Offset(184 * d, 122 * d))
                drawCircle(heat.copy(alpha = 0.6f), 8.dp.toPx(), Offset(184 * d, 122 * d), style = Stroke(1.dp.toPx()))

                // خط التوصيل المنقط
                val leader = Path().apply {
                    moveTo(192 * d, 122 * d)
                    quadraticTo(250 * d, 112 * d, 298 * d, 168 * d)
                }
                drawPath(
                    leader, line,
                    style = Stroke(width = 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f * d, 4f * d)))
                )
                drawCircle(derma.emphasis, 3.dp.toPx(), Offset(298 * d, 168 * d))
            }

            // شرائح الأعلى (XRAI تحت، التحليل فوقها)
            AttributionChip(
                modifier = Modifier.absoluteOffset(x = 125.dp, y = 14.dp),
                text = R.string.onboarding_p2_chip_xrai,
                icon = Icons.Rounded.Insights,
                bold = true
            )
            AttributionChip(
                modifier = Modifier.absoluteOffset(x = 13.dp, y = 14.dp),
                text = R.string.onboarding_p2_chip_neural,
                dot = derma.emphasis,
                bold = false
            )

            // البطاقات
            AttributionCard(
                modifier = Modifier.absoluteOffset(x = 40.dp, y = 73.dp),
                dot = colors.tertiary,
                title = stringResource(R.string.onboarding_p2_label_oily),
                subtitle = stringResource(R.string.onboarding_p2_label_oily_sub, 89),
                titleColor = derma.heading
            )
            AttributionCard(
                modifier = Modifier.absoluteOffset(x = 188.dp, y = 148.dp),
                dot = heat,
                title = stringResource(R.string.onboarding_p2_label_spot),
                subtitle = stringResource(R.string.onboarding_p2_label_spot_sub, 94),
                titleColor = heat
            )
        }
    }
}

@Composable
private fun AttributionChip(
    modifier: Modifier,
    @StringRes text: Int,
    icon: ImageVector? = null,
    dot: Color? = null,
    bold: Boolean
) {
    val derma = MaterialTheme.derma

    Panel(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        withShadow = true,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = derma.iconTint, modifier = Modifier.size(16.dp))
            }
            if (dot != null) Dot(color = dot, size = 7.dp)
            Text(
                text = stringResource(text),
                style = dermaType(if (bold) 12 else 11, 16, if (bold) FontWeight.SemiBold else FontWeight.Normal),
                color = if (bold) derma.heading else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AttributionCard(
    modifier: Modifier,
    dot: Color,
    title: String,
    subtitle: String,
    titleColor: Color
) {
    Panel(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        withShadow = true,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Dot(color = dot, size = 9.dp)
            Column {
                Text(
                    text = title,
                    style = dermaType(12, 16, FontWeight.Bold),
                    color = titleColor
                )
                Text(
                    text = subtitle,
                    style = dermaType(11, 15),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ─────────── Page 3: الإخلاء والإقرار ───────────

@Composable
private fun DisclaimerPage(
    logo: Painter?,
    acknowledged: Boolean,
    onAcknowledgedChange: (Boolean) -> Unit
) {
    val derma = MaterialTheme.derma

    PageColumn(horizontal = 20.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LogoTile(logo, size = 40.dp)
                Column {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = dermaType(16, 22, FontWeight.Bold),
                        color = derma.brandText
                    )
                    Text(
                        text = stringResource(R.string.onboarding_tagline),
                        style = dermaType(11, 15),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Panel(shape = CircleShape, color = derma.chipStrong) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.VerifiedUser,
                        contentDescription = null,
                        tint = derma.peach,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = stringResource(R.string.onboarding_ready),
                        style = dermaType(12, 16),
                        color = derma.peach
                    )
                }
            }
        }

        Spacer(Modifier.height(26.dp))
        CertifiedHero()
        Spacer(Modifier.height(21.dp))

        StepIndicator(step = 3, filled = true, label = R.string.onboarding_p3_label)
        Spacer(Modifier.height(10.dp))
        PageTitle(R.string.onboarding_p3_title, size = 28, lineHeight = 34)
        Spacer(Modifier.height(8.dp))
        PageBody(R.string.onboarding_p3_body)
        Spacer(Modifier.height(12.dp))

        NoticeCard()
        Spacer(Modifier.height(14.dp))
        ConsentCard(checked = acknowledged, onCheckedChange = onAcknowledgedChange)
    }
}

@Composable
private fun NoticeCard() {
    val derma = MaterialTheme.derma

    Panel(
        shape = RoundedCornerShape(20.dp),
        color = derma.noticeCard,
        borderColor = derma.noticeBorder,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconCircle(
                    icon = Icons.Rounded.MedicalServices,
                    size = 32.dp,
                    container = derma.noticeCircle,
                    tint = derma.noticeTitle,
                    iconSize = 18.dp,
                    borderColor = derma.noticeBorder
                )
                Text(
                    text = stringResource(R.string.onboarding_p3_notice_title),
                    style = dermaType(14, 20, FontWeight.Bold),
                    color = derma.noticeTitle
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.onboarding_p3_notice_body),
                modifier = Modifier.padding(start = 40.dp),
                style = dermaType(12, 20),
                color = derma.noticeBody
            )
        }
    }
}

@Composable
private fun ConsentCard(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Panel(
        shape = RoundedCornerShape(20.dp),
        color = derma.consentCard,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Checkbox) { onCheckedChange(!checked) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (checked) derma.emphasis else derma.pagerInactive.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                if (checked) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = colors.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.onboarding_p3_consent_title),
                    style = dermaType(16, 22, FontWeight.Medium),
                    color = colors.onSurface
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.onboarding_p3_consent_desc),
                    style = dermaType(12, 18),
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

/** هيرو الصفحة التالتة: مدارات + بطاقة "معتمد إرشادياً". */
@Composable
private fun CertifiedHero() {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme
    val orbit = colors.tertiary
    val blob = colors.outline.copy(alpha = 0.22f)

    Panel(
        shape = RoundedCornerShape(32.dp),
        color = derma.heroBase,
        modifier = Modifier.fillMaxWidth()
    ) {
        ScaledIllustration(designWidth = 350.dp, designHeight = 270.dp) {
            Canvas(Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height
                val d = 1.dp.toPx()

                drawCircle(
                    brush = Brush.radialGradient(listOf(derma.heroGlow, Color.Transparent), Offset(w - 30 * d, 40 * d), 150 * d),
                    radius = 150 * d, center = Offset(w - 30 * d, 40 * d)
                )
                drawCircle(
                    brush = Brush.radialGradient(listOf(blob, Color.Transparent), Offset(50 * d, h - 40 * d), 120 * d),
                    radius = 120 * d, center = Offset(50 * d, h - 40 * d)
                )

                val c = Offset(w / 2f, 111 * d)
                drawCircle(
                    orbit.copy(alpha = 0.5f), 81 * d, c,
                    style = Stroke(1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f * d, 6f * d)))
                )
                drawCircle(orbit.copy(alpha = 0.35f), 68 * d, c, style = Stroke(2.dp.toPx()))
                listOf(
                    Offset(c.x, c.y - 68 * d), Offset(c.x + 68 * d, c.y),
                    Offset(c.x, c.y + 68 * d), Offset(c.x - 68 * d, c.y)
                ).forEach { drawCircle(orbit, 4 * d, it) }
            }

            // البطاقة المركزية
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .absoluteOffset(y = 48.dp)
                    .size(width = 110.dp, height = 127.dp)
                    .shadow(14.dp, RoundedCornerShape(28.dp), ambientColor = derma.shadow, spotColor = derma.shadow)
                    .clip(RoundedCornerShape(28.dp))
                    .background(derma.heroTile)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 20.dp)
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(derma.heroTileCircle),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Eco,
                        contentDescription = null,
                        tint = derma.peach,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .absoluteOffset(x = 20.dp, y = 62.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(derma.emphasis),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = colors.onPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.onboarding_p3_hero_badge),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    style = dermaType(12, 16, FontWeight.Bold),
                    color = derma.heading
                )
            }

            // زخارف المدار
            Icon(
                Icons.Rounded.AutoAwesome, null, tint = derma.peach,
                modifier = Modifier
                    .absoluteOffset(x = 230.dp, y = 36.dp)
                    .size(20.dp)
            )
            Icon(
                Icons.Rounded.Spa, null, tint = derma.peach,
                modifier = Modifier
                    .absoluteOffset(x = 100.dp, y = 58.dp)
                    .size(16.dp)
            )
            Icon(
                Icons.Rounded.Verified, null, tint = derma.green,
                modifier = Modifier
                    .absoluteOffset(x = 222.dp, y = 162.dp)
                    .size(20.dp)
            )

            // الشريحة السفلية
            Panel(
                shape = CircleShape,
                color = derma.heroTile,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Dot(color = derma.peach)
                    Text(
                        text = stringResource(R.string.onboarding_p3_hero_chip),
                        style = dermaType(11, 16),
                        color = colors.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ═══════════════════════════ Shared ═══════════════════════════

/** بيرسم الـ illustration على مقاس التصميم (350dp) وبيصغّره/يكبّره حسب عرض الشاشة. */
@Composable
private fun ScaledIllustration(
    designWidth: Dp,
    designHeight: Dp,
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val scale = maxWidth / designWidth
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(designHeight * scale),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .requiredSize(designWidth, designHeight)
                    .graphicsLayer(scaleX = scale, scaleY = scale),
                content = content
            )
        }
    }
}

@Composable
private fun SkipPill(onClick: () -> Unit) {
    Panel(
        shape = CircleShape,
        color = MaterialTheme.derma.activeBadge,
        onClick = onClick
    ) {
        Text(
            text = stringResource(R.string.onboarding_skip),
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp),
            style = dermaType(14, 20),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LogoTile(logo: Painter?, size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF7A3E2B)),
        contentAlignment = Alignment.Center
    ) {
        if (logo != null) {
            Image(painter = logo, contentDescription = null, modifier = Modifier.fillMaxSize())
        } else {
            Text(
                text = "D",
                style = dermaType(20, 24, FontWeight.Bold),
                color = Color(0xFFFFF8F6)
            )
        }
    }
}

@Composable
private fun StepIndicator(
    step: Int,
    filled: Boolean,
    icon: ImageVector? = null,
    @StringRes label: Int? = null
) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Panel(
            shape = CircleShape,
            color = if (filled) colors.primary else derma.stepChip
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = derma.peach, modifier = Modifier.size(16.dp))
                }
                Text(
                    text = stringResource(R.string.onboarding_step, step, PAGE_COUNT),
                    style = dermaType(12, 16),
                    color = if (filled) colors.onPrimary else derma.peach
                )
            }
        }
        if (label != null) {
            Text(
                text = stringResource(label),
                style = dermaType(12, 16),
                color = derma.peach
            )
        }
    }
}

@Composable
private fun PageTitle(@StringRes text: Int, size: Int, lineHeight: Int) {
    Text(
        text = stringResource(text),
        style = dermaType(size, lineHeight, FontWeight.Bold),
        color = MaterialTheme.derma.heading
    )
}

@Composable
private fun PageBody(@StringRes text: Int) {
    Text(
        text = stringResource(text),
        style = dermaType(14, 23),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

// ═══════════════════════════ Bottom controls ═══════════════════════════

@Composable
private fun BottomControls(
    page: Int,
    horizontal: Dp,
    canFinish: Boolean,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onFinish: () -> Unit
) {
    val derma = MaterialTheme.derma

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontal)
            .padding(top = 4.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PagerDots(current = page)
        Spacer(Modifier.height(16.dp))

        when (page) {
            0 -> CtaButton(
                modifier = Modifier.fillMaxWidth().height(52.dp),
                onClick = onNext
            ) {
                Text(
                    text = stringResource(R.string.onboarding_next),
                    style = dermaType(16, 22, FontWeight.Medium),
                    color = derma.buttonText
                )
                Spacer(Modifier.width(10.dp))
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = derma.buttonText, modifier = Modifier.size(20.dp))
            }

            1 -> Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Panel(
                    shape = CircleShape,
                    color = derma.card,
                    withShadow = true,
                    onClick = onPrevious,
                    modifier = Modifier
                        .width(117.dp)
                        .height(52.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, null, tint = derma.heading, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.onboarding_previous),
                            style = dermaType(15, 22, FontWeight.Medium),
                            color = derma.heading
                        )
                    }
                }
                CtaButton(
                    modifier = Modifier.weight(1f).height(52.dp),
                    onClick = onNext
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_next),
                        style = dermaType(16, 22, FontWeight.Medium),
                        color = derma.buttonText
                    )
                    Spacer(Modifier.width(10.dp))
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = derma.buttonText, modifier = Modifier.size(20.dp))
                }
            }

            else -> {
                CtaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .alpha(if (canFinish) 1f else 0.5f),
                    enabled = canFinish,
                    onClick = onFinish,
                    spaced = true
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, null, tint = derma.buttonText, modifier = Modifier.size(22.dp))
                        Text(
                            text = stringResource(R.string.onboarding_start),
                            style = dermaType(16, 22, FontWeight.Bold),
                            color = derma.buttonText
                        )
                    }
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = derma.buttonText, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.height(16.dp))
                Panel(
                    shape = CircleShape,
                    color = derma.outlinePill,
                    onClick = onPrevious,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack, null,
                            tint = derma.brandText,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.onboarding_back),
                            style = dermaType(14, 20, FontWeight.Medium),
                            color = derma.brandText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CtaButton(
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    spaced: Boolean = false,
    content: @Composable () -> Unit
) {
    val derma = MaterialTheme.derma

    Row(
        modifier = modifier
            .shadow(14.dp, CircleShape, ambientColor = derma.buttonGlow, spotColor = derma.buttonGlow)
            .clip(CircleShape)
            .background(Brush.horizontalGradient(listOf(derma.ctaStart, derma.ctaEnd)))
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = if (spaced) 40.dp else 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (spaced) Arrangement.SpaceBetween else Arrangement.Center
    ) {
        content()
    }
}

@Composable
private fun PagerDots(current: Int) {
    val derma = MaterialTheme.derma

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(PAGE_COUNT) { index ->
            val active = index == current
            val width by animateDpAsState(if (active) 36.dp else 10.dp, label = "dot")
            Box(
                modifier = Modifier
                    .width(width)
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(if (active) derma.emphasis else derma.pagerInactive)
            )
        }
    }
}

// ═══════════════════════════ Preview ═══════════════════════════

@Preview(showBackground = true, widthDp = 390, heightDp = 800, locale = "ar")
@Composable
private fun OnboardingLightPreview() {
    DermaXRAITheme(darkTheme = false) { OnboardingScreen(onFinish = {}) }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 800, locale = "ar")
@Composable
private fun OnboardingDarkPreview() {
    DermaXRAITheme(darkTheme = true) { OnboardingScreen(onFinish = {}) }
}