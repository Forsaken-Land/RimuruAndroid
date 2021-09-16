package top.fanua.rimuruAndroid.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.*
import top.fanua.rimuruAndroid.data.Chat
import top.fanua.rimuruAndroid.data.Msg
import top.fanua.rimuruAndroid.data.Role
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.theme.ImageHeader
import top.fanua.rimuruAndroid.ui.theme.Theme
import top.fanua.rimuruAndroid.ui.theme.inputColor1
import top.fanua.rimuruAndroid.utils.isToday
import top.fanua.rimuruAndroid.utils.isYesterday
import top.fanua.rimuruAndroid.utils.toDateStr
import kotlin.math.roundToInt

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/10:0:09
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatPage(navController: NavHostController, viewModel: RimuruViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current

    if (viewModel.currentChat != null) {
        var chat by remember { mutableStateOf<Chat?>(null) }
        chat = viewModel.accountDao!!.getSaveChats(viewModel.loginEmail, viewModel.currentChat!!.server.name)
            .collectAsState(null).value?.toChat()
        if (chat != null) {
            chat!!.msg.sortBy { msg -> msg.time }
            val percentOffset = animateFloatAsState(if (viewModel.chatting) 0f else 1f)
            val scrollState = rememberLazyListState()
            Surface(
                Modifier.percentOffsetX(percentOffset.value)
                    .background(Theme.colors.background)
                    .navigationBarsPadding(bottom = false)
            ) {
                Box(Modifier.fillMaxSize()) {
                    Column(Modifier.fillMaxSize()) {
                        Messages(
                            chat!!,
                            scrollState,
                            Modifier.weight(1f).background(Theme.colors.chatBackground),
                            viewModel.me
                        )
                        UserInput(chat!!.server.isLogin) {
                            viewModel.sendMessage(it)
                        }
                    }
                    Column {
                        Divider(
                            startIndent = 0.dp,
                            color = Theme.colors.divider,
                            thickness = 0.8f.dp
                        )
                        TopBar(chat!!.server.name, chat!!.server.isLogin, chat!!.server.online, navController) {
                            viewModel.endChat()
                            keyboardController?.hide()
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.percentOffsetX(percent: Float): Modifier = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.width, placeable.height) {
        placeable.placeRelative((percent * placeable.width).roundToInt(), 0)
    }
}

val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

@Composable
fun UserInput(isLogin: Boolean, send: (String) -> Unit) {
    var msg by remember { mutableStateOf("") }
    var textFieldFocusState by remember { mutableStateOf(false) }
    val sendMessageEnabled = msg.isNotEmpty()
    val disabledContentColor =
        Theme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
    val buttonColors = ButtonDefaults.buttonColors(
        disabledBackgroundColor = Theme.colors.background,
        disabledContentColor = disabledContentColor
    )

    val border = if (!sendMessageEnabled) {
        BorderStroke(
            width = 1.dp,
            color = Theme.colors.onSurface.copy(alpha = 0.12f)
        )
    } else {
        null
    }
    Column(Modifier.navigationBarsWithImePadding()) {
        Divider(
            startIndent = 0.dp,
            color = Theme.colors.divider,
            thickness = 0.8f.dp
        )
        Surface {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .heightIn(40.dp, 150.dp)
                    .semantics {
                        keyboardShownProperty = textFieldFocusState
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = msg,
                    onValueChange = {
                        msg = if (it.length >= 256) {
                            msg.substring(0, 255)
                        } else it
                    },
                    modifier = Modifier.fillMaxWidth(0.85f).heightIn(40.dp, 150.dp)
                        .background(inputColor1, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                        .onFocusChanged { state ->
                            textFieldFocusState = state.isFocused
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.None
                    ),
                    maxLines = 6,
                    cursorBrush = SolidColor(LocalContentColor.current),
                    textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp).offset(x = 5.dp).padding(5.dp),
                    enabled = sendMessageEnabled && isLogin,
                    onClick = {
                        send(msg)
                        msg = ""
                    },
                    colors = buttonColors,
                    border = border,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        "发送",
                    )
                }
            }
        }
    }

}


@Composable
fun Messages(
    chat: Chat,
    scrollState: LazyListState,
    modifier: Modifier,
    role: Role
) {
    Box(modifier) {
        var lastTime by remember { mutableStateOf(0L) }
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            contentPadding = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.statusBars,
                additionalTop = 90.dp
            ), modifier = Modifier
                .fillMaxSize()
        ) {
            itemsIndexed(chat.msg.reversed()) { index, msg ->
                MessageItem(msg, role)
                lastTime = if (chat.msg.size - 1 == index) msg.time
                else chat.msg[index + 1].time
                if (index != chat.msg.size - 1 && (msg.time - (20 * 1000L)) > lastTime) {
                    Text(
                        when {
                            msg.time.isToday() -> msg.time.toDateStr("HH:mm")
                            msg.time.isYesterday() -> msg.time.toDateStr("昨天 HH:mm")
                            else -> msg.time.toDateStr("MM-dd HH:mm")
                        },
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (index == chat.msg.size - 1) {
                    lastTime = msg.time
                    Text(
                        when {
                            msg.time.isToday() -> msg.time.toDateStr("HH:mm")
                            msg.time.isYesterday() -> msg.time.toDateStr("昨天 HH:mm")
                            else -> msg.time.toDateStr("MM-dd HH:mm")
                        },
                        fontSize = 8.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        }
    }

}

@Composable
fun MessageItem(
    msg: Msg,
    me: Role
) {
    val rimuruViewModel: RimuruViewModel = viewModel()
    if (msg.from.uuid == me.uuid) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            val bubbleColor = Theme.colors.bubbleMe
            Column {
                Text(
                    msg.from.name,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(80.dp).padding(2.dp).align(Alignment.End),
                    maxLines = 1
                )
                Text(
                    msg.text,
                    Modifier
                        .drawBehind {
                            val bubble = Path().apply {
                                val rect = RoundRect(
                                    10.dp.toPx(),
                                    0f,
                                    size.width - 10.dp.toPx(),
                                    size.height,
                                    4.dp.toPx(),
                                    4.dp.toPx()
                                )
                                addRoundRect(rect)
                                moveTo(size.width - 10.dp.toPx(), 8.dp.toPx())
                                lineTo(size.width - 5.dp.toPx(), 8.dp.toPx())
                                lineTo(size.width - 10.dp.toPx(), 20.dp.toPx())
                                close()
                            }
                            drawPath(bubble, bubbleColor)
                        }
                        .padding(20.dp, 10.dp).widthIn(max = 280.dp),
                    color = Theme.colors.textPrimaryMe
                )
            }

            ImageHeader(msg.from.icon, dp = 40.dp, size = rimuruViewModel.radian.dp)

        }
    } else {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            ImageHeader(msg.from.icon, dp = 40.dp, size = rimuruViewModel.radian.dp)
            val bubbleColor = Theme.colors.bubbleOthers
            Column {
                Text(
                    msg.from.name,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(80.dp).padding(2.dp).align(Alignment.Start),
                    maxLines = 1
                )
                Text(
                    msg.text,
                    Modifier
                        .drawBehind {
                            val bubble = Path().apply {
                                val rect = RoundRect(
                                    10.dp.toPx(),
                                    0f,
                                    size.width - 10.dp.toPx(),
                                    size.height,
                                    4.dp.toPx(),
                                    4.dp.toPx()
                                )
                                addRoundRect(rect)
                                moveTo(10.dp.toPx(), 8.dp.toPx())
                                lineTo(5.dp.toPx(), 8.dp.toPx())
                                lineTo(10.dp.toPx(), 20.dp.toPx())
                                close()
                            }
                            drawPath(bubble, bubbleColor)
                        }
                        .padding(20.dp, 10.dp).widthIn(max = 320.dp),
                    color = Theme.colors.textPrimary
                )
            }
        }
    }
}



