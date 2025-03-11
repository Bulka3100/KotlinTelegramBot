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
                                "callback_data":"$LEARN_WORDS_CLICKED"
                            },
                             {
                                "text":"статистика",
                                "callback_data":"$STATISTICS_CLICKED"
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

    fun sendQuestion(chatId: Long?, question: Question?) {
        val urlSendMessage = "${org.example.BASE_API}$tokenBot/sendMessage"
        if (question != null) {
            val sendQuestionBody = """
            {
                "chat_id": $chatId,
                "text": "${question?.correctAnswer?.origin}",
                "reply_markup":{
                    "inline_keyboard":[
                        [
                            {
                                "text":"${question.variants.get(0).translate}",
                                "callback_data":"${CALLBACK_DATA_ANSWER_PREFIX}0"
                            },
                             {
                                "text":"${question.variants.get(1).translate}",
                                "callback_data":"${CALLBACK_DATA_ANSWER_PREFIX}1"
                            }
                        ],
                        [
                            {
                                "text":"${question.variants.get(2).translate}",
                                "callback_data":"${CALLBACK_DATA_ANSWER_PREFIX}2"
                            },
                             {
                                "text":"${question.variants.get(3).translate}",
                                "callback_data":"${CALLBACK_DATA_ANSWER_PREFIX}3"
                            }
                        ]
                    ]
                }

            }
        """.trimIndent()
            val request: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlSendMessage))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(sendQuestionBody))
                .build()
            client.send(request, HttpResponse.BodyHandlers.ofString())

        } else {
            sendMessage(chatId, "Все слова выучены!")
        }

    }

    fun checkNextQuestionAndSend(
        trainer: LearnWordsTrainer,
        telegramBotService: TelegramBotService,
        chatId: Long?
    ) {
        val question = trainer.getNextQuestion()
        if (question == null) {
            telegramBotService.sendMessage(chatId, "Все слова в словаре выучены!")
        } else {
            telegramBotService.sendQuestion(chatId, question)
        }
    }

}

const val BASE_API = "https://api.telegram.org/bot"
const val CALLBACK_DATA_ANSWER_PREFIX = "_answer"
