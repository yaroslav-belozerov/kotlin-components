package org.yaabelozerov.kmp_components

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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

  AppThemeConfiguration(
      darkTheme = isSystemInDarkTheme(),
      lightColors = appLightScheme,
      darkColors = appDarkScheme) {
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
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(16.dp)) {
                              item { ShowcaseTitle() }
                              item { ShowcaseCards() }
                              item { ShowcaseLoading() }
                              item { ShowcaseLazyFadeList() }
                              item { ShowcaseRowFadeList() }
                              item { ShowcaseSpringRow() }
                              item {
                                PhoneField(
                                    onContinue = { phone ->
                                      println(phone)
                                      true
                                    })
                                ShowcaseForm()
                              }
                            }
                      }
                    }
              }
            }
      }
}

@Composable
private fun ShowcaseTitle() =
    Text(
        "Hello world!",
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier.padding(bottom = 8.dp))

@Composable
private fun ShowcaseCards() =
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      var isEnabled by remember { mutableStateOf(false) }
      Card(
          colors =
              if (isEnabled)
                  CardDefaults.cardColors()
                      .copy(containerColor = MaterialTheme.colorScheme.primaryContainer)
              else CardDefaults.cardColors(),
          modifier =
              Modifier.height(128.dp)
                  .weight(1f)
                  .bouncyClickable(onClick = { isEnabled = !isEnabled })) {}
      Card(modifier = Modifier.height(128.dp).weight(1f).bouncyClickable()) {}
    }

@Composable
private fun ShowcaseLoading() =
    Box(modifier = Modifier.fillMaxWidth().height(64.dp).shimmerBackground(CardDefaults.shape))

@Composable
private fun ShowcaseLazyFadeList() {
  val lazyList = rememberLazyListState()
  return LazyRow(
      state = lazyList,
      flingBehavior = rememberSnapFlingBehavior(lazyList, SnapPosition.Start),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.horizontalFadingEdge(lazyList, 32.dp)) {
        items(10) { OutlinedCard(modifier = Modifier.height(64.dp).width(96.dp)) {} }
      }
}

@Composable
private fun ShowcaseRowFadeList() {
  val scroll = rememberScrollState()
  return Row(
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.horizontalFadingEdge(scroll, 32.dp).horizontalScroll(scroll)) {
        for (i in 0..10) {
          OutlinedCard(modifier = Modifier.height(64.dp).width(96.dp)) {}
        }
      }
}

@Composable
private fun ShowcaseSpringRow() {
  val lazyList = rememberLazyListState()
  return LazyRow(
      state = lazyList,
      flingBehavior = rememberSnapFlingBehavior(lazyList, SnapPosition.Start),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      modifier =
          Modifier.scrollWithCap(
              lazyList, 64.dp, onLeft = { println("Left") }, onRight = { println("Right") })) {
        items(10) { OutlinedCard(modifier = Modifier.height(64.dp).width(96.dp)) {} }
      }
}

@Composable
private fun ShowcaseForm() {
  var isLoading by remember { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()
  return ValidatedForm(
      validators =
          listOf(
              Validator(key = ValidatorKey.Login) {
                if (it.isBlank()) ValidationResult.Invalid("Can't be empty")
                else if (it.length < 3) ValidationResult.Invalid("Too short")
                else if (it.length > 10) ValidationResult.Invalid("Too long")
                else ValidationResult.Valid
              },
              Validator(key = ValidatorKey.Password) {
                if (it.isBlank()) ValidationResult.Invalid("Can't be empty")
                else if (it.length < 3) ValidationResult.Invalid("Too short")
                else if (it.length > 10) ValidationResult.Invalid("Too long")
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
