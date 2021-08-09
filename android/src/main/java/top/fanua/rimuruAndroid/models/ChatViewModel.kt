package top.fanua.rimuruAndroid.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import top.fanua.rimuruAndroid.data.Chat
import top.fanua.rimuruAndroid.data.Msg
import top.fanua.rimuruAndroid.data.Role
import top.fanua.rimuruAndroid.data.Server

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/9:22:35
 */
class ChatViewModel : ViewModel() {
    var chatList by mutableStateOf(
        mutableListOf<Chat>(
            Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ), Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            ),
            Chat(
                Server(
                    "mc.blackyin.xyz",
                    25565,
                    "测试",
                    "https://skin.blackyin.xyz/avatar/445?size=100"
                ),
                mutableListOf(
                    Msg(
                        Role(
                            "uuid",
                            "Doctor_Yin",
                            "https://skin.blackyin.xyz/avatar/445?size=100"
                        ),
                        "测试信息", 1234L
                    )
                )
            )


        )
    )
    var currentChat: Chat? by mutableStateOf(null)
    var chatting by mutableStateOf(false)
    fun startChat(chat: Chat) {
        chatting = true
        currentChat = chat
    }

    fun endChat() {
        chatting = false
    }

    fun sendMessage(string: String) {

    }
}
