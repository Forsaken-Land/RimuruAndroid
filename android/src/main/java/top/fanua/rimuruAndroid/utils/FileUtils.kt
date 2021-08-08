package top.fanua.rimuruAndroid.utils

import android.graphics.Bitmap
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
        if (file.exists())file.delete()
        file.createNewFile()
        val os: OutputStream = FileOutputStream(file)
        os.write(Json.encodeToString(json).toByteArray())
        os.flush()
        os.close()
    }

    fun File.write(byteArray: ByteArray) {
        if (!parentFile!!.exists()) parentFile!!.mkdirs()
        if (exists()) delete()
        if (!exists()) createNewFile()
        val os: OutputStream = FileOutputStream(this)
        os.write(byteArray)
        os.flush()
        os.close()
    }

    fun File.write(bitmap: Bitmap) {
        if (!parentFile!!.exists()) parentFile!!.mkdirs()
        if (exists()) delete()
        if (!exists()) createNewFile()
        val os: OutputStream = FileOutputStream(this)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        os.flush()
        os.close()
    }


}

