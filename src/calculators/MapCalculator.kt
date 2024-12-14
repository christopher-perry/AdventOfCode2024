package calculators
typealias IntPair = Pair<Int, Int>

/**
 * Superclass containing convenient methods for Calculators that utilize 2D maps
 */
open class MapCalculator(file: String): Calculator(file) {
    protected val map:MutableList<CharArray> = parseInput().map { it.toCharArray() }.toMutableList()

    operator fun IntPair.plus(other: IntPair): IntPair {
        return this.first + other.first to this.second + other.second
    }

    operator fun IntPair.minus(other: IntPair): IntPair {
        return this.first - other.first to this.second - other.second
    }

    operator fun MutableList<CharArray>.get(position: IntPair): Char {
        return this[position.first][position.second]
    }

    operator fun MutableList<CharArray>.set(position: IntPair, value:Char) {
        this[position.first][position.second] = value
    }

    protected fun printMap(displayMap: List<CharArray>) {
        logger.info("\n{}", displayMap.joinToString(separator = "\n") { String(it) })
    }

    protected fun invert(pair:IntPair):IntPair {
        return pair.first * -1 to pair.second * -1
    }
}
