package game

import questions.Question

class State(
    var currentQuestion: Question?,
    var gameStatus: GameStatus = GameStatus.InProgress,
    var passedQuestions: Int = 0,
)
