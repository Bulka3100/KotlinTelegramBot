package org.example

import java.io.File
data class Question(
    var variants: List<Word>,
)
data class Word(
    var correctAnswerCount: Int = 0,
    val origin: String,
    val translate: String
)

class LearnWordsTrainer {

    val dictionary = loadDictionary()

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

    fun saveDictionary(dictionary: List<Word>) {
        val wordsFile = File("words.txt")
        wordsFile.writeText(dictionary.joinToString("\n") { word -> "${word.origin}|${word.translate}|${word.correctAnswerCount}" })
    }

    fun getStatistic(): String {
        val learned = dictionary.filter { it.correctAnswerCount >= MIN_WORDS }.size
        val allWords = dictionary.size
        val percent = learned.toDouble() / allWords.toDouble() * PERCENT
        return "выучено $learned из $allWords |${percent.toInt()}%"

    }


    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswerCount < MIN_WORDS }
        if(notLearnedList.isEmpty()) return null
        val notLearnedFirstFour = notLearnedList.take(4)
        var questionWords = notLearnedFirstFour.shuffled()

        return Question(
            variants = questionWords,
        )
    }
}