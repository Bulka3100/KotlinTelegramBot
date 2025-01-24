package org.example

import java.io.File

fun main() {
 val wordsFile = File("words.txt")
 wordsFile.createNewFile() //в файл же вручную записали значения
 for (i in wordsFile.readLines()) println(i)
}