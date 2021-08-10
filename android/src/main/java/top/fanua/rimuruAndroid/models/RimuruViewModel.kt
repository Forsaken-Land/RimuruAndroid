package top.fanua.rimuruAndroid.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import top.fanua.rimuruAndroid.data.Account
import top.fanua.rimuruAndroid.data.Server
import top.fanua.rimuruAndroid.models.LoginStatus
import top.fanua.rimuruAndroid.ui.sustomStuff.Screen
import top.fanua.rimuruAndroid.ui.theme.Theme
import top.fanua.rimuruAndroid.utils.FileUtils
import java.io.File

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/7:19:00
 */
class RimuruViewModel : ViewModel() {
    var currentScreen by mutableStateOf<Screen>(Screen.Chat)
    private val accountPath = "/accounts"
    private val loginAccount = "/loginAccount"
    var theme by mutableStateOf(Theme.Type.Light)
    var loginEmail by mutableStateOf("")
    var lastEmail by mutableStateOf("")
    var path by mutableStateOf("")
    var accounts by mutableStateOf(
        mutableStateListOf<Account>()
    )
    var radian by mutableStateOf(5)
    var servers by mutableStateOf(mutableStateListOf<Server>())

    var enableEditHost by mutableStateOf(false)
    fun addServer(server: Server) {
        servers.forEach {
            if (it.host == server.host && it.port == server.port) {
                return
            }
        }

        servers.add(server)
    }

    fun delAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            accounts.remove(account)
            TODO()
        }
    }

    fun refresh() {
        val localFiles = File("$path$accountPath").walk()
            .maxDepth(1)
            .filter { it.isFile }
            .filter { it.extension == "@doctor@" }
            .toList()
        val localAccount = mutableStateListOf<Account>()
        localFiles.forEach { file ->
            localAccount.add(FileUtils.readFile(file))
        }
        accounts = localAccount

    }

    @OptIn(InternalCoroutinesApi::class)
    suspend fun loginAccount(email: String, password: String): LoginStatus {
        val job = viewModelScope.launch(Dispatchers.IO) {
            withTimeout(5000L) {
                delay(6000)
            }
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
            LoginStatus.OK
        } else LoginStatus.UNKNOWN
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            lastEmail = loginEmail
            loginEmail = ""
        }
    }
}

fun MutableList<Account>.get(string: String): Account? {
    if (isEmpty()) return null
    forEach {
        if (it.email == string) return it
    }
    return null
}

