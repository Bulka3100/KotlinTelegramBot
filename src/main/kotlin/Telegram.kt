package org.example

import jdk.internal.net.http.HttpRequestImpl
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val tgBot = TelegramBotService(args[0])
    val botToken = args[0]
    val urlGetMe = "https://api.telegram.org/bot$botToken/getMe"
    var updateId = 0
    val client: HttpClient = HttpClient.newBuilder().build()
    val requestGet: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetMe)).build()
    val responseGet: HttpResponse<String> = client.send(requestGet, HttpResponse.BodyHandlers.ofString())
    println(responseGet.body())

    val chatIdRegex = "\"chat\":\\{\"id\":(\\d+)".toRegex()
    val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
    val idRegex = "\"update_id\":(\\d+)".toRegex()
    val dataRegex: Regex = "\"data\":\"(.+?)\"".toRegex()

    val trainer = LearnWordsTrainer()
    while (true) {
        Thread.sleep(2000)

        val updates = tgBot.getUpdates(updateId)
        println(updates)

        val matchResultId = idRegex.find(updates)
        val updateIdValue = matchResultId?.groups[1]?.value?.toIntOrNull()?.plus(1) ?: continue
        println("Update ID: $updateIdValue")

        updateId = updateIdValue


        val matchResult = messageTextRegex.find(updates)
        val groups = matchResult?.groups
        val text = groups?.get(1)?.value


        println(text)

        val matchResultChatId = chatIdRegex.find(updates)
        val chatId = matchResultChatId?.groups[1]?.value?.toLongOrNull()
        println(chatId)
        if (text == "/start")
            tgBot.sendMenu(chatId)
        val data = dataRegex.find(updates)?.groups?.get(1)?.value
        if (data == STATISTICS_CLICKED) {
            tgBot.sendMessage(chatId, trainer.getStatistic())
        }
        if (data == LEARN_WORDS_CLICKED) {

        }
    }

}
const val STATISTICS_CLICKED = "Statistics"
const val LEARN_WORDS_CLICKED = "Learn_words"

