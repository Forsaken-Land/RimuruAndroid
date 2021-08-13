package top.fanua.rimuruAndroid.models

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.forEach
import okhttp3.internal.wait
import top.fanua.rimuruAndroid.data.*
import top.fanua.rimuruAndroid.ui.get
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen.Items.list
import top.fanua.rimuruAndroid.ui.theme.Theme
import top.fanua.rimuruAndroid.ui.toChat
import top.fanua.rimuruAndroid.utils.ImageUtils
import top.fanua.rimuruAndroid.utils.UserUtils
import top.fanua.rimuruAndroid.utils.curTime
import top.fanua.rimuruAndroid.utils.write
import java.io.File

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/7:19:00
 */
class RimuruViewModel : ViewModel() {
    var loading by mutableStateOf(true)
    var currentScreen by mutableStateOf<Screen>(Screen.Chat)


    private val authServer: String = "https://skin.blackyin.xyz/api/yggdrasil/authserver/"
    private val sessionServer: String = "https://skin.blackyin.xyz/api/yggdrasil/sessionserver/"

    var accountDao: AccountDao? by mutableStateOf(null)
    var theme by mutableStateOf(Theme.Type.Light)
    var loginEmail by mutableStateOf("")
    var lastEmail by mutableStateOf("")
    var path by mutableStateOf("")
    var accounts by mutableStateOf(listOf<EmailWithPassword>())
    var radian by mutableStateOf(5)
    var servers by mutableStateOf(listOf<SaveServer>())

    var enableEditHost by mutableStateOf(false)


    fun addServer(server: Server) {
        servers.forEach {
            if (server.name == it.name || (server.port == it.port && server.host == it.host)) return
        }
        viewModelScope.launch(Dispatchers.IO) {
            accountDao!!.insertSaveServer(
                SaveServer(
                    email = loginEmail,
                    host = server.host,
                    port = server.port,
                    icon = "https://www.mcmod.cn/images/favicon.ico",
                    name = server.name
                )
            )
        }
    }

    fun delAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            TODO()
        }
    }


    @OptIn(InternalCoroutinesApi::class)
    suspend fun loginAccount(email: String, password: String): LoginStatus {
        val job = viewModelScope.launch(Dispatchers.IO) {
            if (accounts.size > 5) cancel("账号过多")
            updateAccount(email, password)
        }
        while (job.isActive) {
        }
        return if (job.isCancelled) {
            if (job.getCancellationException() is TimeoutCancellationException) {
                LoginStatus.ERROR.also {
                    it.msg = "登录超时"
                }
            } else {
                LoginStatus.ERROR.also {
                    it.msg = job.getCancellationException().message.toString()
                }
            }
        } else if (job.isCompleted) {
            changeLoginEmail(email)
            LoginStatus.OK
        } else LoginStatus.UNKNOWN
    }

    private suspend fun updateAccount(email: String, password: String) {
        withTimeout(5000L) {
            val ygg = UserUtils(authServer, sessionServer)

            val thread = kotlin.runCatching {
                ygg.loginYgg(email, password)
            }

            var configList = mutableListOf<String>()
            thread.fold(
                onSuccess = {
                    configList = it as MutableList<String>
                },
                onFailure = {
                    Log.e("error", it.message.orEmpty())
                    return@withTimeout cancel("密码错误，或短时间内多次登录失败而被暂时禁止登录")
                }
            )
            while (configList.isEmpty()) {
            }
            if (configList.size == 1) cancel("账号下未绑定角色")

            val token = configList[0]
            val name = configList[1]
            val uuid = configList[2]
            val textures = configList[3]

            if (textures.isEmpty()) cancel("账号下无皮肤参数")

            //头像相关

            val iconPath = "$path/icons"

            val handler = ImageUtils.downloadImg(textures)

            val onIcon = File("$iconPath/${handler.hash!!}.on")
            val icon = File("$iconPath/${handler.hash!!}")
            if (!icon.exists()) icon.write(handler.underBitmap)
            if (!onIcon.exists()) onIcon.write(handler.onBitmap)

            //保存账号相关
            val local = accounts.get(email)?.saveAccount
            if (local != null) {
                accountDao!!.updateSaveAccount(
                    local.copy(
                        accessToken = token,
                        name = name,
                        uuid = uuid,
                        authServer = authServer,
                        sessionServer = sessionServer,
                        icon = "$iconPath/${handler.hash!!}"
                    )
                )
                accountDao!!.updatePassword(Password(email, password))
            } else {
                accountDao!!.insertSaveAccount(
                    SaveAccount(
                        email,
                        token,
                        uuid,
                        name,
                        "$iconPath/${handler.hash!!}",
                        10,
                        authServer,
                        sessionServer
                    ), Password(email, password)
                )
            }


        }
    }


    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            changeLastEmail(loginEmail)
            changeLoginEmail()
        }
    }

    fun refreshAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            updateAccount(loginEmail, accounts.get(loginEmail)!!.password.password)
        }
    }

    var chatList by mutableStateOf(mutableMapOf<ChatMap, ServerWithChats?>())

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
        viewModelScope.launch(Dispatchers.IO) {
            chatList.map { (email, chat) ->
                if ((chat != null) && (chat.toChat().server == currentChat!!.server) && (email.email == loginEmail)) {
                    val user = accounts.get(loginEmail)!!.saveAccount
                    servers.forEach {
                        if (it.name == chat.server.name) {
                            accountDao!!.insertSaveChats(
                                SaveChat(
                                    ownerId = it.uid!!,
                                    text = string,
                                    time = curTime,
                                    uuid = user.uuid!!,
                                    name = user.name!!,
                                    icon = user.icon!!
                                )
                            )
                        }
                    }

                }
            }
        }

    }

    var me by mutableStateOf(Role("", "", ""))

    private suspend fun changeLoginEmail(email: String? = null) {
        val local = accountDao!!.getConfig("loginEmail").firstOrNull()
        if (local != null) accountDao!!.updateConfig(local.copy(value = email.orEmpty()))
        else accountDao!!.insertConfig(Config(key = "loginEmail", value = email.orEmpty()))
    }

    private suspend fun changeLastEmail(email: String? = null) {
        val local = accountDao!!.getConfig("lastEmail").firstOrNull()
        if (local != null) accountDao!!.updateConfig(local.copy(value = email.orEmpty()))
        else accountDao!!.insertConfig(Config(key = "lastEmail", value = email.orEmpty()))
    }

    fun changeRadian(int: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val local = accounts.get(loginEmail)?.saveAccount
            if (local != null) {
                accountDao!!.updateSaveAccount(local.copy(radian = int))
            }
        }

    }
}

data class ChatMap(
    val email: String,
    val name: String
)



