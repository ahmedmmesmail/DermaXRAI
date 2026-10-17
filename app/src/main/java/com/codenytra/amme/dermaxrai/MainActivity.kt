package com.codenytra.amme.dermaxrai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.codenytra.amme.dermaxrai.features.home.HomeScreen
import com.codenytra.amme.dermaxrai.features.onboarding.OnboardingScreen
import com.codenytra.amme.dermaxrai.features.routine.RoutineScreen
import com.codenytra.amme.dermaxrai.features.settings.SettingsActivity
import com.codenytra.amme.dermaxrai.ui.navigation.CradleBottomBar
import com.codenytra.amme.dermaxrai.ui.navigation.AppDestinations
import com.codenytra.amme.dermaxrai.ui.theme.DermaXRAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DermaXRAITheme {
                DermaXRAIApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun DermaXRAIApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    val context = LocalContext.current
    if (true) {
        OnboardingScreen(
            onFinish = { },
            logo = painterResource(R.drawable.icon),
            wordmark = painterResource(R.drawable.banner),
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Image(
                            painter = painterResource(R.drawable.banner),
                            contentDescription = "DermaXRAI",
                            modifier = Modifier.height(50.dp)
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            context.startActivity(
                                Intent(
                                    context,
                                    SettingsActivity::class.java
                                )
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                CradleBottomBar(
                    currentDestination = currentDestination,
                    onDestinationClick = { currentDestination = it },
                    onFabClick = { /* افتح الكاميرا */ }
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            ) {

                val bottomPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())

                when (currentDestination) {
                    AppDestinations.HOME -> HomeScreen(
                        onScanClick = { },
                        onDetailsClick = { },
                        onRoutineClick = { },
                        modifier = Modifier.weight(1f),
                        contentPadding = bottomPadding
                    )

                    AppDestinations.SKIN -> RoutineScreen(
                        modifier = Modifier.weight(1f),
                        contentPadding = bottomPadding,
                        onLogRoutineClick = {},
                        onStepActionClick = {},
                        onTimeClick = {}
                    )
                }

            }
        }
    }
}