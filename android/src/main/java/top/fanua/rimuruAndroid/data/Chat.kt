package top.fanua.rimuruAndroid.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/8:18:23
 */
@Serializable
data class Role(
    val uuid: String,
    val name: String,
    val icon: String
)

@Serializable
data class Server(
    val host: String,
    val port: Int,
    val name: String,
    val icon: String
)

@Serializable
data class Chat(
    val server: Server,
    val msg: MutableList<Msg>
)

@Serializable
data class Msg(
    val from: Role,
    val text: String,
    val time: Long
)
