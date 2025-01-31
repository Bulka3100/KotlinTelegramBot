import Word
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
            "1" -> println("Учить слова")
            "2" -> println("Статистика")
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

v
fun loadDictionary(): List<Word> {
    val dictionary = mutableListOf<Word>()
    val wordsFile = File("words.txt")
    wordsFile.forEachLine { line ->
        val line = line.split("|")
        val word =
            Word(origin = line[0], translate = line[1], correctAnswerCount = line.getOrNull(2)?.toIntOrNull() ?: 0)
        dictionary.add(word)
    }
    val nonMutableDictionary = dictionary.toList()
    return nonMutableDictionary
}