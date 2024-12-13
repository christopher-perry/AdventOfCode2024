package calculators

import org.apache.logging.log4j.LogManager
import parser.InputParser

class PrintInstructionsCalculator {
    companion object {
        private val Logger = LogManager.getLogger(PrintInstructionsCalculator)
        private const val PATH = "resources/input/print_queue.txt"
        private val printInstruction = Regex("(\\d+)\\|(\\d+)")
    }

    private val rules:MutableMap<Int, MutableList<Int>> = mutableMapOf()
    private val updates:MutableList<List<Int>> = mutableListOf()

    init {
        parsePrintOrdering(PATH)
    }

    fun getCount():Int {
        return updates.filter { satisfiesAllRules(it) }
            .sumOf { update -> update[update.size / 2] }
    }

    private fun satisfiesAllRules(update: List<Int>):Boolean {
        update.forEachIndexed { index, num ->
            val forbiddenNumbers = rules[num]
            if (forbiddenNumbers != null) {
                if (update.subList(0, index).any { forbiddenNumbers.contains(it) }) {
                    Logger.info("Update {} has been disqualified because rule for {} (must appear before {}) was violated.",
                        update, num, forbiddenNumbers)
                    return false
                }
            }
        }
        return true
    }

    private fun parsePrintOrdering(path: String) {
        InputParser.parseInput(path).forEach { line: String ->
            val match = printInstruction.find(line)
            if (match != null) {
                val (key, value) = match.destructured
                rules.getOrPut(key.toInt()) { mutableListOf() }.add(value.toInt())
            } else {
                if (line.isNotEmpty()) {
                    updates.add(line.split(",").map { it.toInt() })
                }
            }
        }
        Logger.info("Rules: {}\nUpdates:{}", rules.entries.joinToString(separator = "\n"), updates.joinToString("\n"))
    }
}