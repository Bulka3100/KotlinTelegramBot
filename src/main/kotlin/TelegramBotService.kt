package org.example

import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

class TelegramBotService(
    val tokenBot: String
) {
    val client: HttpClient = HttpClient.newHttpClient()

    fun getUpdates(updateId: Int): String {
        val urlGetUpdates = "$BASE_API$tokenBot/getUpdates?offset=$updateId"
        val httpRequest: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
        val response: HttpResponse<String> = client.send(httpRequest, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }

    fun sendMenu(chatId: Long?) {
        val urlSendMessage = "$BASE_API$tokenBot/sendMessage"
        val sendMenuBody = """
            {
                "chat_id": $chatId,
                "text": "оcновное меню",
                "reply_markup":{
                    "inline_keyboard":[
                        [
                            {
                                "text":"изучить слова",
                                "callback_data":"Learn_words"
                            },
                             {
                                "text":"статистика",
                                "callback_data":"statistics"
                            }
                        ]
                    ]
                }

            }
        """.trimIndent()
        val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage))
            .header("Content-type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(sendMenuBody))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun sendMessage(chatId: Long?, text: String) {
        val encoded = URLEncoder.encode(
            text,
            StandardCharsets.UTF_8
        )
        println(encoded)
        val urlSendMessage = "$BASE_API$tokenBot/sendMessage?chat_id=$chatId&text=$encoded"
        val httpRequest: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
        client.send(httpRequest, HttpResponse.BodyHandlers.ofString())
    }
}

const val BASE_API = "https://api.telegram.org/bot"