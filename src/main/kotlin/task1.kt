package org.example

import java.io.File
import kotlin.collections.mutableListOf

fun main() {

    while (true) {
        println(
            """Меню:
        |1-Учить слова
        |2-Статистика
        |0-Выход
    """.trimMargin()
        )

        val choice = readln()
        when (choice) {
            "1" -> learnWords()
            "2" -> println(getStatistic())
            "0" -> break
            else -> println("введите пункт меню")
        }
    }
}

fun learnWords() {
    val notLearnedList = loadDictionary().filter { it.correctAnswerCount < 3 }
    var questionWords = notLearnedList.take(4).shuffled()
    if (notLearnedList != emptyList<Word>()) {
        for (i in questionWords) {
            questionWords = questionWords.shuffled()
            val correctAnswer = i.translate
            println("${i.origin}:")
            questionWords.mapIndexed { index, word -> println("${index + 1} ${word.translate}") }
            readln()
        }
    } else println("все слова выучены")

}

data class Word(
    val correctAnswerCount: Int = 0,
    val origin: String,
    val translate: String,
)

fun loadDictionary(): List<Word> {
    val dictionary = mutableListOf<Word>()
    val wordsFile = File("words.txt")
    wordsFile.forEachLine { line ->
        val line = line.split("|")
        val word =
            Word(origin = line[0], translate = line[1], correctAnswerCount = line.getOrNull(2)?.toIntOrNull() ?: 0)
        dictionary.add(word)

    }
    return dictionary.toList()
}

fun getStatistic(): String {
    val learned = loadDictionary().filter { it.correctAnswerCount >= MIN_WORDS }.size
    val allWords = loadDictionary().size
    val percent = learned.toDouble() / allWords.toDouble() * PERCENT
    return "выучено $learned из $allWords |${percent.toInt()}%"
}

const val MIN_WORDS = 3
const val PERCENT = 100

