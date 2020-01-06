package eu.thesimplecloud.plugin.proxy

import eu.thesimplecloud.api.CloudAPI
import eu.thesimplecloud.api.service.ICloudService
import eu.thesimplecloud.api.service.ServiceState
import eu.thesimplecloud.api.servicegroup.grouptype.ICloudServerGroup
import eu.thesimplecloud.plugin.listener.CloudListener
import eu.thesimplecloud.plugin.proxy.listener.BungeeListener
import eu.thesimplecloud.plugin.startup.CloudPlugin
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import java.net.InetSocketAddress
import java.net.URLClassLoader

class CloudProxyPlugin : Plugin(), ICloudProxyPlugin {

    val lobbyConnector = LobbyConnector()

    companion object {
        @JvmStatic
        lateinit var instance: CloudProxyPlugin
    }

    init {
        instance = this
    }

    override fun shutdown() {
        ProxyServer.getInstance().stop()
    }

    override fun addServiceToProxy(cloudService: ICloudService) {
        val serviceState = cloudService.getState()
        if (cloudService.getServiceType().isProxy() || serviceState == ServiceState.CLOSED || serviceState == ServiceState.PREPARED)
            return
        val cloudServiceGroup = cloudService.getServiceGroup()
        if ((cloudServiceGroup as ICloudServerGroup).getHiddenAtProxyGroups().contains(CloudPlugin.instance.getGroupName()))
            return
        println("Registered service ${cloudService.getName()}")
        val socketAddress = InetSocketAddress(cloudService.getHost(), cloudService.getPort())
        val info = ProxyServer.getInstance().constructServerInfo(cloudService.getName(), socketAddress,
                cloudService.getUniqueId().toString(), false)
        ProxyServer.getInstance().servers[cloudService.getName()] = info
    }

    override fun removeServiceFromProxy(cloudService: ICloudService) {
        ProxyServer.getInstance().servers.remove(cloudService.getName())
    }

    override fun onLoad() {
        ProxyServer.getInstance().reconnectHandler = ReconnectHandlerImpl()
        val classLoader = URLClassLoader(arrayOf(this.file.toURI().toURL()))
        CloudPlugin(this, classLoader)
    }

    override fun onEnable() {
        ProxyServer.getInstance().configurationAdapter.servers.clear()
        ProxyServer.getInstance().servers.clear()
        for (info in ProxyServer.getInstance().configurationAdapter.listeners) {
            info.serverPriority.clear()
        }
        CloudAPI.instance.getCloudServiceManger().getAllCloudServices().forEach { addServiceToProxy(it) }
        CloudPlugin.instance.onEnable()
        CloudAPI.instance.getEventManager().registerListener(CloudPlugin.instance, CloudListener())
        ProxyServer.getInstance().pluginManager.registerListener(this, BungeeListener())

    }

    override fun onDisable() {
        CloudPlugin.instance.onDisable()
    }

}