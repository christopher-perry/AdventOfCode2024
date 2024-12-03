package parser

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

data class Report(
    private val levels: List<Int>,
    private val maxProblemDampeningLevel: Int = 1,
    private var unsafeLevelCounter: Int = 0
) {
    private val logger: Logger = LogManager.getLogger("Report.kt")
    val isSafe: Boolean
    val isAscending: Boolean

    // Overloaded constructor for Java compatibility
    constructor(levels: List<Int>) : this(levels, 1, 0)

    init {
        if (levels.size < 2) {
            isSafe = false
            isAscending = false
        } else {
            isAscending = levels[0] < levels[1]
            isSafe = calculateIsSafe()
        }
    }

    private fun calculateIsSafe(): Boolean {
        if (levels.size < 2) return false
        val max = levels.size - 1

        for ((i, level) in levels.withIndex()) {
            if (i >= max) {
                break
            }
            val nextLevel = levels[i + 1]
            if (!isSafeLevelChange(level, nextLevel)) {
                unsafeLevelCounter++
                logger.debug(
                    "Unsafe level change detected between {} and {} in {}. Unsafe level counter: {}",
                    level,
                    nextLevel,
                    levels,
                    unsafeLevelCounter
                )
                if (unsafeLevelCounter > maxProblemDampeningLevel) {
                    return false
                } else {
                    val mutableList = levels.toMutableList()
                    mutableList.removeAt(i)
                    if (Report(mutableList, maxProblemDampeningLevel, unsafeLevelCounter).isSafe) {
                        logger.info("Removing $level from $levels made it safe")
                        return true
                    } else {
                        val mutableList2 = levels.toMutableList()
                        mutableList2.removeAt(i + 1)
                        if (Report(mutableList2, maxProblemDampeningLevel, unsafeLevelCounter).isSafe) {
                            logger.info("Removing $nextLevel from $levels made it safe")
                            return true
                        } else {
                            return false
                        }
                    }
                }
            }
        }
        return true
    }

    override fun toString(): String {
        var str = "$levels: "
        if (!isSafe) {
            str += "un"
        }
        str += "safe"
        if (unsafeLevelCounter > 0 && unsafeLevelCounter <= maxProblemDampeningLevel) {
            str += ", $unsafeLevelCounter unsafe levels"
        }
        return str
    }

    private fun isSafeLevelChange(levelOne: Int, levelTwo: Int): Boolean {
        val directionMultiplier = if (isAscending) -1 else 1
        val difference = (levelOne - levelTwo) * directionMultiplier
        return difference in 1..3
    }
}