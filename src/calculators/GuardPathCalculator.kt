package calculators

import jogamp.opengl.GLContextShareSet.printMap

class GuardPathCalculator: Calculator(FILE) {
    companion object {
        private val FILE = "map.txt"
        // Get the traversal direction based on the guard token
        private val GUARD_MAPPER = mapOf('^' to (-1 to 0), '>' to (0 to 1),
            'v' to (1 to 0), '<' to (0 to -1))
    }

    private val map:List<CharArray> = parseInput().map { it.toCharArray() }

    fun countTilesVisited():Int {
        printMap()
        val (initialPosition, direction) = findInitialPositionAndDirection()
        navigateMap(initialPosition, direction)
        return sumAllXTiles()
    }

    private fun sumAllXTiles(): Int {
        return map.sumOf { chars ->
            chars.count { char -> 'X' == char }
        }
    }

    private fun findInitialPositionAndDirection():Pair<Pair<Int, Int>, Pair<Int,Int>> {
        map.forEachIndexed { row, spaces ->
            spaces.forEachIndexed { column, char ->
                val direction: Pair<Int, Int>? = GUARD_MAPPER[char]
                if (direction != null) {
                    return row to column to direction
                }
            }
        }
        throw IllegalStateException("No guard token found in map!")
    }

    private fun navigateMap(position:Pair<Int,Int>, direction:Pair<Int, Int>): Pair<Int, Int> {
        var guard = map[position.first][position.second]
        var nextDirection = direction
        val newPosition = position + direction

        // As soon as the guard leaves, terminate; we're done
        if (guardHasLeft(newPosition)) {
            map[position.first][position.second] = 'X'
            return -1 to -1
        }

        // If we are about to hit an obstacle, simply rotate to the next direction. Then move.
        if (map[newPosition.first][newPosition.second] == '#') {
            guard = getNextGuardToken(guard)
            map[position.first][position.second] = guard
            nextDirection = GUARD_MAPPER[guard]!!
            return navigateMap(position, nextDirection)
        }

        // If we've made it this far, nextDirection is a valid move and we're going straight
        map[position.first][position.second] = 'X'
        map[newPosition.first][newPosition.second] = guard
        printMap()
        return navigateMap(newPosition, direction)
    }

    private fun guardHasLeft(position: Pair<Int, Int>): Boolean {
        return !(position.first in 0..map.size - 1 && position.second in 0 .. map[position.first].size - 1)
    }

    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
        return this.first + other.first to this.second + other.second
    }

    fun getNextGuardToken(currentGuardToken: Char): Char {
        val keys = GUARD_MAPPER.keys.toList()
        val currentIndex = keys.indexOf(currentGuardToken)
        val nextIndex = (currentIndex + 1) % keys.size
        return keys[nextIndex]
    }

    fun printMap() {
        logger.info("\n{}", map.joinToString(separator = "\n") { String(it) })
    }

}