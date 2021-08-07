package top.fanua.rimuruAndroid.utils

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import top.fanua.rimuruAndroid.data.User
import java.io.*

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/7:18:55
 */
@OptIn(InternalCoroutinesApi::class)
class UserViewModel(
    private val accountPath: String,
    val accountFiles: MutableState<List<File>>,
    val accounts: MutableMap<String, File>
) : ViewModel() {

    private val fileExtension = "@doctor@"


    suspend fun login(email: String, password: String): LoginStatus {
        return withContext(Dispatchers.IO) {
            val job = viewModelScope.launch(Dispatchers.IO) {
                val client = OkHttpClient()
                val getRequest: Request = Request.Builder()
                    .url("https://api.btstu.cn/sjtx/api.php?lx=b1")
                    .get()
                    .build()
                val url = client.newCall(getRequest).execute().request().url().url().toString()
                val user = User(email, url, password, true)
                val file = File("$accountPath/$email.$fileExtension")
                FileUtils.writeFile(file, user)
            }
            while (job.isActive) {
            }
            return@withContext if (job.isCompleted) LoginStatus.OK
            else if (job.isCancelled) LoginStatus.ERROR.also {
                it.msg = job.getCancellationException().message.toString()
            } else LoginStatus.UNKNOWN
        }
    }

    fun delUser(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            refresh()
            val file = accounts[email]
            file?.delete()
            refresh()
        }

    }

    fun signOut(email: MutableState<String>): LoginStatus {
        val job = viewModelScope.launch(Dispatchers.IO) {
            val file = accounts[email.value]!!
            val user = FileUtils.readFile<User>(file)
            FileUtils.writeFile(file, user.also {
                it.isLogin = false
            })
            refresh()
            Thread.sleep(10L)
            email.value = ""
        }
        while (true) {
            return if (job.isCompleted) LoginStatus.OK
            else if (job.isCancelled) LoginStatus.ERROR.also {
                it.msg = job.getCancellationException().message.toString()
            }
            else LoginStatus.UNKNOWN
        }
    }


    fun refresh() {
        accountFiles.value = File(accountPath).walk()
            .maxDepth(1)
            .filter { it.isFile }
            .filter { it.extension == "@doctor@" }
            .toList()
        accounts.clear()
        accountFiles.value.forEach {
            accounts[it.name.replace(".$fileExtension", "")] = it
        }
    }

    fun getUserDir(): File = File(accountPath)

}

enum class LoginStatus(var msg: String) {
    OK(""),
    ERROR(""),
    UNKNOWN(""),
    WAITING("")
}
