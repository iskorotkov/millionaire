package questions

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

class Questions {
    constructor(path: String) {
        try {
            val stream = FileInputStream(path)
            val reader = BufferedReader(InputStreamReader(stream))

            var line = reader.readLine()
            while (line != null) {
                val values = line.split('\t')

                val question = values[0]
                val answers = values.subList(1, 5).map { Answer(it, true) }.toTypedArray()
                val rightAnswer = values[5].toInt() - 1
                val difficulty = values[6].toInt() - 1
                questions.add(Question(question, answers, rightAnswer, difficulty))

                line = reader.readLine()
            }

            reader.close()
            stream.close()
        } catch (e: IOException) {
            println("Произошла ошибка при чтении файла")
            throw e
        } catch (e: IndexOutOfBoundsException) {
            println("В строке недостаточно значений")
            throw e
        }
    }

    private val questions: ArrayList<Question> = ArrayList()

    fun getRandomOfDifficulty(difficulty: Int): Question = questions.filter { it.difficulty == difficulty }.random()
}
