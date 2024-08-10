package org.example.plugin.services


import com.intellij.ui.components.JBTextField
import ChatPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.swing.JPanel
import javax.swing.SwingUtilities

class MessageSender(private val chatPanel: ChatPanel, private val inputField: JBTextField) {

    private val streamingService = OllamaStreamingService()

    fun sendMessage() {
        val message = inputField.text.trim()
        if (message.isNotEmpty()) {
            // 立即将用户消息显示在面板上
            chatPanel.addMessageBubble(message, true)
            inputField.text = ""

            // 使用协程在后台线程中处理流式响应
            CoroutineScope(Dispatchers.IO).launch {
                val responseBuilder = StringBuilder()

                streamingService.streamResponse(
                    message,
                    onTokenReceived = { token ->
                        responseBuilder.append(token)
                    },
                    onComplete = {
                        // 完成后在主线程上添加模型的消息气泡
                        SwingUtilities.invokeLater {
                            chatPanel.addMessageBubble(responseBuilder.toString(), false)
                        }
                    },
                    onError = { error ->
                        SwingUtilities.invokeLater {
                            chatPanel.addMessageBubble("Error: ${error.message}", false)
                        }
                    }
                )
            }
        }
    }
}






