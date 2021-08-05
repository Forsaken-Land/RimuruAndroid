package top.fanua.rimuruAndroid.ui.sustomStuff

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Storage
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
    object Home : Screen("home", "主页", Icons.Filled.Home)
    object Terminal : Screen("terminal", "终端", Icons.Filled.VideoLabel)
    object Settings : Screen("settings", "设置", Icons.Filled.Settings)
    object Chat : Screen("chat", "聊天", Icons.Rounded.ChatBubbleOutline)
    object Servers : Screen("servers", "服务器", Icons.Rounded.Storage)


    object Items {
        val list = listOf(
            Home, Servers, Chat, Terminal, Settings
        )
    }
}
