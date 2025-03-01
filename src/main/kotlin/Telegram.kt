package org.example

import jdk.internal.net.http.HttpRequestImpl
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun main(args: Array<String>) {
    val botToken = args[0]
    val urlGetMe = "https://api.telegram.org/bot$botToken/getMe"
    val urlGetUpdates = "https://api.telegram.org/bot$botToken/getUpdates"

    val client: HttpClient = HttpClient.newBuilder().build()

    val requestGet: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetMe)).build()
    val requestUpdate: HttpRequest = HttpRequest.newBuilder().uri(URI.create(urlGetUpdates)).build()


    val responseGet: HttpResponse<String> =client.send(requestGet, HttpResponse.BodyHandlers.ofString())
    val responseUpdate: HttpResponse<String> =client.send(requestUpdate, HttpResponse.BodyHandlers.ofString())

    println(responseGet.body())
    println(responseUpdate.body())
}