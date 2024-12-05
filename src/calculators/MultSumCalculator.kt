package calculators

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import parser.InputParser

class MultSumCalculator @JvmOverloads constructor(path: String? = PATH) {
    private val validPairs: List<Pair<Int, Int>>
    private val products: List<Int>
    val multRegex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
    private val LOG: Logger = LogManager.getLogger(MultSumCalculator)

    init {
        val rawLines = InputParser.parseInput(path)
        validPairs = processRawLines(rawLines)
        products = validPairs.map { it.first * it.second }
    }

    private fun processRawLines(rawLines: List<String>): List<Pair<Int, Int>> {
        return rawLines.flatMap {
            multRegex.findAll(it).map { match ->
                val (first, second) = match.destructured
                LOG.debug("$first, $second")
                first.toInt() to second.toInt()
            }
        }
    }

    val sumOfProducts: Int
        get() {
            return products.sum()
        }

    companion object {
        private const val PATH = "resources/input/multiples.txt"
    }

    fun extract(line: String): Sequence<MatchResult> {
        return multRegex.findAll(line)
    }
}
