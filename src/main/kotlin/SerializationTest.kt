package org.example

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.String

fun main() {
    val json = Json {
        ignoreUnknownKeys = true
    }

    @Serializable
    data class Message(
        @SerialName("text")
        val test: String,
    )

    @Serializable
    data class CallbackQuery(
        @SerialName("data")
        val data: String,
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


    val responseString = """
        {
            "ok": true,
            "result": [
                {
                    "update_id": 231331681,
                    "message": {
                        "message_id": 266,
                        "from": {
                            "id": 659156823,
                            "is_bot": false,
                            "first_name": "\u041a\u0438\u0440\u0438\u043b\u043b",
                            "username": "KirillVip31",
                            "language_code": "ru"
                        },
                        "chat": {
                            "id": 659156823,
                            "first_name": "\u041a\u0438\u0440\u0438\u043b\u043b",
                            "username": "KirillVip31",
                            "type": "private"
                        },
                        "date": 1744767259,
                        "text": "/start",
                        "entities": [
                            {
                                "offset": 0,
                                "length": 6,
                                "type": "bot_command"
                            }
                        ]
                    }
                }
            ]
        }
        
        
    """.trimIndent()


//    val word = Json.encodeToString(
//        Word(
//            correctAnswerCount = 0,
//            origin = "Hello",
//            translate = "Привет "
//        )
//    )
//    println(word)
//    val wordObject = Json.decodeFromString<Word>(
//        """ {"origin":"Hello","translate":"Привет "}"""
//    )
//    println(wordObject)
    val response = json.decodeFromString<Response>(responseString)
    println(response)
}