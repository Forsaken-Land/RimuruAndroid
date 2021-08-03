package top.fanua.rimuruAndroid

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import top.fanua.rimuruAndroid.ui.sustomStuff.CustomBottomNavigation
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.theme.CustomBottomNavigationTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentScreen = mutableStateOf<Screen>(Screen.Home)
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
                            Screen.Terminal -> println(currentScreen.value)
                            Screen.Chat -> println(currentScreen.value)
                        }

                    }
                }
            }
        }
    }
}
