package game

import questions.Question
import questions.Questions

class Game(private val questions: Questions, private val rewards: Rewards) {
    private val lifelines = Lifelines()
    private val state: State = State(questions.getRandomOfDifficulty(0))

    fun answer(index: Int): Boolean {
        getQuestion().answers[index].disable()
        val isCorrect = getQuestion().rightAnswer == index

        if (isCorrect) {
            userAnsweredCorrectly()
        } else {
            userAnsweredIncorrectly()
        }

        lifelines.isSecondChangeActivated = false
        return isCorrect
    }

    fun getGameStatus(): GameStatus = state.gameStatus

    private fun userAnsweredIncorrectly() {
        if (lifelines.isSecondChangeActivated) {
            state.gameStatus = GameStatus.InProgress
        } else {
            state.currentQuestion = null
            state.gameStatus = GameStatus.Lost
        }
    }

    private fun userAnsweredCorrectly() {
        state.passedQuestions++

        if (state.passedQuestions == rewards.length()) {
            state.currentQuestion = null
            state.gameStatus = GameStatus.Won
        } else {
            state.currentQuestion = questions.getRandomOfDifficulty(state.passedQuestions)
            state.gameStatus = GameStatus.InProgress
        }
    }

    fun getQuestion(): Question = state.currentQuestion!!

    fun getCurrentReward(): Int = when (state.gameStatus) {
        GameStatus.Won -> rewards.get(rewards.length() - 1)
        GameStatus.Lost -> when (state.passedQuestions > rewards.getSelectedTier()) {
            true -> rewards.get(rewards.getSelectedTier())
            false -> 0
        }
        GameStatus.InProgress -> rewards.get(state.passedQuestions)
    }

    fun takeHallHelp() {
        if (!hasHallHelp()) {
            throw GameLogicException()
        }

        lifelines.hasHallHelp = false
        lifelines.lifelinesLeft--
    }

    fun takeFiftyFifty() {
        if (!hasFiftyFifty()) {
            throw GameLogicException()
        }

        lifelines.hasFiftyFifty = false
        lifelines.lifelinesLeft--

        getQuestion().answers
            .filter { it.isEnabled() }
            .filterIndexed { i, _ -> i != getQuestion().rightAnswer }
            .shuffled()
            .drop(1)
            .forEach { it.disable() }
    }

    fun takeCallFriend() {
        if (!hasCallFriend()) {
            throw GameLogicException()
        }

        lifelines.hasCallFriend = false
        lifelines.lifelinesLeft--
    }

    fun takeSecondChance() {
        if (!hasSecondChance()) {
            throw GameLogicException()
        }

        lifelines.hasSecondChance = false
        lifelines.lifelinesLeft--

        lifelines.isSecondChangeActivated = true
    }

    fun takeQuestionSwap() {
        if (!hasQuestionSwap()) {
            throw GameLogicException()
        }

        lifelines.hasQuestionSwap = false
        lifelines.lifelinesLeft--

        state.currentQuestion = questions.getRandomOfDifficulty(state.passedQuestions)
    }

    fun hasHallHelp(): Boolean = lifelines.hasHallHelp && lifelines.lifelinesLeft > 0
    fun hasFiftyFifty(): Boolean = lifelines.hasFiftyFifty && lifelines.lifelinesLeft > 0
    fun hasCallFriend(): Boolean = lifelines.hasCallFriend && lifelines.lifelinesLeft > 0
    fun hasSecondChance(): Boolean = lifelines.hasSecondChance && lifelines.lifelinesLeft > 0
    fun hasQuestionSwap(): Boolean = lifelines.hasQuestionSwap && lifelines.lifelinesLeft > 0
}
