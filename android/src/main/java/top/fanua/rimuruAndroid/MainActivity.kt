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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import top.fanua.rimuruAndroid.data.Account
import top.fanua.rimuruAndroid.data.AppDatabase
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
                    Config(viewModel)
                    if (!viewModel.loading) {
                        if (viewModel.loginEmail.isNotEmpty()) Home()
                        else LoginPage()
                    }

                }
            }
        }
    }

    @Composable
    private fun Config(viewModel: RimuruViewModel) {
        viewModel.loginEmail =
            viewModel.accountDao!!.getConfig("loginEmail").collectAsState(null).value?.value.orEmpty()
        viewModel.lastEmail = viewModel.accountDao!!.getConfig("lastEmail").collectAsState(null).value?.value.orEmpty()
        viewModel.accounts = viewModel.accountDao!!.getSaveAccount().collectAsState(mutableListOf()).value
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

    override fun onBackPressed() {
        if (viewModel.chatting) viewModel.endChat() else super.onBackPressed()
    }
}




