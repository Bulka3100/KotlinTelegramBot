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
            "1" -> while (trainer.dictionary.filter { it.correctAnswerCount < MIN_WORDS }.isNotEmpty()) {
                val question=trainer.getNextQuestion()
                if ( question != null) {
                    for (i in question.variants) {
                        question.variants = question.variants.shuffled()
                        val correctAnswer = question.variants.indexOf(i) + 1
                        println("${i.origin}:")
                        question.variants.mapIndexed { index, word -> println("${index + 1}- ${word.translate}") }
                        println("--------")
                        println("0 - Меню")
                        val userAnswerInput = readln().toInt()
                        when (userAnswerInput) {
                            0 -> return
                            correctAnswer -> {
                                println("правильно")
                                i.correctAnswerCount++
                                trainer.saveDictionary(trainer.dictionary)
                            }

                            else -> println("неправильно! ${i.origin} это ${i.translate}")
                        }

                    }
                } else println("все слова выучены")
            }
                "2" -> println(trainer.getStatistic())
                "0" -> break
                else -> println("введите пункт меню")
            }
        }
    }



const val MIN_WORDS = 3
const val PERCENT = 100



