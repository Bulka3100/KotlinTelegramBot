package org.example

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Serializable
data class Message(
    @SerialName("text")
    val text: String,
    @SerialName("chat")
    val chat: Chat,
)

@Serializable
data class CallbackQuery(
    @SerialName("data")
    val data: String? = null,
    @SerialName("message")
    val message: Message? = null,
)

@Serializable
data class Chat(
    @SerialName("id")
    val id: Long,
)

@Serializable
data class Update(
    @SerialName("update_id")
    val updateId: Long,
    @SerialName("message")
    val message: Message? = null,
    @SerialName("callback_query")
    val callbackQuery: CallbackQuery? = null,
)

@Serializable
data class Response(
    @SerialName("result")
    val result: List<Update>,
)

fun main(args: Array<String>) {
    val trainer = LearnWordsTrainer()
    val tgBot = TelegramBotService(args[0])
    val botToken = args[0]
    val urlGetMe = "https://api.telegram.org/bot$botToken/getMe"
    var updateId = 0 // Глобальная переменная updateId
    var lastUpdateId = 0L
    val client: HttpClient = HttpClient.newBuilder().build()
    val requestGet: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetMe)).build()
    val responseGet: HttpResponse<String> = client.send(requestGet, HttpResponse.BodyHandlers.ofString())
    println(responseGet.body())
    val json = Json {
        ignoreUnknownKeys = true
    }

    while (true) {
        Thread.sleep(2000)

        val responseString = tgBot.getUpdates(updateId)
        println(responseString)
        val response: Response = json.decodeFromString(responseString)
        val updates = response.result

        val firstUpdate = updates.firstOrNull() ?: continue
        updateId = firstUpdate.updateId.toInt() + 1 // Обновляем глобальный updateId

        val message = firstUpdate.message?.text
        val chatId = firstUpdate.message?.chat?.id ?: firstUpdate.callbackQuery?.message?.chat?.id
        println(chatId)
        val data = firstUpdate.callbackQuery?.data

        when {
            message == "/start" -> {
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
                    tgBot.sendMessage(chatId, "Неверно!")
                    tgBot.checkNextQuestionAndSend(trainer, tgBot, chatId)
                }
            }
        }
    }
}

const val STATISTICS_CLICKED = "Statistics"
const val LEARN_WORDS_CLICKED = "Learn_words"
