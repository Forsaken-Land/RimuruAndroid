package top.fanua.rimuruAndroid.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.*
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.theme.ImageHeader
import kotlin.math.roundToInt

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/6:4:01
 */
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun SettingsPage() {
    val composableScope = rememberCoroutineScope()
    Box(Modifier.fillMaxSize()) {
        val rimuruViewModel: RimuruViewModel = viewModel()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                rimuruViewModel.accounts.get(rimuruViewModel.loginEmail)?.saveAccount?.icon?.let {
                    ImageHeader(
                        it,
                        dp = 100.dp,
                        size = rimuruViewModel.radian.dp
                    )
                }
                Text(rimuruViewModel.accounts.get(rimuruViewModel.loginEmail)!!.saveAccount.name.orEmpty())
            }
            var sliderPosition by remember { mutableStateOf(rimuruViewModel.radian.toFloat()) }
            rimuruViewModel.radian = sliderPosition.roundToInt()
            Slider(
                value = sliderPosition,
                valueRange = 0f..30f,
                onValueChange = {
                    sliderPosition = it
                    rimuruViewModel.changeRadian(sliderPosition.roundToInt())
                },
                modifier = Modifier.fillMaxWidth(0.6f)
            )
            var time by remember { mutableStateOf(0) }

            Button(onClick = {
                rimuruViewModel.signOut()
                time = 0
                composableScope.cancel()
            }) {
                Text("登出")
            }
            Button(enabled = time == 0, onClick = {
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
}
