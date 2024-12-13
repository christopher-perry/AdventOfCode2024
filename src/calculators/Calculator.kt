package calculators

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import parser.InputParser

abstract class Calculator(private val filename: String) {
    companion object {
        private const val PATH = "resources/input/"
    }

    protected val logger: Logger = LogManager.getLogger(this::class.java)

    protected fun parseInput(): List<String> {
        return InputParser.parseInput(PATH + filename)
    }
}