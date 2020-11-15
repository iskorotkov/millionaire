package questions

import answers.Answer

class Question(val text: String, val answers: List<Answer>, val rightAnswer: Int, val difficulty: Int);
