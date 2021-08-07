package top.fanua.rimuruAndroid.data

import kotlinx.serialization.Serializable

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/7:18:38
 */
@Serializable
data class User(
    val email: String,
    var imgUrl: String,
    val password: String,
    var isLogin: Boolean
)
