package top.fanua.rimuruAndroid.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.fanua.rimuruAndroid.data.Server
import top.fanua.rimuruAndroid.models.RimuruViewModel
import top.fanua.rimuruAndroid.models.UserViewModel
import top.fanua.rimuruAndroid.utils.Info
import top.fanua.rimuruAndroid.utils.UpdateUtils

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
    var port: Int? by remember { mutableStateOf(25565) }
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
                TextField(if (port == null) "" else port.toString(), onValueChange = { value ->
                    port = if (value.length in 1..5) {
                        val temp = value.filter { it.isDigit() }.toIntOrNull()
                        if (temp == null) null
                        else {
                            if (temp > 65535) 65535
                            else temp
                        }
                    } else if (value.length > 5) {
                        65535
                    } else null

                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text("端口")
                    })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (host.isNotEmpty() && serverName.isNotEmpty() && port != null) {
                        server(Server(host, port!!, serverName, "", false, 0))
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

@Composable
fun ErrorDialog(msg1: String, msg2: String, viewModel: RimuruViewModel, exit: Boolean = false) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text(msg1)
            },
            text = {
                Text(msg2)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (exit) {
                            if (viewModel.context != null) viewModel.context!!.finish()
                        } else {
                            openDialog.value = false
                        }
                    }
                ) {
                    Text(if (exit) "退出" else "返回")
                }
            }
        )
    }
}

@Composable
fun UpdateDialog(rimuruViewModel: RimuruViewModel) {
    val userViewModel: UserViewModel = viewModel()
    val start = remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = {
        },
        title = {
            Text("检测到更新")
        }, text = {
            Column {
                Text(
                    "当前版本:${rimuruViewModel.version}\n" +
                            "服务器版本:${rimuruViewModel.serverVersion}", modifier = Modifier.padding(horizontal = 20.dp)
                )
                if (userViewModel.first) {
                    rimuruViewModel.viewModelScope.launch(Dispatchers.IO) {
                        userViewModel.data = rimuruViewModel.updateUtils.getInfo()
                    }
                }

                MarkdownText("#### 更新日志", modifier = Modifier.padding(horizontal = 20.dp).padding(top = 5.dp))
                Surface(modifier = Modifier.height(80.dp)) {
                    LazyColumn(modifier = Modifier.padding(horizontal = 30.dp)) {
                        items(userViewModel.data.data) {
                            MarkdownText("* $it")
                        }
                    }
                }
                if (start.value) {
                    LinearProgressIndicator(
                        rimuruViewModel.updateUtils.pd,
                        modifier = Modifier.fillMaxWidth().height(10.dp).padding(horizontal = 10.dp)
                    )
                    Text(
                        "进度:${rimuruViewModel.updateUtils.pd * 100}%",
                        modifier = Modifier.fillMaxWidth().height(20.dp),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                } else {
                    Spacer(modifier = Modifier.height(30.dp).fillMaxWidth())
                }
            }
        },
        confirmButton = {
            TextButton(enabled = !start.value,
                onClick = {
                    start.value = true
                    rimuruViewModel.viewModelScope.launch(Dispatchers.IO) {
                        rimuruViewModel.updateUtils.update()
                    }
                }
            ) {
                Text("确认")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    rimuruViewModel.context?.finish()
                }
            ) {
                Text("退出")
            }
        }
    )

}

@Composable
fun AddAccountServer(auth: (String) -> Unit) {
    var openDialog by remember { mutableStateOf(true) }
    var adder: String by remember { mutableStateOf("https://") }
    AlertDialog(
        onDismissRequest = {
            if (!openDialog) auth("")
        },
        title = {
            Column {
                Text("添加外置服务器", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                TextField(
                    adder, onValueChange = {
                        adder = it
                    }, readOnly = !openDialog,
                    label = {
                        Text("地址")
                    }
                )
            }
        },
        confirmButton = {
            TextButton(enabled = openDialog,
                onClick = {
                    if (adder.isNotEmpty()) {
                        auth(adder)
                        openDialog = false
                    }
                }
            ) {
                Text("确认")
            }
        },
        dismissButton = {
            TextButton(enabled = openDialog,
                onClick = {
                    auth("")
                }
            ) {
                Text("取消")
            }
        }
    )

}
