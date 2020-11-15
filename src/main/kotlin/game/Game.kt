package game

import rewards.Rewards
import questions.Question
import questions.Questions

class Game(private val questions: Questions) {
    private val prizes = Rewards()
    private val state: GameState = GameState()

    fun answer(index: Int) {
        // TODO:
    }

    fun getNextQuestion(): Question {
        // TODO:
    }

    fun getCurrentReward(): Int {
        // TODO:
    }

    fun takeHallHelp() {
        if (!hasHallHelp()) {
            throw GameLogicException()
        }
    }

    fun takeFiftyFifty() {
        if (!hasFiftyFifty()) {
            throw GameLogicException()
        }
    }

    fun takeCallFriend() {
        if (!hasCallFriend()) {
            throw GameLogicException()
        }
    }

    fun takeSecondChance() {
        if (!hasSecondChance()) {
            throw GameLogicException()
        }
    }

    fun takeQuestionSwap() {
        if (!hasQuestionSwap()) {
            throw GameLogicException()
        }
    }

    fun hasHallHelp(): Boolean = state.hasHallHelp
    fun hasFiftyFifty(): Boolean = state.hasFiftyFifty
    fun hasCallFriend(): Boolean = state.hasCallFriend
    fun hasSecondChance(): Boolean = state.hasSecondChance
    fun hasQuestionSwap(): Boolean = state.hasQuestionSwap
}
