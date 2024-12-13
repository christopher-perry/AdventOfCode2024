package calculators

import org.apache.logging.log4j.LogManager

class PrintInstructionsCalculator:Calculator(FILE) {
    companion object {
        private val Logger = LogManager.getLogger(PrintInstructionsCalculator)
        private const val FILE = "print_queue.txt"
        private val printInstruction = Regex("(\\d+)\\|(\\d+)")
    }

    private val rules:MutableMap<Int, MutableList<Int>> = mutableMapOf()
    private val updates:MutableList<List<Int>> = mutableListOf()

    init {
        parsePrintOrdering()
    }

    fun getCount():Int {
        return updates.filter { satisfiesAllRules(it) }
            .sumOf { update -> update[update.size / 2] }
    }

    fun countBadOnes():Int {
        return updates.filter { !satisfiesAllRules(it) }
            .sumOf { update -> sortUpdate(update)[update.size / 2] }
    }

    // Dumb sort 'cause I can't be arsed
    private fun sortUpdate(update: List<Int>):List<Int> {
        val sortedUpdate = update.toMutableList()
        update.forEachIndexed { index, num ->
            val forbiddenNumbers = rules[num]
            if (forbiddenNumbers != null) {
                val newIndex = update.subList(0, index).indexOfFirst { forbiddenNumbers.contains(it) }
                if (newIndex!= -1) {
                    sortedUpdate.removeAt(index)
                    sortedUpdate.add(newIndex, num)
                }
            }
        }
        if (!satisfiesAllRules(sortedUpdate)) {
            return sortUpdate(sortedUpdate)
        }
        return sortedUpdate
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

    private fun parsePrintOrdering() {
        parseInput().forEach { line: String ->
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