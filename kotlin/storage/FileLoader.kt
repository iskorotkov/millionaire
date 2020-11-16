package storage

import questions.Answer
import questions.Question
import questions.Questions
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

class FileLoader : QuestionsLoader {
    override fun load(path: String): Questions {
        val questions = ArrayList<Question>()

        try {
            val stream = FileInputStream(path)
            val reader = BufferedReader(InputStreamReader(stream))

            var line = reader.readLine()
            while (line != null) {
                val q = parseQuestion(line)
                questions.add(q)

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

        return Questions(questions)
    }

    private fun parseQuestion(line: String): Question {
        val values = line.split('\t')

        val question = values[0]
        val answers = values.subList(1, 5).map { Answer(it) }.toTypedArray()
        val rightAnswer = values[5].toInt() - 1
        val difficulty = values[6].toInt() - 1
        return Question(question, answers, rightAnswer, difficulty)
    }
}
