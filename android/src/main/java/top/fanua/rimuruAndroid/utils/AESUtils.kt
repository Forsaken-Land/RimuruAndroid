package top.fanua.rimuruAndroid.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@SuppressLint("GetInstance")
@RequiresApi(Build.VERSION_CODES.O)
object AESUtils {

    /**
     * ### 加密
     */
    fun encrypt(input: String, password: String): String {
        if (input.isEmpty()) return input
        val cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(password.toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encrypt = cipher.doFinal(input.toByteArray())
        return String(Base64.getEncoder().encode(encrypt))
    }

    /**
     * ### 解密
     */
    fun decrypt(input: String, password: String): String {
        if (input.isEmpty()) return input

        val cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(password.toByteArray(), "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        val decode = cipher.doFinal(Base64.getDecoder().decode(input.toByteArray()))
        return String(decode)
    }

}
