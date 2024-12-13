package calculators

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class MasXSearchCalculator @JvmOverloads constructor(path: String? = PATH) : WordSearchCalculator(path) {
    companion object {
        const val PATH = "resources/input/wordsearch.txt"
        private val LOGGER: Logger = LogManager.getLogger(WordSearchCalculator)
    }
    override val wordbank = listOf("MAS".toCharArray(), "SAM".toCharArray())

    override fun search(row: Int, column: Int, word: CharArray): Int {
        val verticalMirror = row + 2
        val horizontalMirror = column + 2
        if (row >= board.size || verticalMirror >= board.size ||
            column >= board[row].size || horizontalMirror >= board[row].size) {
            return 0
        }

        /**
         * only check down and to the right.  Up and to the left/up and to the right, and down and to the left will have
         * already been covered since we're moving top left to bottom right.
         * M   M   or  M  S  or S   M or S   S
         *   A           A        A        A
         * S   S       M  S    S    M    M   M
         */
        if (seek(word, row, column, 1 to 1)) {
            if (seek(word, verticalMirror, column, -1 to 1) || seek(word, row, verticalMirror, 1 to -1)) {
                return 1
            }
        }
        return 0
    }
}
