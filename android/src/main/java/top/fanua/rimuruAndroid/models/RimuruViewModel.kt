package top.fanua.rimuruAndroid.models

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import top.fanua.doctor.allLoginPlugin.enableAllLoginPlugin
import top.fanua.doctor.client.MinecraftClient
import top.fanua.doctor.client.running.AutoVersionForgePlugin
import top.fanua.doctor.client.utils.ServerInfoUtils
import top.fanua.doctor.network.api.Connection
import top.fanua.doctor.network.event.ConnectionEvent
import top.fanua.doctor.network.event.NetLifeCycleEvent
import top.fanua.doctor.network.handler.onPacket
import top.fanua.doctor.network.handler.replyPacket
import top.fanua.doctor.protocol.definition.login.client.EncryptionResponsePacket
import top.fanua.doctor.protocol.definition.login.server.EncryptionRequestPacket
import top.fanua.doctor.protocol.definition.play.client.*
import top.fanua.rimuruAndroid.client.LoginListener
import top.fanua.rimuruAndroid.data.*
import top.fanua.rimuruAndroid.ui.get
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.theme.Theme
import top.fanua.rimuruAndroid.ui.toChat
import top.fanua.rimuruAndroid.utils.*
import top.limbang.minecraft.yggdrasil.YggdrasilApi
import top.limbang.minecraft.yggdrasil.model.JoinRequest
import top.limbang.minecraft.yggdrasil.model.Token
import java.io.File
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

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

    var clients by mutableStateOf(mutableStateListOf<MinecraftClient>())

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
                    name = server.name,
                    isLogin = false
                )
            )
        }
    }

    fun delServer(server: Server) {
        viewModelScope.launch(Dispatchers.IO) {
            servers.forEach {
                if (server.name == it.name && it.email == loginEmail) {
                    accountDao!!.delSaveServer(it)
                }
            }
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

    fun validateYggdrasilSession() {
        val ygg = YggdrasilApi(authServer, sessionServer).createService()
        runBlocking {
            val ok = ygg.validate(Token(accounts.get(loginEmail)!!.saveAccount.accessToken!!))
            if (!ok) {
                val thread = kotlin.runCatching {
                    ygg.refresh(Token(accounts.get(loginEmail)!!.saveAccount.accessToken!!))
                }
                thread.fold(
                    onSuccess = {
                        val local = accounts.get(loginEmail)?.saveAccount
                        accountDao!!.updateSaveAccount(
                            local!!.copy(
                                accessToken = it.accessToken
                            )
                        )
                    },
                    onFailure = {
                        refreshAccount()
                    }
                )
            }
        }
    }

    private fun saveImg(uuid: String): String {
        var hash = ""
        viewModelScope.launch(Dispatchers.IO) {
            val ygg = YggdrasilApi(authServer, sessionServer).createService()
            val textures = ygg.profile(uuid.replace("-", "")).properties!!.get("textures").orEmpty()
            val iconPath = "$path/icons"
            val texture = String(Base64.decode(textures.toByteArray(), Base64.DEFAULT))
            val textureUrl = Json.parseToJsonElement(texture)
                .jsonObject["textures"]!!
                .jsonObject["SKIN"]!!
                .jsonObject["url"]!!
                .jsonPrimitive.content
            hash = textureUrl.substring(textureUrl.lastIndexOf('/'))
            val onIcon = File("$iconPath/${hash}.on")
            val icon = File("$iconPath/${hash}")
            if (!icon.exists()) {
                val handler = ImageUtils.downloadImg(textures)
                if (!icon.exists()) icon.write(handler.underBitmap)
                if (!onIcon.exists()) onIcon.write(handler.onBitmap)
            }
        }
        while (hash.isEmpty()) {
        }
        return hash
    }

    fun encryption(packet: EncryptionRequestPacket, connection: Connection) {
        val sharedSecret = SecurityUtils.generateSharedKey()
        val publicKey = SecurityUtils.decodePublicKey(packet.publicKey)
        val secret = SecurityUtils.encryptRSA(publicKey, sharedSecret.encoded)
        val verify = SecurityUtils.encryptRSA(publicKey, packet.verifyToken)
        val serverHash = SecurityUtils.generateAuthHash(packet.serverID, sharedSecret, publicKey)
        val ygg = YggdrasilApi(authServer, sessionServer).createService()
        runBlocking {
            ygg.join(
                JoinRequest(
                    accounts.get(loginEmail)!!.saveAccount.accessToken!!,
                    accounts.get(loginEmail)!!.saveAccount.uuid!!,
                    serverHash
                )
            )
        }
        connection.sendPacket(EncryptionResponsePacket(secret, verify))
        connection.setEncryptionEnabled(sharedSecret)


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

    fun start() {
        viewModelScope.launch(Dispatchers.IO) {
            servers.forEach { server ->
                server.isLogin = false
                accountDao!!.updateSaveServer(server)
            }
        }
        servers.forEach { server ->
            if (server.email == loginEmail) {
                viewModelScope.launch(Dispatchers.IO) {
                    Thread.sleep(1000L)
                    val jsonStr: String
                    try {
                        jsonStr = MinecraftClient.ping(server.host, server.port).get(2000, TimeUnit.MILLISECONDS)
                    } catch (e: TimeoutException) {
                        cancel("获取ping信息,等待超时...")
                        return@launch
                    } catch (e: ExecutionException) {
                        cancel("获取ping信息失败,${e.message}")
                        return@launch
                    }
                    val serverInfo = try {
                        ServerInfoUtils.getServiceInfo(jsonStr)
                    } catch (e: NullPointerException) {
                        cancel("服务器正在启动，请等待...")
                        return@launch
                    }
                    val client = MinecraftClient.builder()
                        .plugin(AutoVersionForgePlugin())
                        .enableAllLoginPlugin()
                        .build()

                    clients.forEach { minecraftClient ->
                        if (minecraftClient == client) return@launch
                    }
                    clients.add(client)
                    client.start(
                        server.host,
                        server.port,
                        5000,
                        LoginListener(
                            protocolVersion = serverInfo.versionNumber,
                            name = accounts.get(loginEmail)!!.saveAccount.name!!,
                            suffix = serverInfo.forge?.forgeFeature?.getForgeVersion().orEmpty(),
                            viewModel = this@RimuruViewModel
                        )
                    )

                    client.onPacket<DisconnectPacket> {
                        Log.e("disconnect", packet.reason)
                    }.on(ConnectionEvent.Disconnect) {
                        server.isLogin = false
                        accountDao!!.updateSaveServer(server)
                        Thread.sleep(5000)
                        client.reconnect()
                    }.onPacket<ChatPacket> {
                        if (packet.json.contains("\"text\"") &&
                            packet.json.contains("\"hoverEvent\"") &&
                            packet.json.contains("\"extra\"") &&
                            packet.json.contains("\"value\"") &&
                            packet.json.contains("id") &&
                            packet.json.contains("name")
                        ) {
                            val json = Json.parseToJsonElement(packet.json).jsonObject["extra"]!!
                            val text =
                                json.jsonArray[1].jsonObject["text"]!!.jsonPrimitive.content
                            val temp =
                                json.jsonArray[0]
                                    .jsonObject["extra"]!!.jsonArray[1]
                                    .jsonObject["hoverEvent"]!!.jsonObject["value"]!!.jsonObject["text"]!!.jsonPrimitive.content
                            val jsonTemp =
                                Json.parseToJsonElement(temp.replace("name", "\"name\"").replace("id", "\"id\""))
                            val uuid = jsonTemp.jsonObject["id"]!!.jsonPrimitive.content
                            val name = jsonTemp.jsonObject["name"]!!.jsonPrimitive.content
                            val icon = saveImg(uuid)
                            sendLocalMessage(text, Role(uuid, name, icon), connection.host, connection.port)
                        }
                    }.onPacket<PlayerPositionAndLookPacket> {
                        server.isLogin = true
                        accountDao!!.updateSaveServer(server)
                    }
                    while (true) {
                        delay(5000L)
                        var del = true
                        servers.forEach {
                            if (it == server) del = false
                        }
                        if (del) {
                            client.stop()
                            break
                        }
                        delay(5000L)
                    }
                }
            }
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
        if (currentChat != null) {
            clients.forEach { client ->
                if (client.connection.host == currentChat!!.server.host && currentChat!!.server.port == client.connection.port) {
                    if (string[0] != '/') {
                        client.sendMessage(string)
                    } else {
                        client.sendMessage(".$string")
                    }
                }
            }
        }
    }

    private fun sendLocalMessage(string: String, role: Role, host: String, port: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            servers.forEach {
                if (it.host == host && it.port == port) {
                    accountDao!!.insertSaveChats(
                        SaveChat(
                            ownerId = it.uid!!,
                            text = string,
                            time = curTime,
                            uuid = role.uuid.replace("-", ""),
                            name = role.name,
                            icon = "$path/icons/${role.icon}"
                        )
                    )
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



