package org.example

import java.io.File

data class Question(
    var variants: List<Word>,
    val correctAnswer: Word,
)

data class Word(
    var correctAnswerCount: Int = 0,
    val origin: String,
    val translate: String
)

class LearnWordsTrainer(private val learnedAnswerCount: Int = 3, val countOfQuestion: Int = 4) {
    private var question: Question? = null
    val dictionary = loadDictionary()

    fun loadDictionary(): List<Word> {
        try {
            val dictionary = mutableListOf<Word>()
            val wordsFile = File("words.txt")
            wordsFile.forEachLine { line ->
                val line = line.split("|")
                val word =
                    Word(
                        origin = line[0],
                        translate = line[1],
                        correctAnswerCount = line.getOrNull(2)?.toIntOrNull() ?: 0
                    )
                dictionary.add(word)

            }
            return dictionary.toList()
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException("некорректный файл")
        }

    }

    fun saveDictionary(dictionary: List<Word>) {
        val wordsFile = File("words.txt")
        wordsFile.writeText(dictionary.joinToString("\n") { word -> "${word.origin}|${word.translate}|${word.correctAnswerCount}" })
    }

    fun getStatistic(): String {
        val learned = dictionary.filter { it.correctAnswerCount >= learnedAnswerCount }.size
        val allWords = dictionary.size
        val percent = learned.toDouble() / allWords.toDouble() * PERCENT
        return "выучено $learned из $allWords |${percent.toInt()}%"

    }


    fun getNextQuestion(): Question? {
        val notLearnedList = dictionary.filter { it.correctAnswerCount < learnedAnswerCount }
        if (notLearnedList.isEmpty()) return null

        val questionWords = if (notLearnedList.size < countOfQuestion) {
            val learnedList = dictionary.filter { it.correctAnswerCount >= learnedAnswerCount }.shuffled()
            learnedList.take(countOfQuestion - notLearnedList.size) + notLearnedList.shuffled().take(countOfQuestion)
        } else {
            notLearnedList.shuffled().take(countOfQuestion)
        }.shuffled()
        val correctAnswer = questionWords.random()

        question = Question(
            variants = questionWords,
            correctAnswer = correctAnswer
        )
        return question
    }

    fun checkAnswer(userAnswerIndex: Int?): Boolean {
        return question?.let {
            val correctAnswerId = it.variants.indexOf(it.correctAnswer)
            if (correctAnswerId == userAnswerIndex) {
                it.correctAnswer.correctAnswerCount++
                saveDictionary(dictionary)
                true
            } else {
                false
            }
        } ?: false
    }

}