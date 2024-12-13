package calculators

import com.sun.jndi.toolkit.dir.DirSearch.search
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import parser.InputParser

open class WordSearchCalculator @JvmOverloads constructor(path: String = PATH) {
    companion object {
        const val PATH = "resources/input/wordsearch.txt"
//        private const val PATH = "resources/input/wordsearch_example.txt"
    }
    open val wordbank = listOf("XMAS".toCharArray())
    private val logger: Logger = LogManager.getLogger(WordSearchCalculator)
    protected val board:List<CharArray>

    init {
        val rawLines = InputParser.parseInput(path)
        board = rawLines.map { line -> line.toCharArray() }
        logger.debug("{} is the word\n, {}", wordbank, board.joinToString { s -> "${s.joinToString("")}\n" })
    }

    open val count:Int
        get() {
            var total = 0
            board.forEachIndexed { row, letters ->
                letters.forEachIndexed { column, letter ->
                    // If this could be the start of WORD, check in all possible directions for the next letter
                    wordbank.forEach { word ->
                        if (letter == word.first()) {
                            total += search(row, column, word)
                        }
                    }
                }
            }
            return total
        }

    protected open fun search(row: Int, column: Int, word: CharArray): Int {
        var count = 0
        for (x in -1..1) {
            for (y in -1..1) {
                if (seek(word, row, column, Pair(x, y))) {
                    count++
                }
            }
        }
        return count
    }

    /**
     * Given letters and a position on the game board, as well as a direction (-1, 0, 1), (-1, 0, 1), look for the given
     * letters on the board, stopping whenever we don't find a match, returning true only if the word exists in that
     * direction
     */
    protected fun seek(letters: CharArray, row: Int, column: Int, direction: Pair<Int, Int>): Boolean {
        if (row < 0 || row >= board.size || column < 0 || column >= board[row].size) {
            return false
        }
        if (letters[0] != board[row][column]) {
            return false
        }

        // TODO: stop as soon as we know we can't build the whole word
        if (letters.size > 1) {
            return seek(letters.copyOfRange(1, letters.size), row + direction.first, column + direction.second, direction)
        }
        // We've made it this far and there's no more characters to look for, that's a match!
        return true
    }
}
