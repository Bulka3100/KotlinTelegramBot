package org.example

import java.io.File

fun main() {
    val wordsFile = File("words.txt")
    val dictionary = mutableListOf<Word>()
    wordsFile.forEachLine { line ->
        val line = line.split("|")
        val word = Word(origin = line[0], translate = line[1], correctAnswerCount = line.getOrNull(2)?.toIntOrNull() ?: 0)
        dictionary.add(word)
    }
    println(dictionary)
}

data class Word(
    val correctAnswerCount: Int = 0,
    val origin: String,
    val translate: String,
)