import java.io.File

fun main() {

    val wordsFile = File("words.txt")
    val dictionary = mutableListOf<Word>()
    fun loadDictionary(): MutableList<Word> {
        wordsFile.forEachLine { line ->
            val line = line.split("|")
            val word =
                Word(origin = line[0], translate = line[1], correctAnswerCount = line.getOrNull(2)?.toIntOrNull() ?: 0)
            dictionary.add(word)
        }
        return dictionary
    }

    fun totalCount(): Int{
        return wordsFile.readLines().size
    }
    fun learned(): Int{
        return dictionary.filter { it.correctAnswerCount>=3 }.size
    }

    fun findPercent(): String{
        return "${learned()/totalCount()*100}%"
    }
    println(totalCount())
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
            "1" -> println("Учить слова")
            "2" -> println("Выучено ${learned()} из ${totalCount()} слов | ${findPercent()}")
            "0" -> break
            else -> println("введите пункт меню")
        }
    }
}

data class Word(
    val correctAnswerCount: Int = 0,
    val origin: String,
    val translate: String,
)