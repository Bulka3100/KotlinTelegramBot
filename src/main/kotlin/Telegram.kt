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

    val startUpdateId = updates.lastIndexOf("update_id")
    val endUpdateId = updates.lastIndexOf(",\n\"message\"")

    if (startUpdateId == -1 || endUpdateId == -1) continue
    println(updates.substring(startUpdateId, endUpdateId))

    val updateIdString = updates.substring(startUpdateId + 11, endUpdateId)
    updateId = updateIdString.toInt() + 1
}
}

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates?offset=$updateId"
    val client: HttpClient = HttpClient.newHttpClient()
    val httpRequest: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val response: HttpResponse<String> = client.send(httpRequest, HttpResponse.BodyHandlers.ofString())
    return response.body()
}