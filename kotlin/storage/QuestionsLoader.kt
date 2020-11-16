package storage

import questions.Questions

interface QuestionsLoader {
    fun load(path: String): Questions
}
