package calculators

import kotlin.math.min

typealias IntPair = Pair<Int, Int>

/**
 * Superclass containing convenient methods for Calculators that utilize 2D maps
 */
open class MapCalculator(file: String): Calculator(file) {
    protected val map:MutableList<CharArray> = parseInput().map { it.toCharArray() }.toMutableList()

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

operator fun IntPair.plus(other: IntPair): IntPair {
    return this.first + other.first to this.second + other.second
}

operator fun IntPair.minus(other: IntPair): IntPair {
    return this.first - other.first to this.second - other.second
}

operator fun IntPair.times(other: Int): IntPair {
    return this.first * other to this.second * other
}

operator fun IntPair.div(other: IntPair): IntPair {
    return this.first / other.first to this.second / other.second
}

fun min(pair: Pair<Int, Int>) = min(pair.first, pair.second)

fun IntPair.mod(other: IntPair): IntPair {
    val newFirst = if (other.first == 0) 0 else this.first.mod(other.first) 
    val newSecond = if (other.second == 0) 0 else this.second.mod(other.second) 
    return newFirst to newSecond
}
