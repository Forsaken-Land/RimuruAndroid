package top.fanua.rimuruAndroid.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding
import top.fanua.rimuruAndroid.data.Chat
import top.fanua.rimuruAndroid.models.ChatViewModel
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.theme.Theme
import top.fanua.rimuruAndroid.ui.theme.Theme.Type.*


/**
 *
 * @author Doctor_Yin
 * @since 2021/8/8:19:55
 */
@Composable
fun ServerList() {
    val chatViewModel: ChatViewModel = viewModel()
    val rimuruViewModel: RimuruViewModel = viewModel()
    Column() {
        TopBar("聊天")
        LazyColumn {
            itemsIndexed(chatViewModel.chatList.toList()) { index, item ->
                ChatListItem(item, rimuruViewModel)
                if (index < chatViewModel.chatList.size - 1) {
                    Divider(
                        startIndent = 0.dp,
                        color = Theme.colors.divider,
                        thickness = 0.8f.dp
                    )
                }
                if (index == chatViewModel.chatList.size - 1) Spacer(Modifier.height(60.dp).fillMaxWidth())

            }
        }
    }

}

@Composable
private fun ChatListItem(
    item: Chat,
    rimuruViewModel: RimuruViewModel
) {
    val chatViewModel: ChatViewModel = viewModel()
    Row(
        Modifier.fillMaxWidth().height(60.dp)
            .clickable(indication = LocalIndication.current, interactionSource = MutableInteractionSource()) {
                chatViewModel.startChat(item)
            }
    ) {
        Image(
            rememberImagePainter(data = item.server.icon),
            contentDescription = null,
            modifier = Modifier.padding(10.dp).clip(RoundedCornerShape(rimuruViewModel.radian.dp))
        )
        Column(Modifier.align(Alignment.CenterVertically)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    item.server.name,
                    fontSize = 15.sp
                )

                Text(
                    item.msg.last().time.toString(),
                    color = Theme.colors.timeText,
                    fontSize = 8.sp,
                    modifier = Modifier.width(60.dp)
                )
            }
            Row {
                Text(
                    "${item.msg.last().from.name}: ${item.msg.last().text}",
                    color = Theme.colors.timeText,
                    fontSize = 10.sp
                )
                //TODO
                //气泡
            }
        }
    }
}

@Composable
fun TopBar(title: String, onBack: (() -> Unit)? = null) {
    Box(
        Modifier
            .background(Theme.colors.background.copy(alpha = 0.8f))
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            Modifier
                .height(48.dp)
        ) {
            if (onBack != null) {
                Icon(
                    Icons.Rounded.ArrowBackIos,
                    null,
                    Modifier
                        .clickable(onClick = onBack)
                        .align(Alignment.CenterVertically)
                        .size(36.dp)
                        .padding(8.dp),
                    tint = Theme.colors.icon
                )
            }
            Spacer(Modifier.weight(1f))
            val viewModel: RimuruViewModel = viewModel()

            Icon(
                Icons.Outlined.ColorLens,
                "切换主题",
                Modifier
                    .clickable(interactionSource = MutableInteractionSource(), indication = null, onClick = {
                        viewModel.theme = when (viewModel.theme) {
                            Light -> Light
                        }
                    })
                    .align(Alignment.CenterVertically)
                    .size(36.dp)
                    .padding(8.dp),
                tint = Theme.colors.icon
            )
        }
        Text(title, Modifier.align(Alignment.Center), color = Theme.colors.timeText)
    }
}
