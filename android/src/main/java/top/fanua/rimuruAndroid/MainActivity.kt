package top.fanua.rimuruAndroid

import App
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.fanua.rimuruAndroid.data.User
import top.fanua.rimuruAndroid.ui.*
import top.fanua.rimuruAndroid.ui.sustomStuff.CustomBottomNavigation
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.theme.CustomBottomNavigationTheme
import top.fanua.rimuruAndroid.utils.FileUtils
import top.fanua.rimuruAndroid.utils.UserViewModel
import java.io.File
import java.io.FileInputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val userViewModel = UserViewModel(
                "${applicationContext.dataDir.path}/files/accounts",
                remember { mutableStateOf(emptyList()) },
                remember { mutableMapOf() }
            )
            val currentScreen = mutableStateOf<Screen>(Screen.Home)
            val terminalMsg = remember { mutableStateOf("") }
            val loading = remember { mutableStateOf(true) }
            val email = remember { mutableStateOf("") }

            if (email.value.isEmpty()) {
                userViewModel.viewModelScope.launch(Dispatchers.IO) {
                    userViewModel.refresh()
                    var ok = false
                    userViewModel.accountFiles.value.forEach { file ->
                        val user = FileUtils.readFile<User>(file)
                        if (user.isLogin) {
                            email.value = user.email
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
                        val user = FileUtils.readFile<User>(file)
                        if (user.isLogin && user.email != email.value) {
                            FileUtils.writeFile(file, user.also {
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
