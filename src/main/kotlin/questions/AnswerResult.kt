package questions

import game.GameStatus

class AnswerResult(val correct: Boolean, val gameStatus: GameStatus, val nextQuestion: Question?)
