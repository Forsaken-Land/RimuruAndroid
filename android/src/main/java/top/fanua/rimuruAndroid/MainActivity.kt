package top.fanua.rimuruAndroid

import App
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.fanua.rimuruAndroid.ui.*
import top.fanua.rimuruAndroid.ui.sustomStuff.CustomBottomNavigation
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.theme.CustomBottomNavigationTheme
import java.io.File
import java.io.FileInputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val composableScope = rememberCoroutineScope()
            val currentScreen = mutableStateOf<Screen>(Screen.Home)
            val terminalMsg = remember { mutableStateOf("") }
            val loading = remember { mutableStateOf(true) }
            val accountPath = "${applicationContext.dataDir.path}/files/accounts"
            val accountFiles = remember { mutableStateOf(emptyList<File>()) }
            val email = remember { mutableStateOf("") }

            if (email.value.isEmpty()) {
                composableScope.launch(Dispatchers.IO) {
                    accountFiles.value = File(accountPath).walk()
                        .maxDepth(1)
                        .filter { it.isFile }
                        .filter { it.extension == "@doctor@" }
                        .toList()
                    var ok = false
                    accountFiles.value.forEach {
                        val pros = Properties()
                        pros.load(FileInputStream(it))
                        if (pros.isEmpty) {
                            it.delete()
                        } else {
                            val loginStats = pros.getProperty("isLogin") ?: false.toString()
                            if (loginStats.toBoolean()) {
                                email.value = pros.getProperty("email")
                                ok = true
                            }
                        }
                    }
                    if (!ok) {
                        email.value = ""
                    }
                    loading.value = false
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
                                        accountPath,
                                        accountFiles,
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
                    LoginPage(accountPath, email)
                }


            }
        }
    }
}
