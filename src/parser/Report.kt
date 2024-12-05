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
    var isAscending: Boolean

    // Overloaded constructor for Java compatibility
    constructor(levels: List<Int>) : this(levels, 1, 0)

    init {
        if (levels.size < 2) {
            isSafe = true
            isAscending = false
        } else {
            isAscending = levels[0] < levels[1]
            isSafe = calculateIsSafe()
        }
    }

    /**
     * Iterate through levels ensuring that each level is within 1-3 levels according to prevailing ascending/descending
     * order, using recursion to mitigate problematic levels up to maxProblemDampeningLevel.
     */
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
                    return hasSafeDampenedReport(i)
                }
            }
        }
        return true
    }

    /**
     * Traverse the possible permutations surrounding the given index, checking for safe reports, returning as soon as a
     * safe report is found.  Returns false if no such report exists.
     */
    private fun hasSafeDampenedReport(i: Int): Boolean {
        val reportPermutations = mutableListOf(i, i + 1)
        // Only look back 1 if this is not the first element
        if (i > 0) {
            reportPermutations.add(0, i - 1)
        }
        reportPermutations.forEach {
            val dampenedReport = dampenReport(it)
            unsafeLevelCounter = dampenedReport.unsafeLevelCounter
            isAscending = dampenedReport.isAscending
            if (dampenedReport.isSafe) {
                logger.info("Removing ${levels[it]} from $levels made it safe")
                return true
            }
        }
        return false
    }

    /**
     * Create a dampened Report by removing the level at the given index
     */
    private fun dampenReport(indexToRemove: Int): Report {
        val dampenedLevels = levels.toMutableList().apply { removeAt(indexToRemove) }
        return Report(dampenedLevels, maxProblemDampeningLevel, unsafeLevelCounter)
    }

    override fun toString(): String {
        var str = "$levels: "
        if (!isSafe) {
            str += "un"
        }
        str += "safe"
        if (unsafeLevelCounter > 0) {
            if (isSafe) {
                str += ", $unsafeLevelCounter unsafe levels"
            }
        }
        return str
    }

    private fun isSafeLevelChange(levelOne: Int, levelTwo: Int): Boolean {
        val directionMultiplier = if (isAscending) -1 else 1
        val difference = (levelOne - levelTwo) * directionMultiplier
        return difference in 1..3
    }
}