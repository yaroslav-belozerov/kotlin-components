package org.yaabelozerov.kmp_components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.window.core.layout.WindowWidthSizeClass
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Nav(val route: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
  MAIN("main", Icons.Filled.Home, Icons.Outlined.Home)
}

@Composable
@Preview
fun App() {
  val navCtrl = rememberNavController()
  val currentRoute = navCtrl.currentBackStackEntryAsState().value?.destination?.route

  AppTheme(isSystemInDarkTheme()) {
    NavigationSuiteScaffold(navigationSuiteItems = {
      Nav.entries.forEach {
        val selected = it.route == currentRoute
        item(selected = selected, icon = { Icon(if (selected) it.selectedIcon else it.unselectedIcon, contentDescription = it.route) }, onClick = {
          if (!selected) navCtrl.navigate(it.route)
        })
      }
    }) {
      Scaffold { innerPadding ->
        NavHost(modifier = Modifier.padding(innerPadding), navController = navCtrl, startDestination = Nav.MAIN.route) {
          composable(Nav.MAIN.route) {
            Column(
              Modifier.fillMaxSize().padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally) {
              PhoneField(
                onContinue = { phone ->
                  println(phone)
                  true
                })
            }
          }
        }
      }
    }
  }
}
