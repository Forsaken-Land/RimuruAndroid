package top.fanua.rimuruAndroid.client

import android.util.Log
import top.fanua.doctor.client.MinecraftClient
import top.fanua.doctor.client.entity.ForgeFeature
import top.fanua.doctor.client.entity.ServerInfo
import top.fanua.doctor.client.plugin.ClientAddListenerHook
import top.fanua.doctor.client.plugin.ClientPlugin
import top.fanua.doctor.client.running.AutoVersionForgePlugin
import top.fanua.doctor.core.api.plugin.IPluginHookManager
import top.fanua.doctor.core.api.plugin.IPluginManager
import top.fanua.doctor.core.plugin.addHandler
import top.fanua.doctor.plugin.forge.FML1Plugin
import top.fanua.doctor.plugin.forge.FML2Plugin

/**
 *
 * @author Doctor_Yin
 * @since 2021/8/14:11:46
 */
class ChangeLoginListener(val suffix: String, val name: String) : ClientPlugin {
    override lateinit var client: MinecraftClient
    var forgeFeature: ForgeFeature? = null
    lateinit var pluginManager: IPluginManager
    lateinit var hostSuffix: String
    override fun created(manager: IPluginManager) {
        this.pluginManager = manager
    }

    override fun beforeEnable(serverInfo: ServerInfo) {
        forgeFeature = serverInfo.forge?.forgeFeature

        // 注册插件
        if (serverInfo.forge != null) when (serverInfo.forge!!.forgeFeature) {
            ForgeFeature.FML1 -> pluginManager.registerPlugin(FML1Plugin(serverInfo.forge!!.modMap))
            ForgeFeature.FML2 -> pluginManager.registerPlugin(FML2Plugin(serverInfo.forge!!.modMap))
        }
        hostSuffix = if (serverInfo.forge == null) "" else serverInfo.forge!!.forgeFeature.getForgeVersion()
    }

    override fun registerHook(manager: IPluginHookManager) {
        manager.getHook(ClientAddListenerHook).addHandler(this) {
            it.message = LoginListener(suffix = suffix, name = name)
            true
        }
    }
}

val MinecraftClient.forgeFeature: ForgeFeature?
    get() = plugin<AutoVersionForgePlugin>()?.forgeFeature
