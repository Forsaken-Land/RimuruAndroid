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
            val currentScreen = mutableStateOf<Screen>(Screen.Home)
            val terminalMsg = remember { mutableStateOf("") }
            val accountFiles = remember {
                mutableStateOf(File("${applicationContext.dataDir.path}/files/accounts").walk()
                    .maxDepth(1)
                    .filter { it.isFile }
                    .filter { it.extension == "@doctor@" }
                    .toList())
            }
            val email = remember { mutableStateOf("") }

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
                                Screen.Settings -> SettingsPage(accountFiles, email)
                                Screen.Terminal -> terminal(terminalMsg)
                                Screen.Chat -> println(currentScreen.value)
                                Screen.Servers -> println(currentScreen.value)
                            }

                        }
                    }
                }
            } else {
                LoginPage(applicationContext, email)
            }


        }
    }
}
