package questions

class Answer(private var text: String) {
    private var enabled: Boolean = true

    fun getText() = text

    fun disable() {
        enabled = false
    }

    fun isEnabled() = enabled
}
