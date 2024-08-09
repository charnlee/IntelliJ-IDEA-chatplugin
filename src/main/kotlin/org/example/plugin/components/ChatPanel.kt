package org.example.plugin.components

import com.intellij.openapi.components.Service
import org.example.plugin.listeners.ChatKeyListener
import org.example.plugin.services.MessageSender
import javax.swing.*
import java.awt.BorderLayout

@Service(Service.Level.PROJECT)
class ChatPanel {
    private val mainPanel: JPanel = JPanel(BorderLayout())
    private val chatArea: JTextArea = JTextArea()
    private val inputField: JTextField = JTextField()
    private val sendButton: JButton = JButton("发送")

    // 实例化 MessageSender
    private val messageSender: MessageSender = MessageSender(chatArea, inputField)

    init {
        chatArea.isEditable = false
        val chatScrollPane = JScrollPane(chatArea)

        val inputPanel = JPanel(BorderLayout())
        inputPanel.add(inputField, BorderLayout.CENTER)
        inputPanel.add(sendButton, BorderLayout.EAST)

        mainPanel.add(chatScrollPane, BorderLayout.CENTER)
        mainPanel.add(inputPanel, BorderLayout.SOUTH)

        // 为发送按钮添加点击事件监听器
        sendButton.addActionListener {
            messageSender.sendMessage()
        }

        // 为输入框添加键盘事件监听器，监听Enter键
        inputField.addKeyListener(ChatKeyListener(messageSender))
    }

    val content: JComponent
        get() = mainPanel
}