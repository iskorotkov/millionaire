package game

import questions.Question
import questions.Questions

class Game(private val questions: Questions) {
    private var selectedTier: Int = 0
    private val tiers = 15
    private val lifelines = Lifelines()
    private val state: State

    init {
        val q = questions.getRandomOfDifficulty(0)
        state = State(q)
    }

    fun answer(index: Int): Boolean {
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

        if (state.passedQuestions == tiers) {
            state.currentQuestion = null
            state.gameStatus = GameStatus.Won
        } else {
            state.currentQuestion = questions.getRandomOfDifficulty(state.passedQuestions)
            state.gameStatus = GameStatus.InProgress

        }
    }

    fun getQuestion(): Question = state.currentQuestion!!

    fun getCurrentReward(): Int = when (state.gameStatus) {
        GameStatus.Won -> rewardIfPlayerWon()
        GameStatus.Lost -> rewardIfPlayerLost()
        GameStatus.InProgress -> rewardIfGameContinues()
    }

    private fun rewardIfGameContinues() = when (state.passedQuestions) {
        0 -> 0
        else -> state.passedQuestions - 1
    }

    private fun rewardIfPlayerWon() = tiers - 1

    private fun rewardIfPlayerLost() = when (state.passedQuestions >= selectedTier) {
        true -> selectedTier
        false -> 0
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
            .filter { it.isActive }
            .filterIndexed { i, _ -> i != getQuestion().rightAnswer }
            .shuffled()
            .drop(1)
            .forEach { it.isActive = false }
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
