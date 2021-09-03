package top.fanua.rimuruAndroid.data

import kotlinx.serialization.Serializable

/**
 *
 * @author Doctor_Yin
 * @since 2021/9/3:18:28
 */
@Serializable
data class AllVersion(
    val dev: String,
    val release: String
)
