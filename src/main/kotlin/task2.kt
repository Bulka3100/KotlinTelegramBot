fun main() {

while (true){
    println("""Меню:
        |1-Учить слова
        |2-Статистика
        |0-Выход
    """.trimMargin())
    val choice = readln()
    when(choice){
        "1"->println("Учить слова")
        "2"-> println("Статистика")
        "0"->break
        else -> println("введите пункт меню")
    }
}
}
fun loadDictionary(){

}