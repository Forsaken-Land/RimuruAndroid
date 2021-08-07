package top.fanua.rimuruAndroid.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/7:22:32
 */
@Composable
fun Dialog(msg1: String, msg2: String, close: MutableState<Boolean>) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = msg1)
            },
            text = {
                Text(
                    msg2
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        close.value = true
                    }
                ) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        close.value = true
                    }
                ) {
                    Text("取消")
                }
            }
        )
    }
}
