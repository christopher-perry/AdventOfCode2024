package calculators

private const val A_COST = 3
private const val B_COST = 1

data class ClawMachine(
    val buttonA: IntPair,
    val buttonB: IntPair,
    val prize: IntPair
) {

    data class ButtonCombo(val buttonAPresses: Long, val buttonBPresses: Long) {
        fun cost() = A_COST * buttonAPresses + B_COST * buttonBPresses
    }

    var winningCombinations = mutableSetOf<ButtonCombo>()

    fun calculateWinningCombos() {
        val maxBPresses : Int = min((prize / buttonB))
        for (bPresses in maxBPresses.downTo(0)) {
            val bValue = buttonB * bPresses
            val target = prize - bValue
            // To be a winning combo, target must be divisible by aX and aY
            if (target.mod(buttonA) == 0 to 0) {
                val aPresses = (target / buttonA)
                // If the remainder (x,y) is not ==, it is not evenly divisible, no winning # presses here.
                if (aPresses.first == aPresses.second) {
                    winningCombinations.add(ButtonCombo(aPresses.first.toLong(), bPresses.toLong()))
                }
            }
        }
    }

    fun cheapestCombo() = winningCombinations.minOfOrNull { it.cost() } ?: 0
}

open class ClawMachineReader : Calculator(FILE) {
    companion object {
        val FILE = "claw.txt"
    }

    fun parseClawMachines(): List<ClawMachine> {
        val clawMachines = mutableListOf<ClawMachine>()
        val lines = parseInput()

        var buttonA: IntPair? = null
        var buttonB: IntPair? = null
        var prize: IntPair? = null

        for (line in lines) {
            if (line.startsWith("Button A:")) {
                buttonA = parseIntPair(line.substringAfter("Button A: "))
            } else if (line.startsWith("Button B:")) {
                buttonB = parseIntPair(line.substringAfter("Button B: "))
            } else if (line.startsWith("Prize:")) {
                prize = parseIntPair(line.substringAfter("Prize: "))
            }

            if (buttonA != null && buttonB != null && prize != null) {
                clawMachines.add(ClawMachine(buttonA, buttonB, prize))
                buttonA = null
                buttonB = null
                prize = null
            }
        }
        return clawMachines
    }

    private fun parseIntPair(pointString: String): IntPair {
        val xString = pointString.substringAfter("X").substringBefore(",").trim()
        val yString = pointString.substringAfter("Y").trim()

        val x = if (xString.startsWith("+")) {
            xString.substringAfter("+").toInt()
        } else {
            xString.substringAfter("=").toInt()
        }
        val y = if (yString.startsWith("+")) {
            yString.substringAfter("+").toInt()
        } else {
            yString.substringAfter("=").toInt()
        }

        return IntPair(x, y)
    }
}

fun main() {
    val clawMachines = ClawMachineReader().parseClawMachines()
    clawMachines.forEach {
        it.calculateWinningCombos()
        println("$it: ${it.winningCombinations}")
    }
    println(clawMachines.sumOf { it.cheapestCombo() })
}