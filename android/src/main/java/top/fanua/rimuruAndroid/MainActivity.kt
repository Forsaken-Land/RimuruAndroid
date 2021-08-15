package top.fanua.rimuruAndroid

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.fanua.doctor.allLoginPlugin.enableAllLoginPlugin
import top.fanua.doctor.client.MinecraftClient
import top.fanua.doctor.client.running.AutoVersionForgePlugin
import top.fanua.doctor.client.running.PlayerPlugin
import top.fanua.doctor.client.running.TpsPlugin
import top.fanua.doctor.client.running.tabcomplete.TabCompletePlugin
import top.fanua.doctor.network.event.ConnectionEvent
import top.fanua.doctor.network.handler.onPacket
import top.fanua.doctor.protocol.definition.play.client.ChatPacket
import top.fanua.doctor.protocol.definition.play.client.DisconnectPacket
import top.fanua.doctor.protocol.definition.play.client.PlayerPositionAndLookPacket
import top.fanua.doctor.protocol.definition.play.client.STabCompletePacket
import top.fanua.doctor.protocol.entity.text.ChatSerializer
import top.fanua.rimuruAndroid.data.AppDatabase
import top.fanua.rimuruAndroid.data.Role
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.Home
import top.fanua.rimuruAndroid.ui.LoginPage
import top.fanua.rimuruAndroid.ui.get
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.theme.Theme


class MainActivity : AppCompatActivity() {
    private val viewModel: RimuruViewModel by viewModels()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                viewModel.path = "${applicationContext.dataDir.path}/files"
                Theme {
                    viewModel.accountDao = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "account-data"
                    ).build().accountDao()
                    Config()
                    if (!viewModel.loading) {
                        viewModel.start()
                        if (viewModel.loginEmail.isNotEmpty()) Home()
                        else LoginPage()
                    }

                }
            }
        }
    }

    @Composable
    private fun Config() {
        viewModel.loginEmail =
            viewModel.accountDao!!.getConfig("loginEmail").collectAsState(null).value?.value.orEmpty()
        viewModel.lastEmail = viewModel.accountDao!!.getConfig("lastEmail").collectAsState(null).value?.value.orEmpty()
        viewModel.accounts = viewModel.accountDao!!.getSaveAccount().collectAsState(mutableListOf()).value
        val loginUser = viewModel.accounts.get(viewModel.loginEmail)?.saveAccount
        if (loginUser != null) {
            viewModel.me = Role(loginUser.uuid.orEmpty(), loginUser.name.orEmpty(), loginUser.icon.orEmpty())
        }
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            val data = viewModel.accountDao!!.getSaveAccount().firstOrNull()
            val data1 = viewModel.accountDao!!.getSaveServers(viewModel.loginEmail).firstOrNull()
            while (true) {
                if (data != null && data1 != null) {
                    viewModel.loading = false
                    break
                }
            }
        }

        if (viewModel.loginEmail.isNotEmpty()) {
            viewModel.radian = viewModel.accounts.get(viewModel.loginEmail)?.saveAccount?.radian ?: 10
        } else if (viewModel.lastEmail.isNotEmpty()) {
            viewModel.radian = viewModel.accounts.get(viewModel.lastEmail)?.saveAccount?.radian ?: 10
        }
        if (viewModel.loginEmail == "") viewModel.currentScreen = Screen.Chat
    }

    @Composable
    private fun ClientServer() {
    }

    override fun onBackPressed() {
        if (viewModel.chatting) viewModel.endChat() else super.onBackPressed()
    }
}




