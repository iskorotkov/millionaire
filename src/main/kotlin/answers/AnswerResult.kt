package answers

import game.GameStatus
import questions.Question

class AnswerResult(val correct: Boolean, val gameStatus: GameStatus, val nextQuestion: Question?)
