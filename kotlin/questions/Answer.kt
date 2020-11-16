package questions

class Answer(val text: String, val isCorrect: Boolean) {
    private var enabled: Boolean = true

    fun disable() {
        enabled = false
    }

    val isEnabled
        get() = enabled
}
