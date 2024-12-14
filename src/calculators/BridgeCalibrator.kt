package calculators

typealias LongPair = Pair<Long, Long>
class BridgeCalibrator: Calculator(FILE) {
    companion object {
        const val FILE = "bridge.txt"
        val OPERATIONS = setOf<(LongPair) -> Long> (
            { l -> sum(l) },
            { l -> mult(l) }
        )
    }
    private val equations:List<String> = parseInput()

    fun calculateCalibrations():Long {
        return equations.sumOf {
            parseEquation(it)
        }
    }

    private fun parseEquation(equation:String):Long {
        val (t, p) = equation.split(": ")
        val total = t.toLong()
        val parts = p.split(" ").map { it.toLong() }
        logger.info("Total: {}, Parts: {}", total, parts)
        if (testNumbers(total, parts)) {
            logger.info("Was possible")
            return total
        }
        logger.info("NOT POSSIBLE")
        return  0
    }

    // 187418: 8 1 32 1 3 7 700 2 6 8 9 9
    private fun testNumbers(total:Long, operands: List<Long>):Boolean {
        if (operands[0] > total) {
            // right now we're only increasing in size, short circuit asap
            return false
        }
        if (operands.size < 2) {
            return total == operands[0]
        }
        OPERATIONS.forEach {
            val sum = it(operands[0] to operands[1])
            val clonedList = operands.subList(2, operands.size).toMutableList()
            clonedList.add(0, sum)
            if (testNumbers(total, clonedList)) {
                return true // As soon as we find 1 possibility, return
            }
        }
        return false
    }
}

private fun sum(numbers: Pair<Long, Long>):Long {
    return numbers.first + numbers.second
}

private fun mult(numbers: Pair<Long, Long>):Long {
    return numbers.first * numbers.second
}

fun main() {
    println(BridgeCalibrator().calculateCalibrations())
}