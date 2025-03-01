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
        val updates: String = getUpdates(botToken, updateId)
        println(updates)
        val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates"
        val startUpdateId = updates.lastIndexOf("update_id") + 11
        val endUpdateId = updates.lastIndexOf(",\n\"message\"")
        if(startUpdateId ==-1 || endUpdateId ==-1) continue
        val updateIdString = updates.substring(startUpdateId + 11, endUpdateId)
      //  println(updateIdString)
        updateId = updateIdString.toInt() + 1
    }
}

fun getUpdates(botToken: String, updateId: Int): String {
    val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates?offset=$updateId"

    val client: HttpClient = HttpClient.newBuilder().build()

    val requestUpdate: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()
    val responseUpdate: HttpResponse<String> = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString())
    return responseUpdate.body()
}
