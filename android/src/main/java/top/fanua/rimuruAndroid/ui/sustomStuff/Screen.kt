package top.fanua.rimuruAndroid.ui.sustomStuff

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLabel
import androidx.compose.ui.graphics.vector.ImageVector

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/3:17:07
 */
sealed class Screen(
    val id: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Terminal : Screen("terminal", "Terminal", Icons.Filled.VideoLabel)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
    object Chat : Screen("chat", "Chat", Icons.Filled.Chat)


    object Items {
        val list = listOf(
            Home, Terminal, Chat, Settings
        )
    }
}
