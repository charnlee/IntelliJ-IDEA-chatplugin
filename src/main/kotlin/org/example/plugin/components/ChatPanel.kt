import com.intellij.ui.components.JBPanel
import net.miginfocom.swing.MigLayout
import org.example.plugin.listeners.ChatKeyListener
import org.example.plugin.services.MessageSender
import java.awt.*
import javax.swing.*

class ChatPanel : JBPanel<ChatPanel>(MigLayout("fill, wrap 1", "[grow, fill]", "[grow, fill]0[]")) {

    private val chatUI = ChatUI()
    private lateinit var messageSender: MessageSender

    init {
        setupUI()
        setupEventListeners()
    }

    private fun setupUI() {
        add(chatUI.chatScrollPane, "grow, push")
        add(chatUI.inputPanel, "dock south")

        messageSender = MessageSender(this, chatUI.inputField)
    }

    private fun setupEventListeners() {
        chatUI.sendButton.addActionListener { messageSender.sendMessage() }
        chatUI.inputField.addKeyListener(ChatKeyListener(messageSender))
    }

    fun addMessageBubble(text: String, isUser: Boolean): JBPanel<JBPanel<*>> {
        val messagePanel = createMessagePanel(isUser)
        val messageArea = createMessageArea(text, isUser)

        messagePanel.add(messageArea, "growx")
        chatUI.chatContainer.add(messagePanel, "wrap, growx, pushx")

        refreshChatUI(messageArea)
        scrollToBottom()

        return messagePanel
    }

    private fun scrollToBottom() {
        SwingUtilities.invokeLater {
            val verticalScrollBar = chatUI.chatScrollPane.verticalScrollBar
            verticalScrollBar.value = verticalScrollBar.maximum
        }
    }

    private fun createMessagePanel(isUser: Boolean): JBPanel<JBPanel<*>> {
        return JBPanel<JBPanel<*>>().apply {
            layout = MigLayout("fill, insets 0", if (isUser) "[right]" else "[left]", "[]")
            background = Color(250, 250, 250)
        }
    }

    private fun createMessageArea(text: String, isUser: Boolean): JTextArea {
        return JTextArea(text).apply {
            isEditable = false
            lineWrap = true
            wrapStyleWord = true
            font = Font("Microsoft YaHei", Font.PLAIN, 14)
            background = if (isUser) Color(230, 230, 230) else Color(52, 152, 219)
            foreground = if (isUser) Color(60, 60, 60) else Color.WHITE
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            maximumSize = Dimension(chatUI.chatScrollPane.width - 20, Int.MAX_VALUE)
        }
    }

    fun updateMessageBubble(messagePanel: JBPanel<JBPanel<*>>, newText: String) {
        // 假设messagePanel的第一个组件是JTextArea
        val messageArea = messagePanel.getComponent(0) as JTextArea
        messageArea.text = newText
        messageArea.revalidate()
        messageArea.repaint()
    }

    private fun refreshChatUI(messageArea: JTextArea) {
        messageArea.preferredSize = Dimension(chatUI.chatScrollPane.width - 20, messageArea.preferredSize.height)
        chatUI.chatContainer.revalidate()
        chatUI.chatContainer.repaint()

        SwingUtilities.invokeLater {
            messageArea.preferredSize = Dimension(chatUI.chatScrollPane.width - 20, messageArea.preferredSize.height)
            messageArea.revalidate()
        }
    }
}


