package com.codenytra.amme.dermaxrai.features.routine

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CleanHands
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Grain
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.R
import com.codenytra.amme.dermaxrai.ui.IconCircle
import com.codenytra.amme.dermaxrai.ui.Panel
import com.codenytra.amme.dermaxrai.ui.dermaType
import com.codenytra.amme.dermaxrai.ui.theme.DermaXRAITheme
import com.codenytra.amme.dermaxrai.ui.theme.derma

private enum class RoutineTab { MORNING, EVENING }

// ───────────────────────── Data ─────────────────────────

private sealed interface StepMeta {
    data class Label(
        val icon: ImageVector,
        @StringRes val text: Int,
        val positive: Boolean = false
    ) : StepMeta

    data class Tag(@StringRes val text: Int) : StepMeta
}

private data class RoutineStep(
    val number: Int,
    @StringRes val category: Int,
    @StringRes val title: Int,
    @StringRes val description: Int,
    val icon: ImageVector,
    val meta: StepMeta,
    val footerIcon: ImageVector,
    @StringRes val footerText: Int,
    val footerPositive: Boolean = false,
    @StringRes val action: Int
)

private val morningSteps = listOf(
    RoutineStep(
        number = 1,
        category = R.string.routine_category_cleansing,
        title = R.string.routine_step1_title,
        description = R.string.routine_step1_desc,
        icon = Icons.Rounded.CleanHands,
        meta = StepMeta.Label(Icons.Rounded.Timer, R.string.routine_step1_meta),
        footerIcon = Icons.Rounded.CheckCircle,
        footerText = R.string.routine_step1_footer,
        footerPositive = true,
        action = R.string.routine_step1_action
    ),
    RoutineStep(
        number = 2,
        category = R.string.routine_category_treatment,
        title = R.string.routine_step2_title,
        description = R.string.routine_step2_desc,
        icon = Icons.Rounded.WaterDrop,
        meta = StepMeta.Label(Icons.Rounded.TrendingUp, R.string.routine_step2_meta, positive = true),
        footerIcon = Icons.Rounded.Grain,
        footerText = R.string.routine_step2_footer,
        action = R.string.routine_step2_action
    ),
    RoutineStep(
        number = 3,
        category = R.string.routine_category_hydration,
        title = R.string.routine_step3_title,
        description = R.string.routine_step3_desc,
        icon = Icons.Rounded.MonitorHeart,
        meta = StepMeta.Label(Icons.Rounded.Shield, R.string.routine_step3_meta),
        footerIcon = Icons.Rounded.AutoAwesome,
        footerText = R.string.routine_step3_footer,
        action = R.string.routine_step3_action
    ),
    RoutineStep(
        number = 4,
        category = R.string.routine_category_sun,
        title = R.string.routine_step4_title,
        description = R.string.routine_step4_desc,
        icon = Icons.Rounded.WbSunny,
        meta = StepMeta.Tag(R.string.routine_tag_spf),
        footerIcon = Icons.Rounded.Schedule,
        footerText = R.string.routine_step4_footer,
        action = R.string.routine_step4_action
    )
)

private data class Ingredient(val icon: ImageVector, @StringRes val name: Int)

private val ingredients = listOf(
    Ingredient(Icons.Rounded.Eco, R.string.routine_ingredient_niacinamide),
    Ingredient(Icons.Rounded.WaterDrop, R.string.routine_ingredient_hyaluronic),
    Ingredient(Icons.Rounded.Spa, R.string.routine_ingredient_green_tea)
)

// ───────────────────────── Screen ─────────────────────────

@Composable
fun RoutineScreen(
    modifier: Modifier = Modifier,
    onStepActionClick: (stepNumber: Int) -> Unit = {},
    onTimeClick: () -> Unit = {},
    onLogRoutineClick: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues()
) {
    var tab by rememberSaveable { mutableStateOf(RoutineTab.MORNING) }
    var reminderEnabled by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = contentPadding.calculateBottomPadding() + 12.dp,)
    ) {
        StatusRow()
        Spacer(Modifier.height(6.dp))
        TitleBlock()
        Spacer(Modifier.height(24.dp))
        RoutineTabs(selected = tab, onSelect = { tab = it })
        Spacer(Modifier.height(24.dp))

        if (tab == RoutineTab.MORNING) {
            IngredientsCard()
            Spacer(Modifier.height(24.dp))
            StepsHeader(count = morningSteps.size)
            Spacer(Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                morningSteps.forEach { step ->
                    StepCard(step = step, onActionClick = { onStepActionClick(step.number) })
                }
            }
        } else {
            EveningPlaceholder()
        }

        Spacer(Modifier.height(24.dp))
        ReminderCard(
            enabled = reminderEnabled,
            onEnabledChange = { reminderEnabled = it },
            onTimeClick = onTimeClick
        )
        Spacer(Modifier.height(24.dp))
        LogButton(
            text = if (tab == RoutineTab.MORNING) R.string.routine_log_morning else R.string.routine_log_evening,
            onClick = onLogRoutineClick
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.routine_consistency_note),
            modifier = Modifier.fillMaxWidth(),
            style = dermaType(11, 16),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// ───────────────────────── Header ─────────────────────────

@Composable
private fun StatusRow() {
    val derma = MaterialTheme.derma

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Panel(shape = CircleShape, color = derma.zoneBox) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Verified,
                    contentDescription = null,
                    tint = derma.peach,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = stringResource(R.string.routine_updated),
                    style = dermaType(11, 16),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = stringResource(R.string.routine_accuracy, 96),
            style = dermaType(11, 16),
            color = derma.peach
        )
    }
}

@Composable
private fun TitleBlock() {
    Column {
        Text(
            text = stringResource(R.string.routine_title),
            style = dermaType(28, 36, FontWeight.Bold),
            color = MaterialTheme.derma.heading
        )
        Text(
            text = stringResource(R.string.routine_subtitle),
            style = dermaType(14, 22),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ───────────────────────── Tabs ─────────────────────────

@Composable
private fun RoutineTabs(selected: RoutineTab, onSelect: (RoutineTab) -> Unit) {
    Panel(
        shape = CircleShape,
        color = MaterialTheme.derma.zoneBox,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            TabItem(
                text = R.string.routine_tab_morning,
                icon = Icons.Rounded.WbSunny,
                selected = selected == RoutineTab.MORNING,
                onClick = { onSelect(RoutineTab.MORNING) },
                modifier = Modifier.weight(1f)
            )
            TabItem(
                text = R.string.routine_tab_evening,
                icon = Icons.Rounded.NightsStay,
                selected = selected == RoutineTab.EVENING,
                onClick = { onSelect(RoutineTab.EVENING) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TabItem(
    @StringRes text: Int,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val contentColor = if (selected) colors.onPrimary else colors.onSurfaceVariant

    Row(
        modifier = modifier
            .height(38.dp)
            .clip(CircleShape)
            .background(if (selected) colors.primary else Color.Transparent)
            .clickable(role = Role.Tab, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.size(6.dp))
        Text(
            text = stringResource(text),
            style = dermaType(14, 20, FontWeight.Medium),
            color = contentColor
        )
    }
}

// ───────────────────────── Ingredients ─────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IngredientsCard() {
    val derma = MaterialTheme.derma

    Panel(
        shape = RoundedCornerShape(16.dp),
        color = derma.zoneBox,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Science,
                        contentDescription = null,
                        tint = derma.peach,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.routine_ingredients_title),
                        style = dermaType(14, 18, FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = stringResource(R.string.routine_ingredients_match),
                    style = dermaType(11, 16),
                    color = derma.green
                )
            }

            Spacer(Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ingredients.forEach { ingredient ->
                    Panel(
                        shape = CircleShape,
                        color = derma.greenSurface,
                        borderColor = derma.greenBorder
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = ingredient.icon,
                                contentDescription = null,
                                tint = derma.green,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = stringResource(ingredient.name),
                                style = dermaType(12, 18),
                                color = derma.green
                            )
                        }
                    }
                }
            }
        }
    }
}

// ───────────────────────── Steps ─────────────────────────

@Composable
private fun StepsHeader(count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.routine_steps_title),
            style = dermaType(22, 28, FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(R.string.routine_steps_count, count),
            style = dermaType(11, 16),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StepCard(step: RoutineStep, onActionClick: () -> Unit) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Panel(
        shape = RoundedCornerShape(20.dp),
        color = derma.card,
        withShadow = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconCircle(
                    icon = step.icon,
                    size = 48.dp,
                    container = derma.zoneBox,
                    tint = derma.peach,
                    iconSize = 24.dp,
                    borderColor = colors.outlineVariant
                )
                Spacer(Modifier.size(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // الخطوة + الـ meta
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Panel(
                            shape = RoundedCornerShape(6.dp),
                            color = derma.zoneBox,
                            borderColor = Color.Transparent
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.routine_step_label,
                                    step.number,
                                    stringResource(step.category)
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                style = dermaType(11, 15),
                                color = derma.peach
                            )
                        }
                        StepMetaView(step.meta)
                    }

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = stringResource(step.title),
                        style = dermaType(20, 28, FontWeight.Medium),
                        color = colors.onSurface
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = stringResource(step.description),
                        style = dermaType(12, 18),
                        color = colors.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = colors.outlineVariant)
            Spacer(Modifier.height(11.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = step.footerIcon,
                        contentDescription = null,
                        tint = if (step.footerPositive) derma.green else colors.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = stringResource(step.footerText),
                        style = dermaType(12, 16),
                        color = colors.onSurfaceVariant
                    )
                }
                Text(
                    text = stringResource(step.action),
                    modifier = Modifier.clickable(role = Role.Button, onClick = onActionClick),
                    style = dermaType(12, 16, FontWeight.Medium),
                    color = derma.peach
                )
            }
        }
    }
}

@Composable
private fun StepMetaView(meta: StepMeta) {
    val derma = MaterialTheme.derma

    when (meta) {
        is StepMeta.Label -> {
            val tint = if (meta.positive) derma.green else MaterialTheme.colorScheme.onSurfaceVariant
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = meta.icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = stringResource(meta.text),
                    style = dermaType(11, 16),
                    color = tint
                )
            }
        }

        is StepMeta.Tag -> Panel(
            shape = RoundedCornerShape(6.dp),
            color = Color.Transparent,
            borderColor = derma.tagBorder
        ) {
            Text(
                text = stringResource(meta.text),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                style = dermaType(10, 14, FontWeight.Bold),
                color = derma.heading
            )
        }
    }
}

@Composable
private fun EveningPlaceholder() {
    Panel(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.derma.zoneBox,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.routine_evening_placeholder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            style = dermaType(14, 20),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// ───────────────────────── Reminder ─────────────────────────

@Composable
private fun ReminderCard(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    onTimeClick: () -> Unit
) {
    val derma = MaterialTheme.derma
    val colors = MaterialTheme.colorScheme

    Panel(
        shape = RoundedCornerShape(20.dp),
        color = derma.zoneBox,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconCircle(
                    icon = Icons.Rounded.NotificationsActive,
                    size = 40.dp,
                    container = colors.primary,
                    tint = colors.onPrimary,
                    iconSize = 20.dp,
                    borderColor = Color.Transparent
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.routine_reminder_title),
                        style = dermaType(18, 24, FontWeight.Medium),
                        color = colors.onSurface
                    )
                    Text(
                        text = stringResource(R.string.routine_reminder_subtitle),
                        style = dermaType(12, 16),
                        color = colors.onSurfaceVariant
                    )
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = onEnabledChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colors.onPrimary,
                        checkedTrackColor = colors.primary,
                        checkedBorderColor = Color.Transparent,
                        uncheckedThumbColor = colors.onSurfaceVariant,
                        uncheckedTrackColor = derma.card,
                        uncheckedBorderColor = colors.outlineVariant
                    )
                )
            }

            Spacer(Modifier.height(18.dp))

            Panel(
                shape = CircleShape,
                color = derma.card,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Alarm,
                            contentDescription = null,
                            tint = derma.peach,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(R.string.routine_reminder_time_label),
                            style = dermaType(14, 20),
                            color = colors.onSurfaceVariant
                        )
                    }

                    Panel(
                        shape = CircleShape,
                        color = derma.zoneBox,
                        onClick = onTimeClick
                    ) {
                        Row(
                            modifier = Modifier
                                .height(42.dp)
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.routine_reminder_time),
                                style = dermaType(16, 22, FontWeight.Medium),
                                color = colors.onSurface
                            )
                            Text(
                                text = stringResource(R.string.routine_reminder_am),
                                style = dermaType(12, 16),
                                color = colors.onSurfaceVariant
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = null,
                                tint = colors.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ───────────────────────── CTA ─────────────────────────

@Composable
private fun LogButton(@StringRes text: Int, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val glow = MaterialTheme.derma.buttonGlow

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(14.dp, CircleShape, ambientColor = glow, spotColor = glow),
        shape = CircleShape,
        color = colors.primary
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(text),
                style = dermaType(16, 22, FontWeight.Medium),
                color = colors.onPrimary
            )
            Spacer(Modifier.size(8.dp))
            Icon(
                imageVector = Icons.Rounded.TaskAlt,
                contentDescription = null,
                tint = colors.onPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ───────────────────────── Preview ─────────────────────────

@Preview(showBackground = true, widthDp = 390, heightDp = 1500, locale = "ar")
@Composable
private fun RoutineScreenDarkPreview() {
    DermaXRAITheme(darkTheme = true) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) { RoutineScreen() }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 1500, locale = "ar")
@Composable
private fun RoutineScreenLightPreview() {
    DermaXRAITheme(darkTheme = false) {
        Box(Modifier.background(MaterialTheme.colorScheme.background)) { RoutineScreen() }
    }
}