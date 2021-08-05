package top.fanua.rimuruAndroid

import App
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.flow.compose
import top.fanua.rimuruAndroid.ui.AppDatabase
import top.fanua.rimuruAndroid.ui.LoginPage
import top.fanua.rimuruAndroid.ui.User
import top.fanua.rimuruAndroid.ui.sustomStuff.CustomBottomNavigation
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.terminal
import top.fanua.rimuruAndroid.ui.theme.CustomBottomNavigationTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentScreen = mutableStateOf<Screen>(Screen.Home)
            val terminalMsg = remember { mutableStateOf("") }
            val isLogin = remember { mutableStateOf(false) }


            val db = Room
                .databaseBuilder(applicationContext, AppDatabase::class.java, "db_users")
                .build()
            val userDao = db.getUserDao()
            val users = userDao.queryAll()


            users.observe(this) {
                for (user in it) {
                    if (user.isLogin) {
                        isLogin.value = true
                        break
                    }
                    isLogin.value = false
                }
            }



            if (isLogin.value) {
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
                                Screen.Settings -> println(currentScreen.value)
                                Screen.Terminal -> terminal(terminalMsg)
                                Screen.Chat -> println(currentScreen.value)
                                Screen.Servers -> println(currentScreen.value)
                            }

                        }
                    }
                }
            } else {
                LoginPage(userDao)
            }


        }
    }
}
