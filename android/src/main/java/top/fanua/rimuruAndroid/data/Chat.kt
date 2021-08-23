package top.fanua.rimuruAndroid.data

import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull
import top.fanua.rimuruAndroid.utils.curTime

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
    var host: String,
    var port: Int,
    var name: String,
    var icon: String,
    var isLogin: Boolean,
    var online: Int
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
    val time: Long = curTime
)

@Serializable
data class Online(
    val uuid: String,
    val name: String,
    val gameMode: Int,
    val icon: String
)
