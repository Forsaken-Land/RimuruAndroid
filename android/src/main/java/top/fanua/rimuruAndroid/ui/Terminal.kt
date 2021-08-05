package top.fanua.rimuruAndroid.ui

import android.R.attr.contentDescription
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.IconSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp


/**
 *
 * @author Doctor_Yin
 * @since 2021/8/4:2:20
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun terminal(msg: MutableState<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val text = remember { mutableStateOf("") }

    Column {
        Text(
            modifier = Modifier
                .background(Color.Gray)
                .fillMaxHeight(0.85f)
                .fillMaxWidth(),
            text = msg.value
        )
        Row {
            TextField(
                value = text.value,
                onValueChange = {
                    text.value = it
                },
                trailingIcon = {
                    IconButton(onClick = {
                        if (text.value.isNotEmpty()){
                            msg.value += text.value
                            text.value = ""
                        }

                    }) {
                        Icon(Icons.Filled.Send, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    keyboardController?.hide()
                    if (text.value.isNotEmpty()){
                        msg.value += text.value
                        text.value = ""
                    }

                }),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

        }


    }
}
