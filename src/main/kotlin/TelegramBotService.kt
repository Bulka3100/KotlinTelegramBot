package org.example

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

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

    fun sendMessage(chatId: Long?, text: String) {
        val urlSendMessage = "$BASE_API$tokenBot/sendMessage?chat_id=$chatId&text=$text"
        val httpRequest: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage)).build()
        client.send(httpRequest, HttpResponse.BodyHandlers.ofString())
    }

}

const val BASE_API = "https://api.telegram.org/bot"