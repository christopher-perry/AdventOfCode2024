package calculators

class StoneCalculator : Calculator(FILE) {
    companion object {
        const val FILE = "stones.txt"
    }

    private var stones: MutableMap<Long, Long> = parseInput()[0].split(" ")
        .associate { it.toLong() to 1L }.toMutableMap()

    fun calculateBlinks(blinks: Int): Long {
        repeat(blinks) { blink() }
        return stones.values.sum()
    }

    private fun blink() {
        val newStones: MutableMap<Long, Long> = mutableMapOf()
        stones.forEach { (number, numberOfStones) ->
            if (number == 0L) {
                newStones.merge(1, numberOfStones)  { a, b -> a + b }
            } else {
                val str = number.toString()
                val digits = str.length
                if (digits % 2 == 0) {
                    val half = digits / 2
                    newStones.merge(str.substring(0, half).toLong(), numberOfStones) { a, b -> a + b }
                    newStones.merge(str.substring(half).toLong(), numberOfStones) { a, b -> a + b }
                } else {
                    newStones.merge(number * 2024, numberOfStones)  { a, b -> a + b }
                }
            }
        }
        stones = newStones
    }
}

fun main() {
    println(StoneCalculator().calculateBlinks(75))
}