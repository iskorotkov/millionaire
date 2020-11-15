package rewards

class Rewards(
    var selectedTier: Int = 0, val values: Array<Int> = arrayOf(
        500,
        1_000,
        2_000,
        3_000,
        5_000,
        10_000,
        15_000,
        25_000,
        50_000,
        100_000,
        200_000,
        400_000,
        800_000,
        1_500_000,
        3_000_000
    )
)
