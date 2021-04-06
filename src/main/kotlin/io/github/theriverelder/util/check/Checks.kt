package io.github.theriverelder.util.check

import io.github.theriverelder.util.DICE_1D100
import io.github.theriverelder.util.Dice

enum class CheckHardness(val ratio: Double) {
    NORMAL(1.0),
    HARD(0.5),
    EXTREME(0.2),
    ;

    fun calc(value: Int): Int = (value * ratio).toInt()

    override fun toString(): String = name.toLowerCase()
}

enum class CheckResultType(val text: String? = null) {
    GREAT_FAILURE,
    FAILURE,
    NORMAL_SUCCESS,
    HARD_SUCCESS,
    EXTREME_SUCCESS,
    GREAT_SUCCESS,
    ;

    override fun toString(): String = text ?: this.name.toLowerCase().replace(Regex("[^a-zA-Z0-9]+"), " ")
}

data class CheckResult(
    val succeed: Boolean,
    val points: Int,
    val offset: Int,
    val value: Int,
    val target: Int,
    val hardness: CheckHardness,
    val resultType: CheckResultType,
)

fun check(
    value: Int,
    hardness: CheckHardness = CheckHardness.NORMAL,
    dice: Dice = DICE_1D100,
    offset: Int = 0,
    greatSuccess: Int = 5,
    greatFailure: Int = 95,
): CheckResult {
    val target: Int = hardness.calc(value)
    val points = dice.roll()
    val resultType: CheckResultType = when {
        points <= greatSuccess -> CheckResultType.GREAT_SUCCESS
        points <= CheckHardness.EXTREME.calc(value) -> CheckResultType.EXTREME_SUCCESS
        points <= CheckHardness.HARD.calc(value) -> CheckResultType.HARD_SUCCESS
        points <= CheckHardness.NORMAL.calc(value) -> CheckResultType.NORMAL_SUCCESS
        points <= greatFailure -> CheckResultType.FAILURE
        else -> CheckResultType.GREAT_FAILURE
    }
    return CheckResult(
        points <= target || points <= greatSuccess,
        points,
        offset,
        value,
        target,
        hardness,
        resultType,
    )
}