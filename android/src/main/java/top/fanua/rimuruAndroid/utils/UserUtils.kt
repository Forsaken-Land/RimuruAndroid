package top.fanua.rimuruAndroid.utils

import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.NotNull
import top.limbang.minecraft.yggdrasil.YggdrasilApi
import top.limbang.minecraft.yggdrasil.model.AuthenticateRequest
import top.limbang.minecraft.yggdrasil.model.Profile
import top.limbang.minecraft.yggdrasil.service.YggdrasilApiService

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/13:21:39
 */
class UserUtils(authServer: String, sessionServer: String) {
    private val service = YggdrasilApi(authServer, sessionServer).createService()

    suspend fun loginYgg(@NotNull email: String, @NotNull password: String): List<String> {
        val list = mutableListOf<String>()
        val token = service.authenticate(AuthenticateRequest(email, password))
        list.add(token.accessToken)
        if (token.selectedProfile == null) return list
        val name = token.selectedProfile!!.name
        list.add(name)
        val uuid = token.selectedProfile!!.id
        list.add(uuid)
        val textures = service.profile(uuid).properties!!.get("textures").orEmpty()
        list.add(textures)
        return list
    }

}

fun List<Profile.Properties>.get(index: String): String? {
    forEach {
        if (it.name == index) return it.value
    }
    return null
}
