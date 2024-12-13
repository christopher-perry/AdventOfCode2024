package calculators

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import parser.InputParser

class MultSumCalculator @JvmOverloads constructor(path: String = PATH) {
    companion object {
        private const val PATH = "resources/input/multiples.txt"
        val multRegex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
        val doRegex = Regex("do\\(\\)")
        val dontRegex = Regex("don't\\(\\)")
    }
    private val validPairs: List<Pair<Int, Int>>

    private val products: List<Int>

    private val logger: Logger = LogManager.getLogger()

    init {
        val rawLines = InputParser.parseInput(path)
        validPairs = processRawLines(rawLines)
        products = validPairs.map { it.first * it.second }
    }

    /**
     * Process each line of input, filling the do() ranges, don't() ranges, and mul(A,B) pairs
     */
    private fun processRawLines(rawLines: List<String>): List<Pair<Int, Int>> {
        var lineStartsDisabled = false
        return rawLines.flatMap {
            val (validRanges, nextLineStartsDisabled) = extractValidRanges(it, lineStartsDisabled)
            lineStartsDisabled = nextLineStartsDisabled
            logger.debug("Valid ranges: {}", validRanges)
            multRegex.findAll(it).filter { mult ->
                val index = mult.range.first
                val valid = validRanges.find { range -> range.contains(index) } != null
                if (!valid) {
                    logger.trace("mul was disabled by don't(): ${mult.groupValues.first()} at line index $index")
                }
                valid
            }.map { match ->
                val (first, second) = match.destructured
                logger.debug("$first, $second")
                first.toInt() to second.toInt()
            }
        }
    }

    private fun extractValidRanges(it: String, lineStartsDisabled: Boolean): Pair<List<IntRange>, Boolean> {
        val doRanges = mutableListOf<Int>()
        val dontRanges = mutableListOf<Int>()
        (if (lineStartsDisabled) dontRanges else doRanges).add(0)

        doRegex.findAll(it).forEach { match ->
            doRanges.add(match.range.first)
        }

        dontRegex.findAll(it).forEach { match ->
            dontRanges.add(match.range.last)
        }

        val validRanges = mutableListOf<IntRange>()
        var dontIndex = 0
        var lowestUnmatchedDo:Int? = null
        doRanges.forEach { doValue ->
            // Avoid adding ranges already covered, such as do() *do()* don't()
            if (validRanges.isEmpty() || !validRanges.last().contains(doValue)) {
                // Iterate over don't indices until we find one bigger than our current range
                var dontValue = dontRanges[dontIndex]
                while (dontIndex < dontRanges.size -1 && dontValue < doValue) {
                    dontValue = dontRanges[++dontIndex]
                }

                if (dontValue > doValue) {
                    validRanges.add(doValue..dontValue)
                } else if (lowestUnmatchedDo == null) {
                    lowestUnmatchedDo = doValue
                }
            }
        }

        // We need to preserve the do/don't state for the next line.
        val nextLineStartsDisabled = doRanges.last() < dontRanges.last()
        if (!nextLineStartsDisabled && lowestUnmatchedDo != null) {
            validRanges.add(lowestUnmatchedDo!!..it.length)
        }

        return Pair(validRanges, nextLineStartsDisabled)
    }

    fun isValidMult(multIndex: Int, validRanges: List<IntRange>): Boolean {
        for (range in validRanges) {
            if (range.contains(multIndex)) {
                return true
            }
        }
        return false
    }

    val sumOfProducts: Int
        get() {
            return products.sum()
        }

    fun extract(line: String): Sequence<MatchResult> {
        return multRegex.findAll(line)
    }
}
