package eu.thesimplecloud.base.manager.screens

import eu.thesimplecloud.lib.screen.ICommandExecutable

class ScreenImpl(private val commandExecutable: ICommandExecutable) : IScreen {

    private val messages = ArrayList<String>()

    override fun getCommandExecutable(): ICommandExecutable = this.commandExecutable

    override fun addMessage(message: String) {
        this.messages.add(message)
        if (this.messages.size > 200) {
            this.messages.removeAt(0)
        }
    }

    override fun getAllSavedMessages(): List<String> = this.messages

}