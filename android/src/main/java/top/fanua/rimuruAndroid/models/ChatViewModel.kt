package top.fanua.rimuruAndroid.models

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import top.fanua.rimuruAndroid.data.Chat
import top.fanua.rimuruAndroid.data.Msg
import top.fanua.rimuruAndroid.data.Role
import top.fanua.rimuruAndroid.data.Server
import top.fanua.rimuruAndroid.utils.curTime
import top.fanua.rimuruAndroid.utils.toLocalDate

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/9:22:35
 */
class ChatViewModel : ViewModel() {
    var chatList by mutableStateOf(
        mutableStateListOf<Chat>()
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
        chatList.forEach {
            if (it == currentChat) {
                it.msg.add(Msg(me, string, curTime))
            }
        }
    }

    var me by mutableStateOf(Role("uuid", "Doctor_Yin", "https://skin.blackyin.xyz/avatar/445?size=100"))
}
