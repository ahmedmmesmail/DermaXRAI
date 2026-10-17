package com.codenytra.amme.dermaxrai.features.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SettingsBrightness
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.MainActivity
import com.codenytra.amme.dermaxrai.R
import com.codenytra.amme.dermaxrai.ui.Dot
import com.codenytra.amme.dermaxrai.ui.IconCircle
import com.codenytra.amme.dermaxrai.ui.Panel
import com.codenytra.amme.dermaxrai.ui.dermaType
import com.codenytra.amme.dermaxrai.ui.theme.DermaXRAITheme
import com.codenytra.amme.dermaxrai.ui.theme.derma

class SettingsActivity : ComponentActivity() {
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
                                    context.startActivity(
                                        Intent(
                                            context,
                                            MainActivity::class.java
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                                        contentDescription = "Settings"
                                    )
                                }
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    SettingsScreen(
                        modifier = Modifier.padding(innerPadding),
                        themeMode = ThemeMode.SYSTEM,
                        onThemeModeChange = {},
                        language = AppLanguage.EN,
                        onLanguageChange = {},
                        cameraPermissionEnabled = true,
                        onCameraPermissionChange = {}
                    )
                }
            }
        }
    }
}



enum class ThemeMode { LIGHT, DARK, SYSTEM }

enum class AppLanguage { AR, EN }

// ───────────────────────── Screen ─────────────────────────

/**
 * كل الحالات (state) جاية من برّه (hoisted): الـ theme واللغة والكاميرا،
 * فالـ ViewModel أو الـ DataStore بتاعك هو اللي يحفظ الاختيار.
 */
@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    cameraPermissionEnabled: Boolean,
    onCameraPermissionChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    appLogo: Painter? = null,
    versionName: String = "1.0.0",
    buildNumber: String = "412",
    cacheSizeMb: String = "14.2",
    onPrivacyPolicyClick: () -> Unit = {},
    onCacheLogClick: () -> Unit = {},
    onReleaseNotesClick: () -> Unit = {},
    onLicensesClick: () -> Unit = {},
    onSupportClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 8.dp, bottom = 24.dp)
    ) {
        SettingsHeader()
        Spacer(Modifier.height(24.dp))
        TrustedProcessorCard()

        Spacer(Modifier.height(26.dp))
        SectionHeader(R.string.settings_section_appearance, Icons.Rounded.Palette)
        Spacer(Modifier.height(14.dp))
        AppearanceCard(
            themeMode = themeMode,
            onThemeModeChange = onThemeModeChange,
            language = language,
            onLanguageChange = onLanguageChange
        )

        Spacer(Modifier.height(26.dp))
        SectionHeader(R.string.settings_section_privacy, Icons.Rounded.Security)
        Spacer(Modifier.height(14.dp))
        PrivacyGuaranteeCard()
        Spacer(Modifier.height(8.dp))
        PermissionsCard(
            cameraEnabled = cameraPermissionEnabled,
            onCameraChange = onCameraPermissionChange,
            cacheSizeMb = cacheSizeMb,
            onPrivacyPolicyClick = onPrivacyPolicyClick,
            onCacheLogClick = onCacheLogClick
        )

        Spacer(Modifier.height(28.dp))
        AboutBlock(appLogo = appLogo, versionName = versionName, buildNumber = buildNumber)
        Spacer(Modifier.height(17.dp))
        MedicalDisclaimerCard()
        Spacer(Modifier.height(20.dp))
        FooterBlock(
            onReleaseNotesClick = onReleaseNotesClick,
            onLicensesClick = onLicensesClick,
            onSupportClick = onSupportClick
        )
    }
}

// ───────────────────────── Header ─────────────────────────

@Composable
private fun SettingsHeader() {
    val derma = MaterialTheme.derma

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.settings_title),
                style = dermaType(22, 30, FontWeight.SemiBold),
                color = derma.heading
            )
            Text(
                text = stringResource(R.string.settings_subtitle),
                style = dermaType(12, 18),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconCircle(
            icon = Icons.Rounded.Tune,
            size = 48.dp,
            container = derma.headerCircle,
            tint = derma.iconTint
        )
    }
}

@Composable
private fun TrustedProcessorCard() {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Panel(
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent,
        withShadow = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .background(Brush.horizontalGradient(derma.trustGradient))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconCircle(
                icon = Icons.Rounded.VerifiedUser,
                size = 48.dp,
                container = derma.emphasis,
                tint = colors.onPrimary,
                borderColor = Color.Transparent
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.settings_trusted_title),
                    style = dermaType(16, 22, FontWeight.Medium),
                    color = derma.heading
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.settings_trusted_desc),
                    style = dermaType(12, 19),
                    color = colors.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(@StringRes title: Int, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.derma.accentText,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = stringResource(title),
            style = dermaType(18, 24, FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// ───────────────────────── Appearance ─────────────────────────

@Composable
private fun AppearanceCard(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit
) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    val activeLabel = when (themeMode) {
        ThemeMode.LIGHT -> R.string.settings_theme_active_light
        ThemeMode.DARK -> R.string.settings_theme_active_dark
        ThemeMode.SYSTEM -> R.string.settings_theme_active_auto
    }
    val headerIcon = when (themeMode) {
        ThemeMode.LIGHT -> Icons.Rounded.LightMode
        ThemeMode.DARK -> Icons.Rounded.DarkMode
        ThemeMode.SYSTEM -> Icons.Rounded.SettingsBrightness
    }

    Panel(
        shape = RoundedCornerShape(28.dp),
        color = derma.card,
        withShadow = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // مظهر الواجهة + الحالة
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = headerIcon,
                        contentDescription = null,
                        tint = derma.iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = stringResource(R.string.settings_theme_title),
                            style = dermaType(16, 22, FontWeight.Medium),
                            color = colors.onSurface
                        )
                        Text(
                            text = stringResource(R.string.settings_theme_subtitle),
                            style = dermaType(12, 16),
                            color = colors.onSurfaceVariant
                        )
                    }
                }
                Panel(shape = CircleShape, color = derma.activeBadge) {
                    Text(
                        text = stringResource(activeLabel),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp),
                        style = dermaType(11, 16, FontWeight.Medium),
                        color = derma.accentText
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // اختيار الـ theme
            Panel(
                shape = CircleShape,
                color = derma.zoneBox,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(6.dp)) {
                    ThemeOption(
                        label = R.string.settings_theme_light,
                        icon = Icons.Rounded.LightMode,
                        selected = themeMode == ThemeMode.LIGHT,
                        onClick = { onThemeModeChange(ThemeMode.LIGHT) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOption(
                        label = R.string.settings_theme_dark,
                        icon = Icons.Rounded.DarkMode,
                        selected = themeMode == ThemeMode.DARK,
                        onClick = { onThemeModeChange(ThemeMode.DARK) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOption(
                        label = R.string.settings_theme_auto,
                        icon = Icons.Rounded.SettingsBrightness,
                        selected = themeMode == ThemeMode.SYSTEM,
                        onClick = { onThemeModeChange(ThemeMode.SYSTEM) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // لغة التطبيق
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Translate,
                        contentDescription = null,
                        tint = derma.iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.settings_language_title),
                            style = dermaType(16, 22, FontWeight.Medium),
                            color = colors.onSurface
                        )
                        Text(
                            text = stringResource(R.string.settings_language_subtitle),
                            style = dermaType(12, 18),
                            color = colors.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                LanguageSwitch(selected = language, onSelect = onLanguageChange)
            }
        }
    }
}

@Composable
private fun ThemeOption(
    @StringRes label: Int,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val content = if (selected) colors.onPrimary else colors.onSurfaceVariant

    Row(
        modifier = modifier
            .height(26.dp)
            .clip(CircleShape)
            .background(if (selected) colors.primary else Color.Transparent)
            .clickable(role = Role.RadioButton, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = content,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = stringResource(label),
            style = dermaType(13, 18, FontWeight.Medium),
            color = content
        )
    }
}

@Composable
private fun LanguageSwitch(selected: AppLanguage, onSelect: (AppLanguage) -> Unit) {
    Panel(shape = CircleShape, color = MaterialTheme.derma.zoneBox) {
        Row(modifier = Modifier.padding(4.dp)) {
            LanguageOption(
                label = R.string.settings_language_arabic,
                selected = selected == AppLanguage.AR,
                onClick = { onSelect(AppLanguage.AR) }
            )
            LanguageOption(
                label = R.string.settings_language_english,
                selected = selected == AppLanguage.EN,
                onClick = { onSelect(AppLanguage.EN) }
            )
        }
    }
}

@Composable
private fun LanguageOption(@StringRes label: Int, selected: Boolean, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .height(24.dp)
            .clip(CircleShape)
            .background(if (selected) colors.primary else Color.Transparent)
            .clickable(role = Role.RadioButton, onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(label),
            style = dermaType(13, 18, FontWeight.Medium),
            color = if (selected) colors.onPrimary else colors.onSurfaceVariant
        )
    }
}

// ───────────────────────── Privacy ─────────────────────────

@Composable
private fun PrivacyGuaranteeCard() {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Panel(
        shape = RoundedCornerShape(28.dp),
        color = derma.privacyCard,
        withShadow = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // مربع القفل
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(derma.tile),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = derma.iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.settings_guarantee_title),
                            modifier = Modifier.weight(1f),
                            style = dermaType(20, 28, FontWeight.Medium),
                            color = derma.heading
                        )
                        Spacer(Modifier.width(8.dp))
                        Panel(
                            shape = RoundedCornerShape(18.dp),
                            color = derma.privacyBadge,
                            modifier = Modifier.width(59.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.settings_guarantee_badge),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = dermaType(11, 15, FontWeight.SemiBold),
                                color = derma.accentText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.settings_guarantee_body),
                        style = dermaType(13, 20),
                        color = colors.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Panel(
                shape = RoundedCornerShape(2.dp),
                color = derma.deepInset,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Dot(color = derma.accentText)
                        Text(
                            text = stringResource(R.string.settings_e2ee_label),
                            style = dermaType(12, 16),
                            color = colors.onSurface
                        )
                    }
                    Text(
                        text = stringResource(R.string.settings_e2ee_status),
                        style = dermaType(11, 16),
                        color = derma.accentText
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionsCard(
    cameraEnabled: Boolean,
    onCameraChange: (Boolean) -> Unit,
    cacheSizeMb: String,
    onPrivacyPolicyClick: () -> Unit,
    onCacheLogClick: () -> Unit
) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Panel(
        shape = RoundedCornerShape(28.dp),
        color = derma.card,
        withShadow = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            SettingRow(
                icon = Icons.Rounded.PhotoCamera,
                title = stringResource(R.string.settings_camera_title),
                subtitle = stringResource(R.string.settings_camera_subtitle),
                onClick = { onCameraChange(!cameraEnabled) }
            ) {
                Switch(
                    checked = cameraEnabled,
                    onCheckedChange = onCameraChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colors.onPrimary,
                        checkedTrackColor = colors.primary,
                        checkedBorderColor = Color.Transparent,
                        uncheckedThumbColor = colors.onSurfaceVariant,
                        uncheckedTrackColor = derma.zoneBox,
                        uncheckedBorderColor = colors.outlineVariant
                    )
                )
            }
            SettingRow(
                icon = Icons.Rounded.PrivacyTip,
                title = stringResource(R.string.settings_policy_title),
                subtitle = stringResource(R.string.settings_policy_subtitle),
                onClick = onPrivacyPolicyClick
            ) { Chevron() }
            SettingRow(
                icon = Icons.Rounded.Archive,
                title = stringResource(R.string.settings_cache_title),
                subtitle = stringResource(R.string.settings_cache_subtitle, cacheSizeMb),
                onClick = onCacheLogClick
            ) { Chevron() }
        }
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    trailing: @Composable () -> Unit
) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(role = Role.Button, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconCircle(
            icon = icon,
            size = 40.dp,
            container = derma.rowCircle,
            tint = derma.iconTint,
            iconSize = 20.dp
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = dermaType(15, 22, FontWeight.Medium),
                color = colors.onSurface
            )
            Text(
                text = subtitle,
                style = dermaType(11, 16),
                color = colors.onSurfaceVariant
            )
        }
        trailing()
    }
}

@Composable
private fun Chevron() {
    Icon(
        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
        contentDescription = null,
        tint = MaterialTheme.derma.chevron,
        modifier = Modifier.size(20.dp)
    )
}

// ───────────────────────── About + disclaimer + footer ─────────────────────────

@Composable
private fun AboutBlock(appLogo: Painter?, versionName: String, buildNumber: String) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Panel(
            shape = RoundedCornerShape(20.dp),
            color = derma.card,
            withShadow = true
        ) {
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                if (appLogo != null) {
                    Image(
                        painter = appLogo,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(derma.emphasis),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "D",
                            style = dermaType(20, 24, FontWeight.Bold),
                            color = colors.onPrimary
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = dermaType(16, 22, FontWeight.Bold),
            color = derma.heading
        )
        Text(
            text = stringResource(R.string.settings_about_version, versionName, buildNumber),
            style = dermaType(11, 16),
            color = colors.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Dot(color = derma.accentText, size = 7.dp)
            Text(
                text = stringResource(R.string.settings_about_engine),
                style = dermaType(11, 16),
                color = derma.accentText
            )
        }
    }
}

@Composable
private fun MedicalDisclaimerCard() {
    val derma = MaterialTheme.derma

    Panel(
        shape = RoundedCornerShape(28.dp),
        color = derma.tipCard,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.HealthAndSafety,
                    contentDescription = null,
                    tint = derma.accentText,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(R.string.settings_disclaimer_title),
                    style = dermaType(14, 20, FontWeight.Bold),
                    color = derma.heading
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.settings_disclaimer_body),
                modifier = Modifier.padding(start = 28.dp),
                style = dermaType(11, 20),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FooterBlock(
    onReleaseNotesClick: () -> Unit,
    onLicensesClick: () -> Unit,
    onSupportClick: () -> Unit
) {
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    val accent = MaterialTheme.derma.accentText

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                R.string.settings_copyright,
                stringResource(R.string.app_name),
                "2025"
            ),
            style = dermaType(11, 16),
            color = muted,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            FooterLink(R.string.settings_link_release_notes, onReleaseNotesClick)
            Dot(color = accent, size = 4.dp)
            FooterLink(R.string.settings_link_licenses, onLicensesClick)
            Dot(color = accent, size = 4.dp)
            FooterLink(R.string.settings_link_support, onSupportClick)
        }
    }
}

@Composable
private fun FooterLink(@StringRes text: Int, onClick: () -> Unit) {
    Text(
        text = stringResource(text),
        modifier = Modifier.clickable(role = Role.Button, onClick = onClick),
        style = dermaType(11, 16),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
