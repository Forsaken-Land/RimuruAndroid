package top.fanua.rimuruAndroid.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import top.fanua.rimuruAndroid.data.AllVersion
import top.fanua.rimuruAndroid.data.Config
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.models.UserViewModel

/**
 *
 * @author Doctor_Yin
 * @since 2021/9/3:17:39
 */
@Composable
fun VersionNavigation(rimuruViewModel: RimuruViewModel) {
    val userViewModel: UserViewModel = viewModel()
    val url = "https://gitee.com/Doctor_Yin/mc-bot/raw/master/all.json"
    val dev = rimuruViewModel.accountDao!!.getConfig("dev").collectAsState(null).value
    if (userViewModel.load) {
        userViewModel.viewModelScope.launch(Dispatchers.IO) {
            try {
                val json = String(
                    OkHttpClient().newCall(Request.Builder().get().url(url).build()).execute().body!!.byteStream()
                        .readBytes()
                )
                val data = Json {
                    ignoreUnknownKeys = true
                }.decodeFromString<AllVersion>(json)
                userViewModel.devVersion = data.dev
                userViewModel.releaseVersion = data.release
            } catch (e: Exception) {
                userViewModel.error = true
            }
        }
        userViewModel.load = false
    }
    Surface(Modifier.fillMaxSize(), color = rimuruViewModel.theme.colors.background) {
        if (userViewModel.devVersion.isNotEmpty() || userViewModel.releaseVersion.isNotEmpty()) {
            ChangeVersion(userViewModel, dev, rimuruViewModel)
        } else {
            Text(if (userViewModel.error) "版本获取失败" else "正在获取版本", color = Color.Red, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ChangeVersion(
    userViewModel: UserViewModel,
    dev: Config?,
    rimuruViewModel: RimuruViewModel
) {
    Column(
        Modifier.fillMaxWidth(0.8f).fillMaxHeight().padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.fillMaxWidth(0.8f).height(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MarkdownText("# 自更新版本切换")
        }
        Text("当前开发版: ${userViewModel.devVersion}")
        Text("当前正式版: ${userViewModel.releaseVersion}")
        Spacer(Modifier.fillMaxHeight(0.5f))
        Text(if (!(dev != null && dev.value.isNotEmpty())) "是否切换到开发版" else "已经是开发版")
        if (dev != null && dev.value.isNotEmpty()) Text("当开发版等于正式版时可以切换为正式版")
        var enable by mutableStateOf(false)
        if (dev == null) {
            enable = true
        } else {
            if (dev.value.isNotEmpty()) enable = true
            else {
                if (userViewModel.releaseVersion == userViewModel.devVersion) enable = true
            }
        }
        Button(onClick = {
            rimuruViewModel.viewModelScope.launch(Dispatchers.IO) {
                if (dev != null) {
                    if (dev.value.isEmpty()) {
                        rimuruViewModel.accountDao!!.updateConfig(dev.copy(value = "1"))
                    } else {
                        if (userViewModel.devVersion == userViewModel.releaseVersion) {
                            rimuruViewModel.accountDao!!.updateConfig(dev.copy(value = ""))
                        }
                    }
                } else {
                    rimuruViewModel.accountDao!!.insertConfig(Config(key = "dev", value = "1"))
                }
            }
        }, enabled = enable) {
            Text("切换")
        }
    }
}
