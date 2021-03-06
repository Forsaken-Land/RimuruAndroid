package top.fanua.rimuruAndroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.settings.HeaderSetting
import top.fanua.rimuruAndroid.ui.settings.InfoSetting
import top.fanua.rimuruAndroid.ui.settings.Setting

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/19:0:26
 */
@Composable
fun ComposeNavigation(navController: NavHostController, viewModel: RimuruViewModel) {
    NavHost(
        navController = navController,
        startDestination = "empty",
    ) {
        composable("empty") {

        }
        composable(Setting.HeaderSetting.id) {
            HeaderSetting(viewModel)
        }
        composable(Setting.InfoSetting.id) {
            InfoSetting(viewModel)
        }
        composable("online") {
            OnlineNavigation(viewModel)
        }
        composable(Setting.DevChoose.id) {
            VersionNavigation(viewModel)
        }
    }
}
