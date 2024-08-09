package org.example.plugin.toolWindow

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import org.example.plugin.components.ChatPanel

class ChatToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val chatPanel = project.service<ChatPanel>()
        val contentFactory = ContentFactory.getInstance()  // 使用 getInstance() 方法
        val content = contentFactory.createContent(chatPanel.content, "", false)
        toolWindow.contentManager.addContent(content)
    }
}