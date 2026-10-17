package com.codenytra.amme.dermaxrai.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/* ════════════════════════════════════════════════════════════════
 *  1) ألوان Material (الـ TopAppBar والـ BottomBar وباقي التطبيق)
 *     كلها hex مأخوذة من تصميم فيجما
 * ════════════════════════════════════════════════════════════════ */

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF7A3E2B),                // FAB + تاب الرئيسية
    onPrimary = Color(0xFFFFF8F6),
    primaryContainer = Color(0xFFFFDBCE),
    onPrimaryContainer = Color(0xFF5E2817),
    inversePrimary = Color(0xFFFFDBCE),

    secondary = Color(0xFF914A2D),
    onSecondary = Color(0xFFFFF8F6),
    secondaryContainer = Color(0xFFFFE2D7),
    onSecondaryContainer = Color(0xFF924B2E),

    tertiary = Color(0xFFFDA27F),
    onTertiary = Color(0xFF5E2817),
    tertiaryContainer = Color(0xFFFFE9E1),
    onTertiaryContainer = Color(0xFF5E2817),

    background = Color(0xFFFFF8F6),
    onBackground = Color(0xFF29170F),
    surface = Color(0xFFFFF8F6),                // TopAppBar
    onSurface = Color(0xFF29170F),
    surfaceVariant = Color(0xFFFFE9E1),
    onSurfaceVariant = Color(0xFF53433F),       // نص/أيقونات التنقل غير المختارة
    surfaceTint = Color(0xFF7A3E2B),
    inverseSurface = Color(0xFF29170F),
    inverseOnSurface = Color(0xFFFFF1EC),

    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = Color(0xFFA99690),
    outlineVariant = Color(0xFFFFDBCE),
    scrim = Color.Black,

    surfaceBright = Color(0xFFFFF8F6),
    surfaceDim = Color(0xFFF3D5CB),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFF1EC),
    surfaceContainer = Color(0xFFFFF8F6),       // BottomBar
    surfaceContainerHigh = Color(0xFFFFE2D7),
    surfaceContainerHighest = Color(0xFFFFDBCE)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA85A3C),                // FAB + تاب الرئيسية
    onPrimary = Color(0xFFFFF4E3),
    primaryContainer = Color(0xFF4A261C),
    onPrimaryContainer = Color(0xFFFFF4E3),
    inversePrimary = Color(0xFF7A3E2B),

    secondary = Color(0xFFC9805E),
    onSecondary = Color(0xFF1E0E09),
    secondaryContainer = Color(0xFF4A261C),
    onSecondaryContainer = Color(0xFFE58A68),

    tertiary = Color(0xFFE58A68),
    onTertiary = Color(0xFF1E0E09),
    tertiaryContainer = Color(0xFF3A1F16),
    onTertiaryContainer = Color(0xFFFFF4E3),

    background = Color(0xFF1E0E09),
    onBackground = Color(0xFFFFF4E3),
    surface = Color(0xFF29140D),                // TopAppBar
    onSurface = Color(0xFFFFF4E3),
    surfaceVariant = Color(0xFF3A1F16),
    onSurfaceVariant = Color(0xFFCDB5A8),       // نص/أيقونات التنقل غير المختارة
    surfaceTint = Color(0xFFA85A3C),
    inverseSurface = Color(0xFFFFF4E3),
    inverseOnSurface = Color(0xFF29140D),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = Color(0xFFAC9185),
    outlineVariant = Color(0xFF4D2D22),
    scrim = Color.Black,

    surfaceBright = Color(0xFF4A261C),
    surfaceDim = Color(0xFF1E0E09),
    surfaceContainerLowest = Color(0xFF180B07),
    surfaceContainerLow = Color(0xFF2A140D),
    surfaceContainer = Color(0xFF29140D),       // BottomBar
    surfaceContainerHigh = Color(0xFF3A1F16),
    surfaceContainerHighest = Color(0xFF4A261C)
)

/* ════════════════════════════════════════════════════════════════
 *  2) ألوان التصميم المخصوصة (كل مكوّن بلونه بالظبط زي فيجما)
 *     الاستخدام:  MaterialTheme.derma.card
 * ════════════════════════════════════════════════════════════════ */

@Immutable
class DermaColors(
    // نصوص
    val heading: Color,          // العناوين الكبيرة + قيمة نوع البشرة + نص "عرض التفاصيل"
    val bodyText: Color,         // النصوص الفرعية داخل الكروت
    val percentText: Color,
    val accentText: Color,       // نص الـ chips الصغيرة + نقطة الحالة
    // أيقونات
    val iconTint: Color,
    val iconMuted: Color,
    val chevron: Color,
    // أسطح
    val card: Color,
    val zoneBox: Color,
    val tipCard: Color,
    val disclaimer: Color,
    val chipStrong: Color,
    val chipSoft: Color,
    val circleStrong: Color,
    val circleSoft: Color,
    val track: Color,
    val border: Color,           // شفاف في الـ light
    val shadow: Color,           // شفاف في الـ dark
    // hero
    val heroGradient: List<Color>,
    // زرار الفحص
    val buttonStart: Color,
    val buttonEnd: Color,
    val buttonText: Color,
    // مؤشرات البشرة
    val acneStart: Color,
    val acneEnd: Color,
    val pigmentStart: Color,
    val pigmentEnd: Color,
    val drynessFill: Color,
    val acneDot: Color,
    val pigmentDot: Color,
    val drynessDot: Color,
    // صفحة الروتين
    val peach: Color,            // نص/أيقونات الإجراءات والـ chips (#FFB59A في الـ dark)
    val green: Color,            // توافق عالي + استجابة ممتازة + المكونات
    val greenSurface: Color,
    val greenBorder: Color,
    val tagBorder: Color,        // إطار علامة SPF
    val buttonGlow: Color,       // توهج زرار التسجيل
    // صفحة الإعدادات
    val emphasis: Color,         // دايرة الدرع (5E2817 في الـ light، A85A3C في الـ dark)
    val trustGradient: List<Color>,
    val activeBadge: Color,
    val privacyBadge: Color,
    val tile: Color,             // مربع القفل
    val rowCircle: Color,        // دوائر أيقونات الأذونات
    val headerCircle: Color,     // دايرة أيقونة العنوان
    val privacyCard: Color,
    val deepInset: Color,         // صف التشفير E2EE

    // الـ Onboarding
    val heroBase: Color,
    val heroGlow: Color,
    val heroTile: Color,
    val heroTileCircle: Color,
    val stepChip: Color,
    val noticeCard: Color,
    val noticeBorder: Color,
    val noticeCircle: Color,
    val noticeTitle: Color,
    val noticeBody: Color,
    val consentCard: Color,
    val pagerInactive: Color,
    val ctaStart: Color,
    val ctaEnd: Color,
    val outlinePill: Color,
    val brandText: Color,
    // Camera + Result
    val cameraBar: Color,
    val alertCard: Color,
    val alertTitle: Color,
    val alertBody: Color,
    val mapBase: Color
)

private val LightDerma = DermaColors(
    heading = Color(0xFF5E2817),
    bodyText = Color(0xFF53433F),
    percentText = Color(0xFF5E2817),
    accentText = Color(0xFF924B2E),
    iconTint = Color(0xFF5E2817),
    iconMuted = Color(0xFF53433F),
    chevron = Color(0xFF5E2817),
    card = Color(0xFFFFFFFF),
    zoneBox = Color(0xFFFFF1EC),
    tipCard = Color(0xFFFFF1EC),
    disclaimer = Color(0xFFFFEBE3),
    chipStrong = Color(0xFFFFE2D7),
    chipSoft = Color(0xFFFFE9E1),
    circleStrong = Color(0xFFFFDBCE),
    circleSoft = Color(0xFFFFE9E1),
    track = Color(0xFFFFE9E1),
    border = Color.Transparent,
    shadow = Color(0x2E5E2817),
    heroGradient = listOf(Color(0xFFFFE1D6), Color(0xFFFFECE5), Color(0xFFF3D5CB)),
    buttonStart = Color(0xFF602918),
    buttonEnd = Color(0xFF914A2D),
    buttonText = Color(0xFFFFF8F6),
    acneStart = Color(0xFF8F492D),
    acneEnd = Color(0xFF5F2918),
    pigmentStart = Color(0xFFF89E7B),
    pigmentEnd = Color(0xFF964E31),
    drynessFill = Color(0xFFFDA989),
    acneDot = Color(0xFF7A3E2B),
    pigmentDot = Color(0xFF7A3E2B),
    drynessDot = Color(0xFFFDA27F),
    peach = Color(0xFF924B2E),
    green = Color(0xFF4F7A3A),
    greenSurface = Color(0xFFE9F0DF),
    greenBorder = Color(0x404F7A3A),
    tagBorder = Color(0xFF924B2E),
    buttonGlow = Color(0x4D7A3E2B),
    emphasis = Color(0xFF5E2817),
    trustGradient = listOf(Color(0xFFFFE0D5), Color(0xFFFFF1EB)),
    activeBadge = Color(0xFFFFE9E1),
    privacyBadge = Color(0xFFFFE9E1),
    tile = Color(0xFFFFDBCE),
    rowCircle = Color(0xFFFFE9E1),
    headerCircle = Color(0xFFFFE9E1),
    privacyCard = Color(0xFFFFFFFF),
    deepInset = Color(0xFFFFF1EC),
    heroBase = Color(0xFFFFF1EC),
    heroGlow = Color(0xFFFFD8CA),
    heroTile = Color(0xFFFFE2D7),
    heroTileCircle = Color(0xFFFFDBCE),
    stepChip = Color(0xFFFFE2D7),
    noticeCard = Color(0xFFFFF0EE),
    noticeBorder = Color(0xFFEAA6A4),
    noticeCircle = Color(0xFFFFDAD6),
    noticeTitle = Color(0xFFBA1A1A),
    noticeBody = Color(0xFF53433F),
    consentCard = Color(0xFFFFE2D7),
    pagerInactive = Color(0xFFD8C2BA),
    ctaStart = Color(0xFF602918),
    ctaEnd = Color(0xFF914A2D),
    outlinePill = Color(0xFFFFE2D7),
    brandText = Color(0xFF5E2817),
    cameraBar = Color(0xCC8C7C76),
    alertCard = Color(0xFFFFDAD6),
    alertTitle = Color(0xFF9C1414),
    alertBody = Color(0xFFBA1A1A),
    mapBase = Color(0xFFFFF0EA)
)

private val DarkDerma = DermaColors(
    heading = Color(0xFFFFF4E3),
    bodyText = Color(0xFFE4D8C8),
    percentText = Color(0xFFE58A68),
    accentText = Color(0xFFE58A68),
    iconTint = Color(0xFFE58A68),
    iconMuted = Color(0xFFCDB5A8),
    chevron = Color(0xFFCDB5A8),
    card = Color(0xFF3A1F16),
    zoneBox = Color(0xFF2A140D),
    tipCard = Color(0xFF3A1F16),
    disclaimer = Color(0xFF321A12),
    chipStrong = Color(0xFF4A261C),
    chipSoft = Color(0xFF4A261C),
    circleStrong = Color(0xFF4A261C),
    circleSoft = Color(0xFF4A261C),
    track = Color(0xFF2A140D),
    border = Color(0xFF4D2D22),
    shadow = Color.Transparent,
    heroGradient = listOf(Color(0xFF502B1D), Color(0xFF3A1F16), Color(0xFF4F2E21)),
    buttonStart = Color(0xFFA85A3C),
    buttonEnd = Color(0xFFC9805E),
    buttonText = Color(0xFFFFF4E3),
    acneStart = Color(0xFFAB5D3F),
    acneEnd = Color(0xFFC77E5C),
    pigmentStart = Color(0xFF7C381C),
    pigmentEnd = Color(0xFFA45739),
    drynessFill = Color(0xFF8F4C33),
    acneDot = Color(0xFFE58A68),
    pigmentDot = Color(0xFFC9805E),
    drynessDot = Color(0xFF793519),
    peach = Color(0xFFFFB59A),
    green = Color(0xFF8DB07C),
    greenSurface = Color(0xFF392B1E),
    greenBorder = Color(0xFF48432E),
    tagBorder = Color(0xFF934F35),
    buttonGlow = Color(0x66A85A3C),
    emphasis = Color(0xFFA85A3C),
    trustGradient = listOf(Color(0xFF49271B), Color(0xFF3A1F15)),
    activeBadge = Color(0xFF2A140D),
    privacyBadge = Color(0xFF3A1F16),
    tile = Color(0xFF3A1F16),
    rowCircle = Color(0xFF2A140D),
    headerCircle = Color(0xFF3A1F16),
    privacyCard = Color(0xFF2A140D),
    deepInset = Color(0xFF1E0E09),
    heroBase = Color(0xFF281612),
    heroGlow = Color(0xFF48281C),
    heroTile = Color(0xFF180A06),
    heroTileCircle = Color(0xFF2C1A16),
    stepChip = Color(0xFF2A140D),
    noticeCard = Color(0xFF2C1412),
    noticeBorder = Color(0xFF5C403E),
    noticeCircle = Color(0xFF563430),
    noticeTitle = Color(0xFFFFDAD6),
    noticeBody = Color(0xFFEAC6C2),
    consentCard = Color(0xFF281612),
    pagerInactive = Color(0xFF48342E),
    ctaStart = Color(0xFFA85A3C),
    ctaEnd = Color(0xFFA85A3C),
    outlinePill = Color(0xFF1E0E09),
    brandText = Color(0xFFFFB59A),
    cameraBar = Color(0xFF180A06),
    alertCard = Color(0xFF3C1C14),
    alertTitle = Color(0xFFE58A68),
    alertBody = Color(0xFFC9805E),
    mapBase = Color(0xFF2A140C)
)

private val LocalDermaColors = staticCompositionLocalOf { LightDerma }

val MaterialTheme.derma: DermaColors
    @Composable
    @ReadOnlyComposable
    get() = LocalDermaColors.current

@Composable
fun DermaXRAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalDermaColors provides if (darkTheme) DarkDerma else LightDerma) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}