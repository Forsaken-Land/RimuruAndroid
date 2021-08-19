package top.fanua.rimuruAndroid.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.core.graphics.get
import androidx.core.graphics.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url
import top.fanua.rimuruAndroid.utils.ImageUtils.downloadImg
import top.limbang.minecraft.yggdrasil.service.YggdrasilApiService

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/10:18:59
 */
object ImageUtils {
    data class Header(
        val underBitmap: Bitmap,
        val onBitmap: Bitmap,
        var hash: String? = null
    )

    private fun drawImg(bitmap: Bitmap, size: Int): Header {
        val underBitmap = Bitmap.createBitmap(size * 8, size * 8, Bitmap.Config.ARGB_8888)
        val onBitmap = Bitmap.createBitmap(size * 8, size * 8, Bitmap.Config.ARGB_8888)
        for (x in 0..7) {
            for (y in 0..7) {
                for (i in 0 until size) {
                    for (j in 0 until size) {
                        underBitmap[(x * size) + j, (y * size) + i] = bitmap[x + 8, y + 8]
                        onBitmap[(x * size) + j, (y * size + i)] = bitmap[x + 40, y + 8]
                    }
                }
            }
        }
        return Header(underBitmap, onBitmap)
    }

    suspend fun downloadImg(url: String, size: Int = 16): Header {
        return withContext(Dispatchers.IO) {

            val hash = url.substring(url.lastIndexOf('/'))
            val inputStream =
                OkHttpClient().newCall(Request.Builder().get().url(url).build()).execute().body!!.byteStream()

            val img = drawImg(BitmapFactory.decodeStream(inputStream), size)
            inputStream.close()
            return@withContext img.copy(hash = hash)
        }
    }
}


