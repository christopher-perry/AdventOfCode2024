package calculators

class GuardPathCalculator: MapCalculator(FILE) {
    companion object {
        private val FILE = "map.txt"
        private val TRAVERSED_CHARS = setOf('-', '+', '|', 'O')
        // Get the traversal direction based on the guard token
        private val GUARD_MAPPER = mapOf('^' to (-1 to 0), '>' to (0 to 1),
            'v' to (1 to 0), '<' to (0 to -1))
        private val DIRECTION_TO_GUARD_MAPPER = GUARD_MAPPER.map { entry -> entry.value to entry.key }.toMap()
    }

    private val maxSize = map.size * map[0].size
    private var possibleLoops = 0

    fun countTilesVisited():Int {
        printMap(map)
        val (initialPosition, direction) = findInitialPositionAndDirection()
        navigateMap(initialPosition, direction)
        logger.info("Possible infinite loops: {}", possibleLoops)
        logger.info("Total O's :{}", map.sumOf { chars -> chars.count {char -> char == 'O' } })
        return sumAllTraversedTiles()
    }

    private fun sumAllTraversedTiles(): Int {
        return map.sumOf { chars ->
            chars.count { char -> char in TRAVERSED_CHARS }
        }
    }

    private fun findInitialPositionAndDirection():Pair<IntPair, IntPair> {
        map.forEachIndexed { row, spaces ->
            spaces.forEachIndexed { column, char ->
                val direction: IntPair? = GUARD_MAPPER[char]
                if (direction != null) {
                    return row to column to direction
                }
            }
        }
        throw IllegalStateException("No guard token found in map!")
    }

    private fun navigateMap(position:IntPair, direction:IntPair, previousToken:Char? = null): IntPair {
        var guard = DIRECTION_TO_GUARD_MAPPER[direction]!!
        var nextDirection = direction
        val newPosition = position + direction

        // As soon as the guard leaves, terminate; we're done
        if (guardHasLeft(newPosition)) {
            travelOver(position, nextDirection, map[position])
            return -1 to -1
        }

        // If we are about to hit an obstacle, simply rotate to the next direction. Then move.
        if (map[newPosition] == '#') {
            guard = getNextGuardToken(guard)
            nextDirection = GUARD_MAPPER[guard]!!
            return navigateMap(position, nextDirection, '+')
        }

        val loop = if(previousToken != null) checkIfAnObstacleCanCauseALoop(map, newPosition, nextDirection) else 0
        possibleLoops += loop
        // If we've made it this far, nextDirection is a valid move and we're going straight.
        // mark currentPosition based on its previous token (because currently it is guard)
        travelOver(position, nextDirection, previousToken)
        val nextToken = if (loop > 0) 'O' else map[newPosition]
        map[newPosition] = guard
        printMap(map)
        return navigateMap(newPosition, nextDirection, nextToken)
    }

    /**
     * Look at what would happen if we dropped an obstacle at the new position.
     * If we would then proceed forward to another obstacle, then another, then another at the same column as
     * newPosition, we can create an infinite loop.  Increment the counter.
     */
    private fun checkIfAnObstacleCanCauseALoop(displayMap:List<CharArray>, startingPoint: IntPair, nextDirection: IntPair): Int {
        var tempCursor = startingPoint
        var direction = nextDirection
        var obstacle = startingPoint + direction
        var timesCollided = 0 // track how many obstacles we hit so we can stop if we can't return back to our starting point
        // Initial "Collision" to the obstacle we plop down
        direction = getNextDirection(direction)
        while (true) {
            val nextPosition = tempCursor + direction
            if (nextPosition == startingPoint) {
                return 1
            }
            if (guardHasLeft(nextPosition)) {
                return 0
            }
            if (map[nextPosition] == '#' || nextPosition == obstacle) {
                timesCollided ++
                direction = getNextDirection(direction)
            } else {
                tempCursor = nextPosition
            }
            if (timesCollided > 100) {
                return 0
            }
        }
    }

    // Replace the tile at the given position with the token determined by the given direction
    private fun travelOver(position: IntPair, direction: IntPair, currentToken: Char ?= ' ') {
        var newToken = if (direction.first != 0) '|' else '-'
        if (currentToken == '+' || setOf('|', '-').containsAll(setOf(newToken, currentToken))) {
            newToken = '+'
        }
        if (currentToken == 'O') {
            newToken = 'O'
        }
        map[position] = newToken
    }

    private fun guardHasLeft(position: IntPair): Boolean {
        return !(position.first in 0..map.size - 1 && position.second in 0 .. map[position.first].size - 1)
    }

    private fun getNextGuardToken(currentGuardToken: Char): Char {
        val keys = GUARD_MAPPER.keys.toList()
        val currentIndex = keys.indexOf(currentGuardToken)
        val nextIndex = (currentIndex + 1) % keys.size
        return keys[nextIndex]
    }

    private fun getNextDirection(direction: IntPair): IntPair {
        val directions = DIRECTION_TO_GUARD_MAPPER.keys.toList()
        val currentIndex = directions.indexOf(direction)
        val nextIndex = (currentIndex + 1) % directions.size
        return directions[nextIndex]
    }
}