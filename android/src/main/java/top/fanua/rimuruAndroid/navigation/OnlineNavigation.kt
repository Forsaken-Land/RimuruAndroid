package top.fanua.rimuruAndroid.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import top.fanua.rimuruAndroid.data.Online
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.clickableWithout
import top.fanua.rimuruAndroid.ui.theme.ImageHeader

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/19:20:55
 */
@Composable
fun OnlineNavigation(viewModel: RimuruViewModel, navController: NavHostController) {
    if (viewModel.currentChat != null) {
        val online = viewModel.online[viewModel.currentChat!!.server.name]!!
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
            Spacer(Modifier.height(60.dp).fillMaxWidth().clickableWithout(true) {
                navController.navigate("empty")
            })
            LazyRow(
                Modifier.padding(top = 60.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                items(online) { item: Online ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(120.dp).clickable {}
                    ) {
                        ImageHeader(item.icon, size = viewModel.radian.dp)
                        Text(item.name)
                    }

                }
            }
            Spacer(Modifier.padding(top=180.dp).fillMaxSize().clickableWithout(true) {
                navController.navigate("empty")
            })

        }
    }
}
