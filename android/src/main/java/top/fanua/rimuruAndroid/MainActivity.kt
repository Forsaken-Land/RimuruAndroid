package top.fanua.rimuruAndroid

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.ProvideWindowInsets
import top.fanua.rimuruAndroid.models.ChatViewModel
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.Home
import top.fanua.rimuruAndroid.ui.LoginPage
import top.fanua.rimuruAndroid.ui.theme.Theme


class MainActivity : AppCompatActivity() {
    private val chatViewModel: ChatViewModel by viewModels()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                val rimuruViewModel: RimuruViewModel = viewModel()
                rimuruViewModel.path = "${applicationContext.dataDir.path}/files"
                Theme {
                    rimuruViewModel.refresh()
                    if (rimuruViewModel.loginEmail.isNotEmpty()) {
                        Home()
                    } else LoginPage()
                }
            }

        }


    }

    override fun onBackPressed() {
        if (chatViewModel.chatting) chatViewModel.endChat() else super.onBackPressed()
    }
}

