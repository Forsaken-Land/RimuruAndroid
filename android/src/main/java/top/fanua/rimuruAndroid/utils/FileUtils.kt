package top.fanua.rimuruAndroid.utils

import android.content.ContentValues
import android.icu.text.DateTimePatternGenerator.PatternInfo.OK
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import top.fanua.rimuruAndroid.MainActivity
import top.fanua.rimuruAndroid.data.User
import java.io.*

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/7:20:38
 */
object FileUtils {
    inline fun <reified T> readFile(file: File): T {
        val stringBuilder = StringBuilder()
        val bufferedReader = BufferedReader(InputStreamReader(FileInputStream(file)))
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null
        ) {
            stringBuilder.append(line)
        }
        bufferedReader.close()
        return Json.decodeFromString(stringBuilder.toString())
    }

    inline fun <reified T> writeFile(file: File, json: T) {
        if (file.exists()) {
            val d1 = file.delete()
            Log.d(ContentValues.TAG, "delete old file: $d1")
        }
        val c1 = file.createNewFile()
        Log.d(ContentValues.TAG, "create new file: $c1")
        val os: OutputStream = FileOutputStream(file)
        os.write(Json.encodeToString(json).toByteArray())
        os.flush()
        os.close()
    }

}

