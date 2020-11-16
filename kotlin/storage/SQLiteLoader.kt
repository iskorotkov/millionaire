package storage

import questions.Answer
import questions.Question
import questions.Questions
import java.lang.Exception
import java.sql.Connection
import java.sql.ResultSet

import java.sql.DriverManager
import java.sql.Statement

class SQLiteLoader : QuestionsLoader {
    override fun load(path: String): Questions {
        Class.forName("org.sqlite.JDBC")

        val results = ArrayList<Question>()

        try {
            val conn: Connection = DriverManager.getConnection(path)
            val statement: Statement = conn.createStatement()

            val query = """
                    select q.id  as question_id,
                    q.text       as question_text,
                    q.difficulty as question_difficulty,
                    a.id         as answer_id,
                    a.text       as answer_text,
                    a.isCorrect  as is_answer_correct
                    from questions q
                    join answers a on q.id = a.question_id
                    order by q.id, a.id""".trimMargin()
            val set: ResultSet = statement.executeQuery(query)

            var previousId = -1
            var text: String? = null
            var difficulty: Int? = null
            val answers = ArrayList<Answer>()

            while (set.next()) {
                val id = set.getInt("question_id")

                if (id == previousId) {
                    answers.add(createAnswer(set))
                } else {
                    if (previousId >= 0) {
                        val q = Question(text!!, answers.toTypedArray(), difficulty!!)
                        results.add(q)
                        answers.clear()
                    }

                    previousId = id
                    text = set.getString("question_text")
                    difficulty = set.getInt("question_difficulty")

                    answers.add(createAnswer(set))
                }
            }

            set.close()
            conn.close()
        } catch (e: Exception) {
            println("Не получилось прочитать вопросы из БД")
            throw e
        }

        return Questions(results)
    }

    private fun createAnswer(set: ResultSet): Answer {
        val answerText = set.getString("answer_text")
        val isCorrect = set.getInt("is_answer_correct") > 0
        return Answer(answerText, isCorrect)
    }
}
