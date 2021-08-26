package top.fanua.rimuruAndroid.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Process
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import top.fanua.rimuruAndroid.BuildConfig
import top.fanua.rimuruAndroid.models.RimuruViewModel
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.serialization.SerialName


/**
 *
 * @author Doctor_Yin
 * @since 2021/8/25:18:48
 */
class UpdateUtils(private val viewModel: RimuruViewModel) {
    var pd by mutableStateOf(0.0f)
    private var updateUrl by mutableStateOf("")
    var ok by mutableStateOf(false)
    suspend fun checkVersion(): String {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val url = "https://gitee.com/Doctor_Yin/mc-bot/raw/master/update.json"
                val json = String(
                    OkHttpClient().newCall(Request.Builder().get().url(url).build()).execute().body!!.byteStream()
                        .readBytes()
                )
                val update = Json.decodeFromString<Update>(json)
                updateUrl = update.downloadUrl

                update.serverVersion
            } catch (e: Exception) {
                viewModel.version
            }
        }
    }

    suspend fun getInfo(): Info {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val url = "https://gitee.com/Doctor_Yin/mc-bot/raw/master/info.json"
                val json = String(
                    OkHttpClient().newCall(Request.Builder().get().url(url).build()).execute().body!!.byteStream()
                        .readBytes()
                )
                val info = Json.decodeFromString<Info>(json)
                info
            } catch (e: Exception) {
                Info(listOf("修复多个模块的若干问题，提升稳定性"))
            }
        }
    }

    suspend fun update() {
        withContext(Dispatchers.IO) {
            downloadFile()
            ok = true
        }
    }

    fun installApk(mContext: Context) {
        val fileUri = FileProvider.getUriForFile(
            mContext,
            mContext.packageName + ".fileprovider",
            File("${viewModel.path}/app.apk")
        )


        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(
                Uri.fromFile(File("${viewModel.path}/app.apk")),
                "application/vnd.android.package-archive"
            )
        }
        mContext.startActivity(intent)
        Process.killProcess(Process.myPid())

    }

    private fun downloadFile() {
        if (updateUrl.isEmpty()) {
            viewModel.needUpdate = false
            return
        }

        var ok = false
        OkHttpClient().newCall(Request.Builder().url(updateUrl).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val inputStream = response.body!!.byteStream()
                val length = response.body!!.contentLength()
                val bis = BufferedInputStream(inputStream)

                val file = File("${viewModel.path}/app.apk")
                if (file.exists()) file.delete()
                if (!file.exists()) file.createNewFile()
                val os = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var len: Int
                var total = 0
                while (bis.read(buffer).also { len = it } != -1) {
                    os.write(buffer, 0, len)
                    total += len
                    pd = total.toFloat() / length.toFloat()
                }
                os.close()
                bis.close()
                inputStream.close()
                ok = true
            }

        })
        while (!ok) {
        }
    }
}

@Serializable
data class Update(
    val serverVersion: String,
    val downloadUrl: String
)

@Serializable
data class Info(
    val data: List<String>
)
