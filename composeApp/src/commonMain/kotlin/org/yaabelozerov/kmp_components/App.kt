package org.yaabelozerov.kmp_components

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Nav(val route: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
  MAIN("main", Icons.Filled.Home, Icons.Outlined.Home)
}

@Composable
@Preview
fun App() {
  val navCtrl = rememberNavController()
  val currentRoute = navCtrl.currentBackStackEntryAsState().value?.destination?.route
  val coroutineScope = rememberCoroutineScope()

  AppThemeConfiguration(
      darkTheme = isSystemInDarkTheme(),
      lightColors = appLightScheme,
      darkColors = appDarkScheme,
      typography = makeTypography()) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
              Nav.entries.forEach {
                val selected = it.route == currentRoute
                item(
                    selected = selected,
                    icon = {
                      Icon(
                          if (selected) it.selectedIcon else it.unselectedIcon,
                          contentDescription = it.route)
                    },
                    onClick = { if (!selected) navCtrl.navigate(it.route) })
              }
            }) {
              Scaffold { innerPadding ->
                NavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navCtrl,
                    startDestination = Nav.MAIN.route) {
                      composable(Nav.MAIN.route) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(16.dp)) {
                              item {
                                Text(
                                    "Hello world!",
                                    style = MaterialTheme.typography.displayMedium,
                                    modifier = Modifier.padding(bottom = 8.dp))
                              }
                              item {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()) {
                                      Row(
                                          modifier = Modifier.fillMaxWidth(),
                                          horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                            var isEnabled by remember { mutableStateOf(false) }
                                            Card(
                                                colors =
                                                    if (isEnabled)
                                                        CardDefaults.cardColors()
                                                            .copy(
                                                                containerColor =
                                                                    MaterialTheme.colorScheme
                                                                        .primaryContainer)
                                                    else CardDefaults.cardColors(),
                                                modifier =
                                                    Modifier.height(128.dp)
                                                        .weight(1f)
                                                        .bouncyClickable(
                                                            onClick = {
                                                              isEnabled = !isEnabled
                                                            })) {}
                                            Card(
                                                modifier =
                                                    Modifier.height(128.dp)
                                                        .weight(1f)
                                                        .bouncyClickable()) {}
                                          }
                                      Box(
                                          modifier =
                                              Modifier.fillMaxWidth()
                                                  .height(64.dp)
                                                  .shimmerBackground(CardDefaults.shape))
                                      val listState1 = rememberLazyListState()
                                      LazyRow(
                                          state = listState1,
                                          flingBehavior =
                                              rememberSnapFlingBehavior(
                                                  listState1, SnapPosition.Start),
                                          horizontalArrangement = Arrangement.spacedBy(12.dp),
                                          modifier =
                                              Modifier.horizontalFadingEdge(listState1, 32.dp)) {
                                            items(10) {
                                              OutlinedCard(
                                                  modifier = Modifier.height(64.dp).width(96.dp)) {}
                                            }
                                          }
                                      val scrollState1 = rememberScrollState()
                                      Row(
                                          horizontalArrangement = Arrangement.spacedBy(12.dp),
                                          modifier =
                                              Modifier.horizontalFadingEdge(scrollState1, 32.dp)
                                                  .horizontalScroll(scrollState1)) {
                                            for (i in 0..10) {
                                              OutlinedCard(
                                                  modifier = Modifier.height(64.dp).width(96.dp)) {}
                                            }
                                          }
                                      val listState2 = rememberLazyListState()
                                      LazyRow(
                                          state = listState2,
                                          flingBehavior =
                                              rememberSnapFlingBehavior(
                                                  listState2, SnapPosition.Start),
                                          horizontalArrangement = Arrangement.spacedBy(12.dp),
                                          modifier =
                                              Modifier.scrollWithCap(
                                                  listState2,
                                                  64.dp,
                                                  onLeft = { println("Left") },
                                                  onRight = { println("Right") })) {
                                            items(10) {
                                              OutlinedCard(
                                                  modifier = Modifier.height(64.dp).width(96.dp)) {}
                                            }
                                          }
                                      PhoneField(
                                          onContinue = { phone ->
                                            println(phone)
                                            true
                                          })
                                    }
                              }
                              item {
                                var isLoading by remember { mutableStateOf(false) }
                                ValidatedForm(
                                    validators =
                                        listOf(
                                            Validator(key = ValidatorKey.Login) {
                                              if (it.isBlank())
                                                  ValidationResult.Invalid("Can't be empty")
                                              else if (it.length < 3)
                                                  ValidationResult.Invalid("Too short")
                                              else if (it.length > 10)
                                                  ValidationResult.Invalid("Too long")
                                              else ValidationResult.Valid
                                            },
                                            Validator(key = ValidatorKey.Password) {
                                              if (it.isBlank())
                                                  ValidationResult.Invalid("Can't be empty")
                                              else if (it.length < 3)
                                                  ValidationResult.Invalid("Too short")
                                              else if (it.length > 10)
                                                  ValidationResult.Invalid("Too long")
                                              else ValidationResult.Valid
                                            }),
                                    onSubmit = {
                                      coroutineScope.launch {
                                        isLoading = true
                                        delay(1000)
                                        isLoading = false
                                      }
                                    },
                                    isLoading = isLoading)
                              }
                            }
                      }
                    }
              }
            }
      }
}
