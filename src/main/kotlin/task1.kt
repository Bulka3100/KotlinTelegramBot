package org.example

import java.io.File

fun main() {
    val wordsFile = File("words.txt")
    wordsFile.forEachLine { line -> println(line) }
}