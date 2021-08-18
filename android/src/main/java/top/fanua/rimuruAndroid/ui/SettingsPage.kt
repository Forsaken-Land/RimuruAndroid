package top.fanua.rimuruAndroid.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import kotlinx.coroutines.*
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.settings.Setting
import top.fanua.rimuruAndroid.ui.theme.ImageHeader
import top.fanua.rimuruAndroid.ui.theme.Theme
import kotlin.math.roundToInt

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/6:4:01
 */
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun SettingsPage(navController: NavHostController) {
    val composableScope = rememberCoroutineScope()
    val rimuruViewModel: RimuruViewModel = viewModel()
    Box(Modifier.fillMaxSize().background(Theme.colors.setBackground)) {
        var time by remember { mutableStateOf(0) }
        LazyColumn(
            contentPadding = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.statusBars,
                additionalTop = 20.dp
            ), modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    Modifier.fillMaxWidth().height(80.dp).background(Theme.colors.background)
                        .padding(horizontal = 40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rimuruViewModel.loginAccount?.icon?.let {
                        ImageHeader(
                            it,
                            dp = 50.dp,
                            size = rimuruViewModel.radian.dp
                        )
                    }
                    Text(
                        rimuruViewModel.accounts.get(rimuruViewModel.loginEmail)?.saveAccount?.name.orEmpty(),
                        modifier = Modifier.padding(10.dp)
                    )
                    Button(modifier = Modifier.padding(horizontal = 20.dp), enabled = time == 0, onClick = {
                        rimuruViewModel.refreshAccount()
                        composableScope.launch(Dispatchers.IO) {
                            time = 60
                            while (true) {
                                if (time == 0) return@launch
                                time--
                                delay(1000L)
                            }
                        }
                    }) {
                        if (time > 0) Text("请等待 $time 秒")
                        else Text("刷新账号信息")
                    }

                }

            }
            items(Setting.Items.list) {
                Divider(
                    startIndent = 0.dp,
                    color = Theme.colors.divider,
                    thickness = 2.dp
                )
                Row(
                    Modifier.fillMaxWidth().height(80.dp).background(Theme.colors.background)
                        .padding(horizontal = 40.dp).clickable {
                            navController.navigate(it.id)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(it.title)
                }

            }
            item {
                Divider(
                    startIndent = 0.dp,
                    color = Theme.colors.divider,
                    thickness = 15.dp
                )
                Row(
                    Modifier.fillMaxWidth().height(60.dp).clickableWithout(true) {
                        rimuruViewModel.signOut()
                        composableScope.cancel()
                        time = 0
                    }.background(Theme.colors.background),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("退出登录", textAlign = TextAlign.Center, maxLines = 1)
                }

            }
        }
    }
}
