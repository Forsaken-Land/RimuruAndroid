package top.fanua.rimuruAndroid.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.statusBarsHeight
import top.fanua.rimuruAndroid.R
import top.fanua.rimuruAndroid.models.ChatViewModel
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.ui.theme.InputColor
import top.fanua.rimuruAndroid.ui.theme.Theme
import top.fanua.rimuruAndroid.ui.theme.TransparentInputField
import kotlin.math.roundToInt

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/10:0:09
 */
@Composable
fun ChatPage() {
    val chatViewModel: ChatViewModel = viewModel()
    if (chatViewModel.currentChat != null) {
        val chat = chatViewModel.currentChat!!
        val percentOffset = animateFloatAsState(if (chatViewModel.chatting) 0f else 1f)
        Scaffold(
            Modifier.percentOffsetX(percentOffset.value).background(Theme.colors.background),
            topBar = {
                TopBar(chat.server.name) { chatViewModel.endChat() }
            },
            bottomBar = {
                ChatBottomBar(chatViewModel)
            }
        ) { paddingValues ->
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(chat.msg.size) { index ->
                    val msg = chat.msg[index]
//                    MessageItem(msg, shakingTime, chat.msgs.size - index - 1)
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

@Composable
fun ChatBottomBar(chatViewModel: ChatViewModel) {
    val viewModel: RimuruViewModel = viewModel()
    var msg: String by remember { mutableStateOf("") }
    UserInput(onMessageSent = {
        msg = it
    })
}

@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    keyboardShown: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    .align(Alignment.Bottom)
            ) {
                var lastFocusState by remember { mutableStateOf(false) }
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { onTextChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                        .align(Alignment.CenterStart)
                        .onFocusChanged { state ->
                            if (lastFocusState != state.isFocused) {
                                onTextFieldFocused(state.isFocused)
                            }
                            lastFocusState = state.isFocused
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Send
                    ),
                    maxLines = 1,
                    cursorBrush = SolidColor(LocalContentColor.current),
                    textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
                )

                val disableContentColor =
                    MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                if (textFieldValue.text.isEmpty() && !focusState) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp),
                        text = stringResource(id = R.string.textfield_hint),
                        style = MaterialTheme.typography.body1.copy(color = disableContentColor)
                    )
                }
            }
        }
    }
}
