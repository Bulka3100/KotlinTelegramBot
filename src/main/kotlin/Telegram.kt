package org.example

import jdk.internal.net.http.HttpRequestImpl
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val botToken = args[0]
    val urlGetMe = "https://api.telegram.org/bot$botToken/getMe"
    var updateId = 0
    val client: HttpClient = HttpClient.newBuilder().build()
    val requestGet: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetMe)).build()
    val responseGet: HttpResponse<String> = client.send(requestGet, HttpResponse.BodyHandlers.ofString())
    println(responseGet.body())

    while (true) {
        Thread.sleep(2000)

        val updates = getUpdates(botToken, updateId)
        println(updates)
        val idRegex : Regex = "\"id\":\"(.+?) \"".toRegex()
        val matchResultId = idRegex.find(updates)
        val groupsId = matchResultId?.groups
        val chatId = groupsId?.get(1)?.value!!.toInt()
        if (chatId != null) {
            updateId = chatId + 1
            println("Chat ID: $chatId")
        } else {
            println("Chat ID not found.")
        }

        val messageTextRegex: Regex = "\"text\":\"(.+?)\"".toRegex()
        val matchResult = messageTextRegex.find(updates)
        val groups = matchResult?.groups
        val text = groups?.get(1)?.value

        println(text)
    }
}

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates?offset=$updateId"
    val client: HttpClient = HttpClient.newHttpClient()
    val httpRequest: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response: HttpResponse<String> = client.send(httpRequest, HttpResponse.BodyHandlers.ofString())
    return response.body()
}