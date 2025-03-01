package calculators

fun ClawMachine.calculateWinningCombosBigPrize() {
    val aX = buttonA.first.toLong()
    val aY = buttonA.second.toLong()
    val bX = buttonB.first.toLong()
    val bY = buttonB.second.toLong()
    val pX = prize.first + 10000000000000L
    val pY = prize.second + 10000000000000L

    // Check for integer solutions to: pX = aX * aPresses + bX * bPresses, pY = aY * aPresses + bY * bPresses
    val determinant = aX * bY - aY * bX

    if (determinant != 0L) {
        val aPressesNumerator = pX * bY - pY * bX
        val bPressesNumerator = aX * pY - aY * pX

        if (aPressesNumerator % determinant == 0L && bPressesNumerator % determinant == 0L) {
            val aPresses = aPressesNumerator / determinant
            val bPresses = bPressesNumerator / determinant

            if (aPresses >= 0L && bPresses >= 0L) {
                winningCombinations.add(ClawMachine.ButtonCombo(aPresses, bPresses))
            }
        }
    }
}

fun main() {
    val clawMachines = ClawMachineReader().parseClawMachines()
    // This is majorly inefficient because already calling calculateWinningCombos in the init and this just overrides
    // the results, but I don't care enough to fix it
    clawMachines.forEach {
        it.calculateWinningCombosBigPrize()
        println("$it: ${it.winningCombinations}") // Won't be more than one anymore, oh well.
    }
    println(clawMachines.sumOf { it.cheapestCombo() })
}