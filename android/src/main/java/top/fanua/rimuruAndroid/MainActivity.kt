package top.fanua.rimuruAndroid

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import top.fanua.rimuruAndroid.data.*
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.Home
import top.fanua.rimuruAndroid.ui.LoginPage
import top.fanua.rimuruAndroid.ui.get
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.theme.Theme
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {
    private val viewModel: RimuruViewModel by viewModels()
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
                    ).addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7
                    )
                        .build().accountDao()
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

        viewModel.lastEmail =
            viewModel.accountDao!!.getConfig("lastEmail").collectAsState(null).value?.value.orEmpty()

        viewModel.accounts =
            viewModel.accountDao!!.getSaveAccount().collectAsState(mutableListOf()).value

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
            viewModel.loginAccount = viewModel.accounts.get(viewModel.loginEmail)?.saveAccount
        } else if (viewModel.lastEmail.isNotEmpty()) {
            viewModel.radian = viewModel.accounts.get(viewModel.lastEmail)?.saveAccount?.radian ?: 10
        }

        if (viewModel.loginEmail == "") viewModel.currentScreen = Screen.Chat

    }

    override fun onBackPressed() {
        if (viewModel.chatInfo) {
            viewModel.chatInfo = false
            super.onBackPressed()
        } else if (viewModel.chatting) viewModel.endChat() else super.onBackPressed()
    }
}




