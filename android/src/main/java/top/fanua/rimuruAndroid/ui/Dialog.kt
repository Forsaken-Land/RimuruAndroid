package top.fanua.rimuruAndroid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.substring
import androidx.lifecycle.viewmodel.compose.viewModel
import top.fanua.rimuruAndroid.data.Server
import top.fanua.rimuruAndroid.models.RimuruViewModel

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
                close.value = true
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

@Composable
fun AddServerPage(server: (Server?) -> Unit) {
    val rimuruViewModel: RimuruViewModel = viewModel()
    var host: String by remember { mutableStateOf("mc.blackyin.xyz") }
    var port: Int by remember { mutableStateOf(25565) }
    var serverName: String by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {
            server(null)
        },
        title = {
            Column {
                Text("添加服务器", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                TextField(
                    serverName, onValueChange = {
                        serverName = it.replace("\n", "")
                    },
                    label = {
                        Text("服务器名字")
                    }
                )
                TextField(
                    host, onValueChange = {
                        host = it
                    }, readOnly = !rimuruViewModel.enableEditHost,
                    label = {
                        Text("地址")
                    }
                )
                TextField(port.toString(), onValueChange = { value ->
                    port = if (value.length in 1..5) {
                        val temp = value.filter { it.isDigit() }.toInt()
                        if (temp > 65535) 65535
                        else temp
                    } else if (value.length > 5) {
                        65535
                    } else 1

                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text("端口")
                    })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (host.isNotEmpty() && serverName.isNotEmpty()) {
                        server(Server(host, port, serverName, ""))
                    }
                }
            ) {
                Text("确认")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    server(null)
                }
            ) {
                Text("取消")
            }
        }
    )
}
