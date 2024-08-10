package org.example.plugin.services


import javax.swing.JTextField
import org.example.plugin.components.ChatPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.swing.JPanel

class MessageSender(private val chatPanel: ChatPanel, private val inputField: JTextField) {

    private val streamingService = OllamaStreamingService()

    fun sendMessage() {
        val message = inputField.text.trim()
        if (message.isNotEmpty()) {
            // 立即将用户消息显示在面板上
            chatPanel.addMessageBubble("User: $message\n", true)
            inputField.text = ""

            // 添加一个新的空气泡用于显示模型返回的信息
            chatPanel.addMessageBubble("", false)

            // 准备一个 StringBuilder 来累积模型的返回内容
            val responseBuilder = StringBuilder()

            // 使用协程在后台线程中处理流式响应
            CoroutineScope(Dispatchers.IO).launch {
                streamingService.streamResponse(
                    message,
                    onTokenReceived = { token ->
                        // 每次接收到一个新 token 时，追加到 responseBuilder 中
                        responseBuilder.append(token)

                        // 在主线程中更新最后一个消息气泡的内容
                        CoroutineScope(Dispatchers.Main).launch {
                            chatPanel.updateLastMessageBubble(responseBuilder.toString(), false)
                        }
                    },
                    onComplete = {
                        // 响应完成后的处理逻辑
                    },
                    onError = { error ->
                        CoroutineScope(Dispatchers.Main).launch {
                            chatPanel.addMessageBubble("\nError: ${error.message}\n", false)
                        }
                    }
                )
            }
        }
    }
}






