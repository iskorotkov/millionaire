package storage

import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager

class FileToSQLiteConverter {
    private val loader = FileLoader()

    fun convert(filepath: String, connectionString: String) {
        val data = loader.load(filepath)

        Class.forName("org.sqlite.JDBC")

        // TODO: automatically create all tables if they don't exist

        try {
            val conn: Connection = DriverManager.getConnection(connectionString)

            data.questions.forEach { question ->
                // TODO: check if question is already present

                val questionInsertion = conn.prepareStatement(
                    """insert into questions(text, difficulty) 
                    |values (?, ?)""".trimMargin()
                )
                questionInsertion.setString(1, question.text)
                questionInsertion.setInt(2, question.difficulty)
                questionInsertion.execute()

                val questionIdRetrieval = conn.createStatement()
                val result = questionIdRetrieval.executeQuery("""select last_insert_rowid() as id""")
                result.next()
                val questionId = result.getInt("id")
                result.close()

                question.answers.forEach {
                    val isCorrect = when (it.isCorrect) {
                        true -> 1
                        false -> 0
                    }

                    val answerInsertion = conn.prepareStatement(
                        """insert into answers(text, isCorrect, question_id) 
                        |values (?, ?, ?);""".trimMargin()
                    )
                    answerInsertion.setString(1, it.text)
                    answerInsertion.setInt(2, isCorrect)
                    answerInsertion.setInt(3, questionId)
                    answerInsertion.execute()
                }
            }

            conn.close()
        } catch (e: Exception) {
            println("Couldn't write questions to DB")
            throw e
        }
    }
}

fun main() {
    val converter = FileToSQLiteConverter()
    val from = System.getenv("QUESTIONS_FILE")
    val to = System.getenv("QUESTIONS_DB")
    converter.convert(from, to)
    println("Conversion completed")
}
