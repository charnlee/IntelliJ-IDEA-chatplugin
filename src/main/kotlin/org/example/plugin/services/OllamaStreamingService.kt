package org.example.plugin.services


import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.StreamingResponseHandler
import dev.langchain4j.model.chat.StreamingChatLanguageModel
import dev.langchain4j.model.ollama.OllamaStreamingChatModel
import dev.langchain4j.model.output.Response

class OllamaStreamingService {

    private val model: StreamingChatLanguageModel

    init {
        model = OllamaStreamingChatModel.builder()
            .baseUrl("http://localhost:11434") // 本地运行的 Ollama 实例
            .modelName("codeqwen:latest") // 模型名称
            .temperature(0.0)
            .build()
    }

    fun streamResponse(message: String, onTokenReceived: (String) -> Unit, onComplete: (Response<AiMessage>) -> Unit, onError: (Throwable) -> Unit) {
        model.generate(message, object : StreamingResponseHandler<AiMessage> {
            override fun onNext(token: String) {
                // 每次接收到一个新 token 就调用此回调
                onTokenReceived(token)
            }

            override fun onComplete(response: Response<AiMessage>) {
                // 响应完成后调用
                onComplete(response)
            }

            override fun onError(error: Throwable) {
                // 处理错误
                onError(error)
            }
        })
    }
}
