package org.example

import java.io.File
import java.util.Dictionary
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
            "1" -> while (loadDictionary().filter { it.correctAnswerCount < MIN_WORDS }.isNotEmpty()) learnWords()
            "2" -> println(getStatistic())
            "0" -> break
            else -> println("введите пункт меню")
        }
    }
}

fun learnWords() {
    val dictionary= loadDictionary()
    val notLearnedList = loadDictionary().filter { it.correctAnswerCount < MIN_WORDS }
    val notLearnedFirstFour = notLearnedList.take(4)
    var questionWords = notLearnedFirstFour.shuffled()
    if (notLearnedList.isNotEmpty()) {
        for (i in questionWords) {
            questionWords = questionWords.shuffled()
            val correctAnswer = questionWords.indexOf(i) + 1
            println("${i.origin}:")
            questionWords.mapIndexed { index, word -> println("${index + 1} ${word.translate}") }
            println("--------")
            println("0 - Меню")
            val userAnswerInput = readln().toInt()
            when (userAnswerInput) {
                0 -> return
                correctAnswer -> {
                    println("правильно")
                    i.correctAnswerCount++
                    saveDictionary(dictionary)
                }

                else -> println("неправильно! ${i.origin} это ${i.translate}")
            }

        }
    } else println("все слова выучены")

}

data class Word(
    var correctAnswerCount: Int = 0,
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
fun saveDictionary(dictionary: List<Word>) {
val wordsFile = File("words.txt")
wordsFile.writeText(dictionary.joinToString("\n") {word-> "${word.origin}|${word.translate}|${word.correctAnswerCount}"})
}

const val MIN_WORDS = 3
const val PERCENT = 100

