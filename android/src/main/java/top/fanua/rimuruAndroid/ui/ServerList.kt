package top.fanua.rimuruAndroid.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding
import top.fanua.rimuruAndroid.data.*
import top.fanua.rimuruAndroid.models.ChatMap
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen.Items.list
import top.fanua.rimuruAndroid.ui.theme.Theme
import top.fanua.rimuruAndroid.ui.theme.Theme.Type.*
import top.fanua.rimuruAndroid.utils.toDateStr
import java.util.stream.Collectors.toList

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/8:19:55
 */
@Composable
fun ServerList() {
    val viewModel: RimuruViewModel = viewModel()
    viewModel.servers = viewModel.accountDao!!.getSaveServers(viewModel.loginEmail)
        .collectAsState(listOf()).value
    Box {
        LazyColumn {
            itemsIndexed(viewModel.servers) { index, item ->
                val chat = viewModel.accountDao!!.getSaveChats(viewModel.loginEmail, item.name)
                    .collectAsState(null).value
                viewModel.chatList[ChatMap(viewModel.loginEmail, item.name)] = chat
                if (index == 0) {
                    Spacer(Modifier.height(48.dp).fillMaxWidth())
                }
                ChatListItem(chat, item.toServer(), viewModel)
                if (index < viewModel.servers.size - 1) {
                    Divider(
                        startIndent = 0.dp,
                        color = Theme.colors.divider,
                        thickness = 0.8f.dp
                    )
                }
                if (index == viewModel.servers.size - 1) Spacer(Modifier.height(60.dp).fillMaxWidth())

            }
        }
        Column {
            TopBar("聊天")
            Divider(
                startIndent = 0.dp,
                color = Theme.colors.divider,
                thickness = 0.8f.dp
            )
        }


    }

}

private fun SaveServer.toServer(): Server {
    return Server(host, port, name, icon)
}

fun ServerWithChats.toChat(): Chat {
    val chat = mutableListOf<Msg>()
    chats.forEach {
        chat.add(Msg(Role(it.uuid, it.name, it.icon), it.text, it.time))
    }
    return Chat(Server(server.host, server.port, server.name, server.icon), chat)

}

enum class Status {
    OPEN, CLOSE
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ChatListItem(
    item: ServerWithChats?,
    server: Server,
    viewModel: RimuruViewModel
) {
    var blockSize = 50.dp
    var blockSizePx = with(LocalDensity.current) { blockSize.toPx() }

    val swipeableState = rememberSwipeableState(initialValue = Status.CLOSE)
    Row(
        Modifier.fillMaxWidth().height(60.dp)
            .swipeable(
                state = swipeableState,
                anchors = mapOf(
                    0f to Status.CLOSE,
                    blockSizePx to Status.OPEN
                ),
                thresholds = { from, _ ->
                    if (from == Status.OPEN) {
                        FractionalThreshold(0.3f)
                    } else {
                        FractionalThreshold(0.5f)
                    }
                },
                orientation = Orientation.Horizontal
            ).offset {
                IntOffset(swipeableState.offset.value.toInt(), 0)
            }.clickable {
                viewModel.startChat(item?.toChat()!!)
            }
    ) {
        Image(
            rememberImagePainter(data = server.icon),
            contentDescription = null,
            modifier = Modifier.size(50.dp).padding(10.dp).clip(RoundedCornerShape(viewModel.radian.dp))
        )
        Column(Modifier.align(Alignment.CenterVertically)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    server.name,
                    fontSize = 15.sp
                )
                if (item != null && item.toChat().msg.isNotEmpty()) Text(
                    item.toChat().msg.last().time.toDateStr("HH:mm"),
                    color = Theme.colors.timeText,
                    fontSize = 8.sp,
                    modifier = Modifier.width(60.dp)
                )


            }
            if (item != null && item.toChat().msg.isNotEmpty()) Row {
                Text(
                    "${item.toChat().msg.last().from.name}: ${item.toChat().msg.last().text.replace("\n", "    ")}",
                    color = Theme.colors.timeText,
                    maxLines = 1,
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
    val rimuruViewModel: RimuruViewModel = viewModel()
    Box(
        Modifier
            .background(Theme.colors.background.copy(alpha = 0.9f))
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
            } else {
                var addServer by remember { mutableStateOf(false) }
                if (addServer) {
                    AddServerPage {
                        addServer = if (it != null) {
                            rimuruViewModel.addServer(it)
                            false
                        } else {
                            false
                        }
                    }
                }
                Icon(
                    Icons.Rounded.Add,
                    null,
                    Modifier
                        .clickable(onClick = {
                            addServer = true
                        })
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
