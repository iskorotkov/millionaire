package game

import questions.AnswerResult
import questions.Question

class GameState(
    var lifelinesLeft: Int = 4,
    var passedQuestions: Int = 0,
    var selectedTier: Int = 0,
    var hasHallHelp: Boolean = false,
    var hasFiftyFifty: Boolean = false,
    var hasCallFriend: Boolean = false,
    var hasSecondChance: Boolean = false,
    var hasQuestionSwap: Boolean = false,
    var currentQuestion: Question? = null,
    var lastStatus: AnswerResult? = null
)
