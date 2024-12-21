package calculators

class StoneCalculator : Calculator(FILE) {
    companion object {
        const val FILE = "stones.txt"
    }

    private var stones: MutableList<Stone> = parseInput()[0].split(" ")
        .map { num -> Stone(num.toLong()) }.toMutableList()

    fun calculateBlinks(blinks: Int): Int {
        repeat(blinks) { blink() }
        return stones.size
    }

    fun blink() {
        stones = stones.flatMap { it.transform() }.toMutableList()
        println(stones)
    }

}

data class Stone(var number:Long) {
    fun transform(): MutableList<Stone> {
        if (number == 0L) {
            number = 1
            return mutableListOf(this)
        }
        val str = number.toString()
        val digits = str.length
        if (digits % 2 == 0) {
            val half = digits / 2
            number = str.substring(0, half).toLong()
            return mutableListOf(this, Stone(str.substring(half).toLong()))
        }
        number *= 2024
        return mutableListOf(this)
    }
}


fun main() {
    println(StoneCalculator().calculateBlinks(25))
}