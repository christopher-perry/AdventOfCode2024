package calculators

typealias Matrix<T> = List<MutableList<T>>
data class TreeNode<T>(val value: T, val children: MutableList<TreeNode<T>> = mutableListOf()) {
    fun getLeafNodes():List<T> {
        if (children.isEmpty()) return mutableListOf(value)

        return children.flatMap { it.getLeafNodes() }
    }
}

val ALL_DIRECTIONS = setOf(IntPair(0, 1), IntPair(1, 0), IntPair(0, -1), IntPair(-1, 0)) // > v < ^

private fun <T> Matrix<T>.onBoard(coordinates: Pair<Int, Int>) =
    coordinates.first in indices && coordinates.second in this[coordinates.first].indices

operator fun <T> Matrix<T>.get(position: IntPair): T {
    return this[position.first][position.second]
}

operator fun <T> Matrix<T>.set(position: IntPair, value:T) {
    this[position.first][position.second] = value
}

fun <T> Matrix<T>.findCoordinates(predicate: (T) -> Boolean): List<IntPair> {
    val coordinates = mutableListOf<IntPair>()
    for (row in this.indices) {
        for (column in this[row].indices) {
            if (predicate(this[row][column])) {
                coordinates.add(row to column)
            }
        }
    }
    return coordinates
}

fun <T> Matrix<T>.seekUniqueCoordinates(pattern: List<T>, coordinates:IntPair): Set<IntPair> {
    if (pattern.isEmpty() || !this.onBoard(coordinates) || pattern[0] != this[coordinates]) {
        return emptySet()
    }

    if (pattern.size > 1) {
        return ALL_DIRECTIONS.flatMap {
            seekUniqueCoordinates(pattern.subList(1, pattern.size), coordinates + it)
        }.toSet()
    }
    // We've made it this far and there's no more characters to look for, that's a match!
    return setOf(coordinates)
}

fun <T> Matrix<T>.seekAllPathsTo(pattern: List<T>, coordinates: IntPair): TreeNode<Pair<T, IntPair>>? {
    if (pattern.isEmpty() || !this.onBoard(coordinates) || pattern[0] != this[coordinates]) {
        return null
    }

    val node = TreeNode(this[coordinates] to coordinates)
    if (pattern.size > 1) {
        ALL_DIRECTIONS.forEach { direction ->
            seekAllPathsTo(pattern.subList(1, pattern.size), coordinates + direction)?.let { node.children.add(it) }
        }
    }
    return node
}

fun <T> Matrix<T>.seek(pattern:List<T>, coordinates: IntPair, direction: IntPair): Boolean {
    if (this.onBoard(coordinates) || pattern.isEmpty() || pattern[0] != this[coordinates]) {
        return false
    }

    if (pattern.size > 1) {
        return this.seek(pattern.subList(1, pattern.size), coordinates + direction, direction)
    }
    // We've made it this far and there's no more characters to look for, that's a match!
    return true
}