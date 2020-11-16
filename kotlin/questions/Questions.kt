package questions

class Questions(val questions: ArrayList<Question>) {
    fun getRandomOfDifficulty(difficulty: Int): Question = questions.filter { it.difficulty == difficulty }.random()
}
