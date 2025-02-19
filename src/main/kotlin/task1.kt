package org.example

fun Question.questionToString(question: Question): String {
    val variants = this.variants
        .mapIndexed { index, word -> ("${index + 1}- ${word.translate}") }
        .joinToString("\n")
    return this.correctAnswer.origin + "\n" + variants + "\n" + "0-выйти в меню"
}

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
            "1" -> while (true) {
                val question = trainer.getNextQuestion()
                if (question != null) {
                    println(question.questionToString(question))
                    val userAnswerInput = readln().toInt()
                    if (userAnswerInput == 0) break
                    if (trainer.checkAnswer(userAnswerInput?.minus((1)))) {
                        println("верно")
                    } else {
                        println("Неправильно! ${question.correctAnswer.origin}-это ${question.correctAnswer.translate}")
                    }
                } else {
                    println("все слова выучены")
                    break
                }
            }

            "2" -> println(trainer.getStatistic())
            "0" -> break
            else -> println("введите пункт меню")
        }
    }
}


const val MIN_WORDS = 3
const val PERCENT = 100



