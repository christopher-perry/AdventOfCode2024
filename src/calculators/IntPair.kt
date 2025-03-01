package calculators

typealias IntPair = Pair<Int, Int>

operator fun IntPair.plus(other: IntPair): IntPair {
    return this.first + other.first to this.second + other.second
}

operator fun IntPair.minus(other: IntPair): IntPair {
    return this.first - other.first to this.second - other.second
}

operator fun IntPair.times(other: Int): IntPair {
    return this.first * other to this.second * other
}

operator fun IntPair.times(other: Long): LongPair {
    return this.first * other to this.second * other
}

operator fun IntPair.div(other: IntPair): IntPair {
    return this.first / other.first to this.second / other.second
}

fun min(pair: Pair<Int, Int>) = kotlin.math.min(pair.first, pair.second)

fun IntPair.mod(other: IntPair): IntPair {
    val newFirst = if (other.first == 0) 0 else this.first.mod(other.first)
    val newSecond = if (other.second == 0) 0 else this.second.mod(other.second)
    return newFirst to newSecond
}