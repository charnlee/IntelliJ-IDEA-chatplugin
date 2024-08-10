package org.example.plugin.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import org.example.plugin.components.ChatPanel
import org.example.plugin.services.MessageSender


class ChatToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val chatPanel = ChatPanel()
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(chatPanel.content, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
