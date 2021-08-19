package top.fanua.rimuruAndroid.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.navigation.ComposeNavigation
import top.fanua.rimuruAndroid.ui.sustomStuff.CustomBottomNavigation
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.theme.Theme


/**
 *
 * @author Doctor_Yin
 * @since 2021/8/8:18:58
 */
@Composable
fun Home() {
    val navController = rememberNavController()
    val rimuruViewModel: RimuruViewModel = viewModel()
    Column {
        Surface(color = Theme.colors.background) {
            Scaffold(
                bottomBar = {
                    CustomBottomNavigation(currentScreenId = rimuruViewModel.currentScreen.id) {
                        rimuruViewModel.currentScreen = it
                    }
                }
            ) {
                when (rimuruViewModel.currentScreen) {
                    Screen.Home -> Box(Modifier.fillMaxSize()) { }
                    Screen.Settings -> SettingsPage(navController)
                    Screen.Chat -> ServerList()
                }
            }
        }
    }
    ChatPage(navController, rimuruViewModel)
    ComposeNavigation(navController, rimuruViewModel)
}

