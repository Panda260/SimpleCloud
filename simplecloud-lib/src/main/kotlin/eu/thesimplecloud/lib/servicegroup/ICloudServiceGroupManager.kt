package eu.thesimplecloud.lib.servicegroup

import eu.thesimplecloud.clientserverapi.lib.packet.connectionpromise.IConnectionPromise
import eu.thesimplecloud.lib.service.ICloudService
import eu.thesimplecloud.lib.service.ServiceType
import eu.thesimplecloud.lib.servicegroup.grouptype.ICloudLobbyGroup
import eu.thesimplecloud.lib.servicegroup.grouptype.ICloudProxyGroup
import eu.thesimplecloud.lib.servicegroup.grouptype.ICloudServerGroup
import eu.thesimplecloud.lib.servicegroup.impl.DefaultLobbyGroup
import eu.thesimplecloud.lib.servicegroup.impl.DefaultProxyGroup
import eu.thesimplecloud.lib.servicegroup.impl.DefaultServerGroup

interface ICloudServiceGroupManager {

    /**
     * Updates or adds a [ICloudServiceGroup]
     */
    fun updateGroup(cloudServiceGroup: ICloudServiceGroup)

    /**
     * Removes the [ICloudServiceGroup] found by the specified name
     */
    fun removeGroup(name: String)


    /**
     * Creates a new [ICloudServerGroup] by the specified parameters
     */
    fun createServerGroup(
            groupName: String,
            templateName: String,
            memory: Int,
            maxPlayers: Int,
            minimumOnlineServiceCount: Int,
            maximumOnlineServiceCount: Int,
            maintenance: Boolean,
            static: Boolean,
            percentToStartNewService: Int,
            wrapperName: String?,
            modulesToCopy: List<String> = emptyList(),
            hiddenAtProxyGroups: List<String> = emptyList()
    ): IConnectionPromise<ICloudServerGroup> =
            createServiceGroup(DefaultServerGroup(
                    groupName,
                    templateName,
                    memory,
                    maxPlayers,
                    minimumOnlineServiceCount,
                    maximumOnlineServiceCount,
                    maintenance,
                    static,
                    percentToStartNewService,
                    wrapperName,
                    modulesToCopy,
                    hiddenAtProxyGroups
            )) as IConnectionPromise<ICloudServerGroup>

    /**
     * Creates a new [ICloudLobbyGroup] by the specified parameters
     */
    fun createLobbyGroup(
            groupName: String,
            templateName: String,
            memory: Int,
            maxPlayers: Int,
            minimumOnlineServiceCount: Int,
            maximumOnlineServiceCount: Int,
            maintenance: Boolean,
            static: Boolean,
            percentToStartNewService: Int,
            wrapperName: String?,
            priority: Int,
            permission: String,
            modulesToCopy: List<String> = emptyList(),
            hiddenAtProxyGroups: List<String> = emptyList()
    ): IConnectionPromise<ICloudLobbyGroup> =
            createServiceGroup(DefaultLobbyGroup(
                    groupName,
                    templateName,
                    memory,
                    maxPlayers,
                    minimumOnlineServiceCount,
                    maximumOnlineServiceCount,
                    maintenance,
                    static,
                    percentToStartNewService,
                    wrapperName,
                    priority,
                    permission,
                    modulesToCopy,
                    hiddenAtProxyGroups
            )) as IConnectionPromise<ICloudLobbyGroup>

    /**
     * Creates a new [ICloudProxyGroup] by the specified parameters
     */
    fun createProxyGroup(
            groupName: String,
            templateName: String,
            memory: Int,
            maxPlayers: Int,
            minimumOnlineServiceCount: Int,
            maximumOnlineServiceCount: Int,
            maintenance: Boolean,
            static: Boolean,
            percentToStartNewService: Int,
            wrapperName: String,
            startPort: Int,
            modulesToCopy: List<String> = emptyList()
    ): IConnectionPromise<ICloudProxyGroup> =
            createServiceGroup(DefaultProxyGroup(
                    groupName,
                    templateName,
                    memory,
                    maxPlayers,
                    minimumOnlineServiceCount,
                    maximumOnlineServiceCount,
                    maintenance,
                    static,
                    percentToStartNewService,
                    wrapperName,
                    startPort,
                    modulesToCopy
            )) as IConnectionPromise<ICloudProxyGroup>

    /**
     * Creates a service group
     */
    fun createServiceGroup(cloudServiceGroup: ICloudServiceGroup): IConnectionPromise<ICloudServiceGroup>

    /**
     * Returns a list of all registered [ICloudServiceGroup]
     */
    fun getAllGroups(): List<ICloudServiceGroup>

    /**
     * Returns the [ICloudServiceGroup] found by the specified name
     */
    fun getGroup(name: String): ICloudServiceGroup? = getAllGroups().firstOrNull { it.getName().equals(name, true) }

    /**
     * Returns all registered proxy groups
     */
    fun getProxyGroups(): List<ICloudProxyGroup> = getAllGroups().filter { it.getServiceType() == ServiceType.PROXY }.map { it as ICloudProxyGroup }

    /**
     * Returns all registered lobby groups
     */
    fun getLobbyGroups(): List<ICloudLobbyGroup> = getAllGroups().filter { it.getServiceType() == ServiceType.LOBBY }.map { it as ICloudLobbyGroup }

    /**
     * Returns all registered server groups
     */
    fun getServerGroups(): List<ICloudServerGroup> = getAllGroups().filter { it.getServiceType() == ServiceType.SERVER }.map { it as ICloudServerGroup }

    /**
     * Clears the cache of all [ICloudServiceGroup]s
     */
    fun clearCache()

    /**
     * Starts a new service by the specified group
     */
    fun startNewService(cloudServiceGroup: ICloudServiceGroup): IConnectionPromise<ICloudService>

    /**
     * Deletes the specified service group from the cloud
     */
    fun deleteServiceGroup(cloudServiceGroup: ICloudServiceGroup): IConnectionPromise<Unit>

}