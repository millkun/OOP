package main

import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import javax.xml.parsers.DocumentBuilderFactory

fun main() {
    while (true) {
        print("Введите название файла (или 'exit' для выхода): ")
        val input: String = readlnOrNull().toString()

        if (input.equals("exit", ignoreCase = true)) {
            println("Завершение работы приложения.")
            break
        }

        val fileController: FileController

        try {
            fileController = FileController(input)
            fileController.getDuplicate()
            fileController.getFloutCount()
            fileController.getTime()
        } catch (e: FileNotFoundException) {
            println("Ошибка: ${e.message}")
        } catch (e: IllegalArgumentException) {
            println("Ошибка: ${e.message}")
        } catch (e: Exception) {
            println("Произошла непредвиденная ошибка: ${e.message}")
        }
    }
}

class FileController(path: String) : FileControllerInterface {
    private val service: FileServiceInterface = when (path.substringAfterLast(".")) {
        "csv" -> CSVFileService(path)
        "xml" -> XMLFileService(path)
        else -> throw IllegalArgumentException("Неизвестный тип: ${path.substringAfterLast(".")}")
    }

    private val triple = service.open()

    private val duplicates = triple.first
    private val floorCount = triple.second
    private val time = triple.third

    override fun getDuplicate() {
        println("Дублирующиеся записи с количеством повторений:")
        duplicates.forEach { (key, value) ->
            if (value > 1) println("$key: $value")
        }
    }

    override fun getFloutCount() {
        println("Количество этажей в городах:")
        floorCount.forEach { (floor, count) ->
            println("$floor этажных зданий: $count")
        }
    }

    override fun getTime() {
        println("Время выполнения: $time мс")
    }
}

interface FileControllerInterface {
    fun getDuplicate()
    fun getFloutCount()
    fun getTime()
}

interface FileServiceInterface {
    fun open(): Triple<Map<String, Int>, Map<Int, Int>, Long>
}

class CSVFileService(private val path: String) : FileServiceInterface {
    override fun open(): Triple<Map<String, Int>, Map<Int, Int>, Long> {
        val startTime = System.currentTimeMillis()
        val duplicates = mutableMapOf<String, Int>()
        val floorCount = mutableMapOf<Int, Int>()

        BufferedReader(FileReader(File(path))).use { reader ->
            var isFirstLine = true
            reader.lineSequence().forEach { line ->
                if (isFirstLine) {
                    isFirstLine = false
                    return@forEach
                }
                println("Обрабатывается строка: $line") // Тоже отладка. Можно закомментить
                val parts = line.split(";")
                if (parts.size < 4) {
                    println("Некорректная строка: $line")
                    return@forEach
                }

                val city = parts[0].trim().replace("\"", "")
                val floors = parts[3].trim().replace("\"", "").toIntOrNull()
                if (floors == null) {
                    println("Некорректное количество этажей в строке: $line")
                    return@forEach
                }

                duplicates[city] = duplicates.getOrDefault(city, 0) + 1
                floorCount[floors] = floorCount.getOrDefault(floors, 0) + 1
            }
        }

        val endTime = System.currentTimeMillis()
        return Triple(duplicates, floorCount, endTime - startTime)
    }
}

class XMLFileService(private val path: String) : FileServiceInterface {
    override fun open(): Triple<Map<String, Int>, Map<Int, Int>, Long> {
        val startTime = System.currentTimeMillis()
        val duplicates = mutableMapOf<String, Int>()
        val floorCount = mutableMapOf<Int, Int>()

        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(File(path))
        val items = doc.getElementsByTagName("item")

        for (i in 0 until items.length) {
            val itemElement = items.item(i)
            val cityName = itemElement.attributes.getNamedItem("city").nodeValue
            val floors = itemElement.attributes.getNamedItem("floor").nodeValue.toIntOrNull()
            if (floors == null) {
                println("Некорректное количество этажей в элементе: $itemElement")
                continue
            }

            println("Обрабатывается элемент: $cityName, $floors этажей") // Для отладки. В целом можно
            // и убрать, чтобы место не занимало.

            duplicates[cityName] = duplicates.getOrDefault(cityName, 0) + 1
            floorCount[floors] = floorCount.getOrDefault(floors, 0) + 1
        }

        val endTime = System.currentTimeMillis()
        return Triple(duplicates, floorCount, endTime - startTime)
    }
}
