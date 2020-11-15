package game

import questions.AnswerResult as AnswerResult
import rewards.Rewards
import questions.Question
import questions.Questions

class Game(private val questions: Questions) {
    private val rewards = Rewards()
    private val lifelines = Lifelines()
    private val state: State

    init {
        val q = questions.getRandomOfDifficulty(0)
        state = State(q)
    }

    fun answer(index: Int): AnswerResult = if (state.currentQuestion.rightAnswer == index) {
        userAnsweredCorrectly()
    } else {
        userAnsweredIncorrectly()
    }

    private fun userAnsweredIncorrectly() = if (lifelines.isSecondChangeActivated) {
        AnswerResult(false, GameStatus.InProgress, state.currentQuestion)
    } else {
        AnswerResult(false, GameStatus.Lost, null)
    }

    private fun userAnsweredCorrectly(): AnswerResult {
        state.passedQuestions++
        return if (state.passedQuestions == rewards.values.size) {
            AnswerResult(true, GameStatus.Won, null)
        } else {
            AnswerResult(true, GameStatus.InProgress, questions.getRandomOfDifficulty(state.passedQuestions))
        }
    }

    fun getQuestion(): Question = state.currentQuestion

    fun getCurrentReward(): Int = when (state.gameStatus) {
        GameStatus.Won -> rewardIfPlayerWon()
        GameStatus.Lost -> rewardIfPlayerLost()
        GameStatus.InProgress -> rewardIfGameContinues()
    }

    private fun rewardIfGameContinues() = when (state.passedQuestions) {
        0 -> 0
        else -> rewards.values[state.passedQuestions - 1]
    }

    private fun rewardIfPlayerWon() = rewards.values.last()

    private fun rewardIfPlayerLost() = when (state.passedQuestions >= rewards.selectedTier) {
        true -> rewards.values[rewards.selectedTier]
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
    }

    fun takeQuestionSwap() {
        if (!hasQuestionSwap()) {
            throw GameLogicException()
        }

        lifelines.hasQuestionSwap = false
        lifelines.lifelinesLeft--
    }

    fun hasHallHelp(): Boolean = lifelines.hasHallHelp && lifelines.lifelinesLeft > 0
    fun hasFiftyFifty(): Boolean = lifelines.hasFiftyFifty && lifelines.lifelinesLeft > 0
    fun hasCallFriend(): Boolean = lifelines.hasCallFriend && lifelines.lifelinesLeft > 0
    fun hasSecondChance(): Boolean = lifelines.hasSecondChance && lifelines.lifelinesLeft > 0
    fun hasQuestionSwap(): Boolean = lifelines.hasQuestionSwap && lifelines.lifelinesLeft > 0
}
