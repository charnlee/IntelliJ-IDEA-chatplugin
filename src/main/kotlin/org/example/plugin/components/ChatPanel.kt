package org.example.plugin.components

import org.example.plugin.services.MessageSender
import org.example.plugin.listeners.ChatKeyListener
import javax.swing.*
import java.awt.*

import javax.swing.*
import java.awt.*
import java.awt.image.BufferedImage

import javax.swing.*
import java.awt.*

class ChatPanel : JPanel() {

    private val mainPanel: JPanel = JPanel(BorderLayout())
    private val chatContainer: JPanel = JPanel()
    private val chatScrollPane: JScrollPane = JScrollPane(chatContainer)
    private val inputField: JTextField = JTextField()
    private val sendButton: JButton = JButton("发送")
    private lateinit var messageSender: MessageSender

    init {
        chatContainer.layout = BoxLayout(chatContainer, BoxLayout.Y_AXIS)
        chatContainer.background = Color(245, 245, 245) // 更加柔和的背景颜色

        chatScrollPane.verticalScrollBar.unitIncrement = 16
        chatScrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER

        val inputPanel = JPanel(BorderLayout())
        inputPanel.background = Color(230, 230, 230) // 输入面板的背景颜色
        inputField.font = Font("Arial", Font.PLAIN, 14) // 更改字体
        inputPanel.add(inputField, BorderLayout.CENTER)
        sendButton.background = Color(30, 144, 255) // 按钮的背景颜色
        sendButton.foreground = Color.WHITE // 按钮文字的颜色
        inputPanel.add(sendButton, BorderLayout.EAST)

        mainPanel.add(chatScrollPane, BorderLayout.CENTER)
        mainPanel.add(inputPanel, BorderLayout.SOUTH)

        // 初始化 MessageSender
        messageSender = MessageSender(this, inputField)

        // 为发送按钮添加点击事件监听器
        sendButton.addActionListener {
            messageSender.sendMessage()
        }

        // 为输入框添加键盘事件监听器，监听Enter键
        inputField.addKeyListener(ChatKeyListener(messageSender))
    }

    fun addMessageBubble(text: String, isUser: Boolean) {
        val messagePanel = JPanel()
        messagePanel.layout = BoxLayout(messagePanel, BoxLayout.X_AXIS)
        messagePanel.background = if (isUser) Color.LIGHT_GRAY else Color.CYAN
        messagePanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        // 使用 HTML 来自动处理换行
        val messageLabel = JLabel("<html>${text.replace("\n", "<br>")}</html>")
        messageLabel.foreground = Color.BLACK
        messageLabel.isOpaque = true
        messageLabel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        // 限制消息气泡的最大宽度
        val maxWidth = 300 // 设置气泡的最大宽度，单位为像素
        messageLabel.maximumSize = Dimension(maxWidth, Int.MAX_VALUE)
        messageLabel.preferredSize = Dimension(maxWidth, messageLabel.preferredSize.height)
        messageLabel.size = Dimension(maxWidth, messageLabel.preferredSize.height)

        if (isUser) {
            messagePanel.add(Box.createHorizontalGlue())
        }
        messagePanel.add(messageLabel)

        chatContainer.add(messagePanel)
        chatContainer.add(Box.createVerticalStrut(10))

        SwingUtilities.invokeLater {
            chatScrollPane.verticalScrollBar.value = chatScrollPane.verticalScrollBar.maximum
        }
    }


    fun updateLastMessageBubble(text: String, isUser: Boolean) {
        // 获取最后一个消息气泡面板（JPanel）
        val lastComponent = chatContainer.getComponent(chatContainer.componentCount - 2)
        if (lastComponent is JPanel) {
            // 获取消息气泡面板中的第一个组件（假设是 JLabel）
            val messagePanel = lastComponent.getComponent(0) as? JPanel
            if (messagePanel != null) {
                val messageLabel = messagePanel.getComponent(0) as? JLabel
                if (messageLabel != null) {
                    messageLabel.text = text

                    SwingUtilities.invokeLater {
                        chatScrollPane.verticalScrollBar.value = chatScrollPane.verticalScrollBar.maximum
                    }
                }
            }
        }
    }


    // 创建圆形头像图标的方法
    private fun createAvatarIcon(color: Color, size: Int): Icon {
        val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        val g = image.createGraphics()
        g.color = color
        g.fillOval(0, 0, size, size)
        g.dispose()
        return ImageIcon(image)
    }

    val content: JComponent
        get() = mainPanel
}




