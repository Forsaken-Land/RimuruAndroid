package top.fanua.rimuruAndroid

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.ProvideWindowInsets
import top.fanua.rimuruAndroid.models.ChatViewModel
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.Home
import top.fanua.rimuruAndroid.ui.LoginPage
import top.fanua.rimuruAndroid.ui.theme.Theme


class MainActivity : AppCompatActivity() {
    val chatViewModel: ChatViewModel by viewModels()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        setContent {
            val rimuruViewModel: RimuruViewModel = viewModel()
            rimuruViewModel.path = "${applicationContext.dataDir.path}/files"
            Theme {
                ProvideWindowInsets {
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


//setContent {
//    Theme {
//        rememberSystemUiController().setStatusBarColor(
//            Color.Transparent, darkIcons = MaterialTheme.colors.isLight
//        )
//
//
//        val userViewModel = UserViewModel(
//            "/accounts",
//            remember { mutableStateOf(emptyList()) },
//            remember { mutableMapOf() },
//            remember { mutableStateOf("") }
//        )
//        val loading = remember { mutableStateOf(true) }
//        WindowCompat.setDecorFitsSystemWindows(window, userViewModel.email.value.isNotEmpty())
//        if (userViewModel.email.value.isEmpty()) {
//            userViewModel.viewModelScope.launch(Dispatchers.IO) {
//                userViewModel.refresh()
//                var ok = false
//                userViewModel.accountFiles.value.forEach { file ->
//                    val account = FileUtils.readFile<Account>(file)
//                    if (account.isLogin) {
//                        userViewModel.email.value = account.email
//                        ok = true
//                    }
//                }
//                if (!ok) {
//                    userViewModel.email.value = ""
//                }
//                loading.value = false
//
//            }
//        } else {
//            userViewModel.viewModelScope.launch(Dispatchers.IO) {
//                userViewModel.refresh()
//                userViewModel.accountFiles.value.forEach { file ->
//                    val account = FileUtils.readFile<Account>(file)
//                    if (account.isLogin && account.email != userViewModel.email.value) {
//                        FileUtils.writeFile(file, account.also {
//                            it.isLogin = false
//                        })
//                    }
//                }
//                userViewModel.refresh()
//
//            }
//
//        }
//
//        if (!loading.value) {
//            if (userViewModel.email.value.isNotEmpty()) {
//                Home()
//            } else {
//                LoginPage()
//            }
//
//
//        }
//    }
//
//}
