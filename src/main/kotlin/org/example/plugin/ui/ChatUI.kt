import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import java.awt.*
import javax.swing.*

class ChatUI {

    val chatContainer: JBPanel<*> = JBPanel<JBPanel<*>>().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        background = Color(250, 250, 250)
    }

    val chatScrollPane: JBScrollPane = JBScrollPane(chatContainer).apply {
        verticalScrollBar.unitIncrement = 16
        horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    }

    val inputField: JBTextField = JBTextField().apply {
        font = Font("Segoe UI", Font.PLAIN, 14)
        border = BorderFactory.createEmptyBorder(5, 10, 5, 10)
        background = Color(255, 255, 255)
        foreground = Color(60, 60, 60)
    }

    val sendButton: JButton = JButton("Send").apply {
        background = Color(52, 152, 219)
        foreground = Color.WHITE
        font = Font("Segoe UI", Font.BOLD, 14)
        isFocusPainted = false
        border = BorderFactory.createEmptyBorder(5, 15, 5, 15)
        isContentAreaFilled = true
        isOpaque = true
        isBorderPainted = false
    }

    val inputPanel: JBPanel<JBPanel<*>> = JBPanel<JBPanel<*>>(BorderLayout()).apply {
        background = Color(240, 240, 240)
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        add(inputField, BorderLayout.CENTER)
        add(sendButton, BorderLayout.EAST)
    }

}
