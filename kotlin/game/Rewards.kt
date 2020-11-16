package game

class Rewards(
    private val selectedTier: Int, private val rewards: Array<Int>
) {
    fun getSelectedTier() = selectedTier
    fun length() = rewards.size
    fun get(index: Int) = rewards[index]
}
