package org.example

fun main() {
    val trainer = LearnWordsTrainer()
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
            "1" -> while (trainer.dictionary.filter { it.correctAnswerCount < MIN_WORDS }.isNotEmpty()) trainer.learnWords()
            "2" -> println(trainer.getStatistic())
            "0" -> break
            else -> println("введите пункт меню")
        }
    }
}



const val MIN_WORDS = 3
const val PERCENT = 100

