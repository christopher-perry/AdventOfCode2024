package calculators

class ResonanceCalculator: MapCalculator(FILE) {
    companion object {
        const val FILE = "resonance.txt"
    }

    private val antiNodeMap = map.map { row -> CharArray(row.size) { '.' } }.toMutableList()
    private var antennaCoordinates = mutableMapOf<Char, MutableList<IntPair>>()

    fun calculateResonanceScore():Int {
        logger.info(printMap(map))
        map.forEachIndexed { row, tiles ->
            tiles.forEachIndexed { column, tile ->
                if (tile != '.') {
                    antennaCoordinates.getOrPut(tile) { mutableListOf() }.add(row to column)
                }
            }
        }
        antennaCoordinates.filter { entry -> entry.value.size > 1 }
            .forEach { (token, coordinates) ->
                coordinates.forEachIndexed { index, coordinate ->
                    for (i in 0..<coordinates.size) {
                        if (i != index) {
                            val distance = calculateDistance(coordinate, coordinates[i])
                            var destination = coordinate + distance
                            while (isSafe(destination)) {
                                antiNodeMap[destination] = token
                                destination += distance
                            }
                            destination = coordinate - distance
                            while (isSafe(destination)) {
                                antiNodeMap[destination] = token
                                destination -= distance
                            }
//                            val possibleAntinode = mirrorCoordinates(coordinate, coordinates[i])
                        }
                    }
                }
            }
        logger.info(printMap(antiNodeMap))
        return antiNodeMap.sumOf { it.count { c -> c != '.' } }
    }

    private fun isSafe(coordinates:IntPair):Boolean {
        return coordinates.first in 0..<map.size && coordinates.second in 0..<map[coordinates.first].size
    }

    private fun mirrorCoordinates(fromPosition:IntPair, toPosition: IntPair): IntPair {
        val distance = fromPosition - toPosition
        return fromPosition + distance
    }

    private fun calculateDistance(fromPosition:IntPair, toPosition: IntPair): IntPair {
        return fromPosition - toPosition
    }
}

fun main() {
    println(ResonanceCalculator().calculateResonanceScore())
}