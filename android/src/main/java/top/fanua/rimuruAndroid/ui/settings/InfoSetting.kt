package top.fanua.rimuruAndroid.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.theme.Theme

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/19:1:28
 */
@Composable
fun InfoSetting(viewModel: RimuruViewModel) {
    val url = "https://raw.githubusercontent.com/Forsaken-Land/RimuruAndroid/dev/README.md"
    val url2 = "https://git.blackyin.xyz:8443/liang/RimuruAndroid/-/raw/dev/README.md"
    var markdown by mutableStateOf("")
    viewModel.viewModelScope.launch(Dispatchers.IO) {
        val inputStream = try {
            OkHttpClient().newCall(Request.Builder().get().url(url2).build()).execute().body!!.byteStream()
        } catch (e: Exception) {
            OkHttpClient().newCall(Request.Builder().get().url(url).build()).execute().body!!.byteStream()
        }
        val byteArray = inputStream.readBytes()
        markdown = String(byteArray)
    }
    Surface(Modifier.fillMaxSize(), color = Theme.colors.background) {
        MarkdownText(markdown, modifier = Modifier.padding(horizontal = 20.dp))
    }
}

