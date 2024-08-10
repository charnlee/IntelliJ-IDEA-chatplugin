import com.intellij.ui.components.JBPanel
import org.example.plugin.listeners.ChatKeyListener
import org.example.plugin.services.MessageSender
import java.awt.*
import javax.swing.*

class ChatPanel : JBPanel<ChatPanel>(BorderLayout()) {

    private val chatUI = ChatUI()

    private lateinit var messageSender: MessageSender

    init {
        add(chatUI.chatScrollPane, BorderLayout.CENTER)
        add(chatUI.inputPanel, BorderLayout.SOUTH)

        messageSender = MessageSender(this, chatUI.inputField)

        chatUI.sendButton.addActionListener {
            messageSender.sendMessage()
        }

        chatUI.inputField.addKeyListener(ChatKeyListener(messageSender))
    }

    fun addMessageBubble(text: String, isUser: Boolean) {
        val messagePanel = JBPanel<JBPanel<*>>().apply {
            layout = FlowLayout(if (isUser) FlowLayout.RIGHT else FlowLayout.LEFT).apply {
                hgap = 0
                vgap = 0
            }
            background = Color(250, 250, 250)
        }

        val messageArea = JTextArea(text).apply {
            isEditable = false
            lineWrap = true
            wrapStyleWord = true
            font = Font("Segoe UI", Font.PLAIN, 14)
            background = if (isUser) Color(230, 230, 230) else Color(52, 152, 219)
            foreground = if (isUser) Color(60, 60, 60) else Color.WHITE
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
//            preferredSize = Dimension(chatUI.chatScrollPane.width - 20, 0) // Adjust based on padding
//            maximumSize = Dimension(400, Int.MAX_VALUE)
        }
        // 设置最大宽度约束
        messageArea.maximumSize = Dimension(chatUI.chatScrollPane.width - 20, Int.MAX_VALUE)

        if (isUser) {
            messagePanel.add(Box.createHorizontalGlue())
        }
        messagePanel.add(messageArea)

        chatUI.chatContainer.add(Box.createVerticalStrut(10))
        chatUI.chatContainer.add(messagePanel)

        chatUI.chatContainer.revalidate()
        chatUI.chatContainer.repaint()

        SwingUtilities.invokeLater {
            chatUI.chatScrollPane.verticalScrollBar.value = chatUI.chatScrollPane.verticalScrollBar.maximum
        }
//        // 更新 JTextArea 的宽度以匹配 chatScrollPane 的宽度
        SwingUtilities.invokeLater {
            messageArea.preferredSize = Dimension(chatUI.chatScrollPane.width - 20, messageArea.preferredSize.height)
            messageArea.revalidate()
        }
    }
}

