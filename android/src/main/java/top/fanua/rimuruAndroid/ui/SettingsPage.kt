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
                        modifier = Modifier.size(60.dp),
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

            Button(onClick = {
                rimuruViewModel.signOut()
            }) {
                Text("登出")
            }
            Button(onClick = {
                rimuruViewModel.refreshAccount()
            }) {
                Text("刷新账号信息")
            }
        }


    }
}
