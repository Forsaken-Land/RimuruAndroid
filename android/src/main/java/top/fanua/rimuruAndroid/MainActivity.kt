package top.fanua.rimuruAndroid

import App
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewModelScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.fanua.rimuruAndroid.data.Account
import top.fanua.rimuruAndroid.ui.*
import top.fanua.rimuruAndroid.ui.sustomStuff.CustomBottomNavigation
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.theme.CustomBottomNavigationTheme
import top.fanua.rimuruAndroid.utils.FileUtils
import top.fanua.rimuruAndroid.models.UserViewModel

class MainActivity : AppCompatActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        setContent {

            rememberSystemUiController().setStatusBarColor(
                Color.Transparent, darkIcons = MaterialTheme.colors.isLight
            )
            val userViewModel = UserViewModel(
                "${applicationContext.dataDir.path}/files/accounts",
                remember { mutableStateOf(emptyList()) },
                remember { mutableMapOf() }
            )
            val currentScreen = mutableStateOf<Screen>(Screen.Home)
            val terminalMsg = remember { mutableStateOf("") }
            val loading = remember { mutableStateOf(true) }
            val email = remember { mutableStateOf("") }
            WindowCompat.setDecorFitsSystemWindows(window, email.value.isNotEmpty())
            if (email.value.isEmpty()) {
                userViewModel.viewModelScope.launch(Dispatchers.IO) {
                    userViewModel.refresh()
                    var ok = false
                    userViewModel.accountFiles.value.forEach { file ->
                        val account = FileUtils.readFile<Account>(file)
                        if (account.isLogin) {
                            email.value = account.email
                            ok = true
                        }
                    }
                    if (!ok) {
                        email.value = ""
                    }
                    loading.value = false

                }
            } else {
                userViewModel.viewModelScope.launch(Dispatchers.IO) {
                    userViewModel.refresh()
                    userViewModel.accountFiles.value.forEach { file ->
                        val account = FileUtils.readFile<Account>(file)
                        if (account.isLogin && account.email != email.value) {
                            FileUtils.writeFile(file, account.also {
                                it.isLogin = false
                            })
                        }
                    }
                    userViewModel.refresh()
                }

            }

            if (!loading.value) {
                if (email.value.isNotEmpty()) {
                    CustomBottomNavigationTheme {
                        Surface(color = MaterialTheme.colors.background) {
                            Scaffold(
                                bottomBar = {
                                    CustomBottomNavigation(currentScreenId = currentScreen.value.id) {
                                        currentScreen.value = it
                                    }

                                }
                            ) {
                                when (currentScreen.value) {
                                    Screen.Home -> App()
                                    Screen.Settings -> SettingsPage(
                                        userViewModel,
                                        email
                                    )
                                    Screen.Terminal -> terminal(terminalMsg)
                                    Screen.Chat -> println(currentScreen.value)
                                    Screen.Servers -> println(currentScreen.value)
                                }

                            }
                        }
                    }
                } else {
                    LoginPage(userViewModel, email)
                }


            }
        }
    }
}
