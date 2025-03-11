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
        val data = dataRegex.find(updates)?.groups?.get(1)?.value
        if (chatId != null) {
            when {
                text == "/start" -> {
                    tgBot.sendMenu(chatId)
                }

                data == STATISTICS_CLICKED -> {
                    tgBot.sendMessage(chatId, trainer.getStatistic())
                }

                data == LEARN_WORDS_CLICKED -> {
                    val newQuestion = trainer.getNextQuestion()
                    tgBot.sendQuestion(chatId, newQuestion)
                }

                data != null && data.startsWith(CALLBACK_DATA_ANSWER_PREFIX) -> {
                    val answerIndex = data.substringAfter(CALLBACK_DATA_ANSWER_PREFIX).toInt()
                    println(answerIndex)
                    val isRight = trainer.checkAnswer(answerIndex)
                    if (isRight) {
                        tgBot.sendMessage(chatId, "Верно!")
                        tgBot.checkNextQuestionAndSend(trainer, tgBot, chatId)
                    } else {
                        tgBot.sendMessage(chatId, "неверно!")
                    }
                }
            }


        }
    }
}


val trainer = LearnWordsTrainer()
const val STATISTICS_CLICKED = "Statistics"
const val LEARN_WORDS_CLICKED = "Learn_words"

