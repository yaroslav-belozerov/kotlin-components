package org.yaabelozerov.kmp_components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Nav(val route: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
  MAIN("main", Icons.Filled.Home, Icons.Outlined.Home),
  FONTS("fonts", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List)
}

@Composable
@Preview
fun App() {
  val navCtrl = rememberNavController()
  val currentRoute = navCtrl.currentBackStackEntryAsState().value?.destination?.route

  AppThemeConfiguration(
      darkTheme = isSystemInDarkTheme(), lightColors = appLightScheme, darkColors = appDarkScheme) {
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
                    onClick = {
                      if (!selected)
                          navCtrl.navigate(it.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navCtrl.graph.startDestinationRoute ?: return@navigate) {
                              saveState = true
                            }
                          }
                    })
              }
            }) {
              NavHost(
                  modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars).fillMaxSize(),
                  navController = navCtrl,
                  enterTransition = { fadeIn() + expandIn() },
                  exitTransition = { fadeOut() + shrinkOut() },
                  startDestination = Nav.MAIN.route) {
                    composable(Nav.MAIN.route) {
                      LazyColumn(
                          modifier = Modifier.fillMaxSize(),
                          horizontalAlignment = Alignment.CenterHorizontally,
                          verticalArrangement = Arrangement.spacedBy(16.dp),
                          contentPadding = PaddingValues(16.dp)) {
                            item { ShowcaseTitle() }
                            item { ShowcaseTextLine() }
                            item { ShowcaseCards() }
                            item { ShowcaseButtons() }
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
                    composable(Nav.FONTS.route) {
                      val types =
                          mapOf(
                              MaterialTheme.typography.displayLarge to "Display Large",
                              MaterialTheme.typography.displayMedium to "Display Medium",
                              MaterialTheme.typography.displaySmall to "Display Small",
                              MaterialTheme.typography.headlineLarge to "Headline Large",
                              MaterialTheme.typography.headlineMedium to "Headline Medium",
                              MaterialTheme.typography.headlineSmall to "Headline Small",
                              MaterialTheme.typography.titleLarge to "Title Large",
                              MaterialTheme.typography.titleMedium to "Title Medium",
                              MaterialTheme.typography.titleSmall to "Title Small",
                              MaterialTheme.typography.bodyLarge to "Body Large",
                              MaterialTheme.typography.bodyMedium to "Body Medium",
                              MaterialTheme.typography.bodySmall to "Body Small",
                              MaterialTheme.typography.labelLarge to "Label Large",
                              MaterialTheme.typography.labelMedium to "Label Medium",
                              MaterialTheme.typography.labelSmall to "Label Small",
                          )
                      var tooltipIndex by remember { mutableStateOf(-1) }
                      LazyColumn(
                          verticalArrangement = Arrangement.spacedBy(8.dp),
                          modifier = Modifier.fillMaxSize(),
                          contentPadding = PaddingValues(16.dp)) {
                            itemsIndexed(types.keys.toImmutableList()) { index, it ->
                              ElevatedCard(
                                  onClick = {
                                    tooltipIndex = if (index != tooltipIndex) {
                                      index
                                    } else {
                                      -1
                                    }
                                  }, modifier = Modifier.animateContentSize()) {
                                    Text(
                                        if (tooltipIndex == index)
                                            it.fontFamily!!
                                                .toString()
                                                .splitToSequence("/")
                                                .last()
                                                .splitToSequence(",")
                                                .first()
                                        else types[it].toString(),
                                        style = it,
                                        modifier = Modifier.padding(16.dp))
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
private fun ShowcaseTextLine() {
  Column(
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier.padding(bottom = 16.dp)) {
        var txt1 by remember { mutableStateOf(TextFieldValue()) }
        TextLine(txt1, { txt1 = it }, modifier = Modifier.fillMaxWidth())
        var txt2 by remember { mutableStateOf(TextFieldValue()) }
        TextLine(
            txt2,
            { txt2 = it },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Person, null) },
            placeholderString = "Username")
        var txt3 by remember { mutableStateOf(TextFieldValue()) }
        val mask = "000 000 00 00"
        TextLine(
            txt3,
            {
              if (it.text.length <= mask.filter { it != ' ' }.length &&
                  it.text.all { it.isDigit() }) {
                txt3 = it
              }
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Text("+7") },
            placeholderString = "000 000 00 00",
            visualTransformation = PhoneVisualTransformation(mask, maskNumber = '0'))
        var txt4 by remember { mutableStateOf(TextFieldValue()) }
        TextLine(
            txt4,
            { txt4 = it },
            modifier = Modifier.fillMaxWidth(),
            isError = txt4.text.any { it.isDigit() },
            placeholderString = "No digits!")
        TextLine(
            TextFieldValue("Wrong Answer: 10"),
            {},
            modifier = Modifier.fillMaxWidth(),
            isError = true,
            enabled = false)
      }
}

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
private fun ShowcaseButtons() {
  LazyRow(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start)) {
        item {
          var wasClicked by remember { mutableStateOf(false) }
          CustomButton(
              onClick = { wasClicked = !wasClicked },
              text = if (wasClicked) "Clicked!" else "Click Me!",
              icon = if (wasClicked) Icons.Filled.Check else null)
        }
        item {
          var wasClicked by remember { mutableStateOf(false) }
          CustomOutlinedButton(
              onClick = { wasClicked = !wasClicked },
              text = if (wasClicked) "Clicked!" else "Click Me!",
              icon = if (wasClicked) Icons.Filled.Check else null)
        }
        item {
          var wasClicked by remember { mutableStateOf(false) }
          CustomTextButton(
              onClick = { wasClicked = !wasClicked },
              text = if (wasClicked) "Clicked!" else "Click Me!",
              icon = if (wasClicked) Icons.Filled.Check else null)
        }
      }
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
