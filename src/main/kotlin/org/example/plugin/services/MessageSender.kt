package org.example.plugin.services


import com.intellij.ui.components.JBTextField
import ChatPanel
import com.intellij.ui.components.JBPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                var responsePanel: JBPanel<JBPanel<*>>? = null
                val responseBuilder = StringBuilder()

                streamingService.streamResponse(
                    message,
                    onTokenReceived = { token ->
                        SwingUtilities.invokeLater {
                            if (responsePanel == null) {
                                // 第一次收到token时创建消息气泡
                                responsePanel = chatPanel.addMessageBubble("", false)
                            }
                            // 将新的token追加到已有的消息气泡中
                            responseBuilder.append(token)
                            chatPanel.updateMessageBubble(responsePanel!!, responseBuilder.toString())
                        }
                    },
                    onComplete = {
                        // 完成后，可能需要在主线程上执行一些清理操作
                        SwingUtilities.invokeLater {
                            // 可以在此处执行任何完成时的操作（如日志记录或通知）
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








