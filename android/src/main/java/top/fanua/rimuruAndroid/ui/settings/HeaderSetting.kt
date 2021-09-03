package top.fanua.rimuruAndroid.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.get
import top.fanua.rimuruAndroid.ui.theme.ImageHeader
import top.fanua.rimuruAndroid.ui.theme.Theme
import kotlin.math.roundToInt

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/18:23:11
 */
@Composable
fun HeaderSetting(viewModel: RimuruViewModel) {
    var sliderPosition by remember { mutableStateOf(viewModel.radian.toFloat()) }
    viewModel.radian = sliderPosition.roundToInt()
    Surface(modifier = Modifier.fillMaxSize().background(viewModel.theme.colors.background)) {
        Row(
            Modifier.fillMaxWidth().height(80.dp).background(viewModel.theme.colors.background)
                .padding(horizontal = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageHeader(
                viewModel.loginAccount?.icon.orEmpty(),
                dp = 50.dp,
                size = viewModel.radian.dp
            )
            Slider(
                value = sliderPosition,
                valueRange = 0f..30f,
                onValueChange = {
                    sliderPosition = it
                    viewModel.changeRadian(sliderPosition.roundToInt())
                },
                modifier = Modifier.fillMaxWidth(0.6f)
            )
        }

    }
}
