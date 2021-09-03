package top.fanua.rimuruAndroid.ui.settings

import androidx.compose.runtime.Composable

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/18:23:11
 */

sealed class Setting(
    val id: String,
    val title: String
) {
    object HeaderSetting : Setting("header", "头像设置")
    object InfoSetting : Setting("info", "关于")
    object DevChoose:Setting("dev","版本选项")

    object Items {
        val list = listOf(HeaderSetting,InfoSetting,DevChoose)
    }
}

