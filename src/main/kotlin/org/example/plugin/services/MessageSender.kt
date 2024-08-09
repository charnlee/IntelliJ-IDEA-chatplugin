package org.example.plugin.services


import javax.swing.JTextArea
import javax.swing.JTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageSender(private val chatArea: JTextArea, private val inputField: JTextField) {

    private val streamingService = OllamaStreamingService()

    fun sendMessage() {
        val message = inputField.text.trim()
        if (message.isNotEmpty()) {
            // 先将用户消息显示在面板上
            chatArea.append("Me: $message\n")
            inputField.text = ""

            // 使用协程在后台线程中处理流式响应
            CoroutineScope(Dispatchers.IO).launch {
                streamingService.streamResponse(
                    message,
                    onTokenReceived = { token ->
                        // 每次接收到一个新 token 时，追加到聊天面板中
                        CoroutineScope(Dispatchers.Main).launch {
                            chatArea.append(token)
                        }
                    },
                    onComplete = {
                        // 响应完成后的处理逻辑，可以为空
                    },
                    onError = { error ->
                        CoroutineScope(Dispatchers.Main).launch {
                            chatArea.append("\nError: ${error.message}\n")
                        }
                    }
                )
            }
        }
    }
}


