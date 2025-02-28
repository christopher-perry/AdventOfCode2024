package calculators

class GardenCalculator() : MapCalculator(FILE) {
    companion object {
        const val FILE = "garden.txt"
    }

    private var regions: List<Region>
    private val plants = mutableMapOf<Char, MutableList<IntPair>>()

    init {
        map.forEachIndexed { row, chars ->
            chars.forEachIndexed { column, plant ->
                plants.getOrPut(plant) { mutableListOf() }.add(row to column)
            }
        }
        regions = plants.flatMap { (plant, coordinates) ->
            Region.divideIntoRegions(plant, coordinates)
        }
    }

    fun calculateFenceCost(): Int {
        println(regions.joinToString("\n"))
        return regions.sumOf { it.fenceCost() }
    }

    fun calculateFenceCostBulk(): Int {
        println(regions.joinToString("\n"))
        return regions.sumOf { it.fenceCostBulk() }
    }
}

data class Region(val plant: Char, var coordinates: MutableList<IntPair>) {
    companion object {
        fun divideIntoRegions(plant: Char, coordinates: MutableList<IntPair>): List<Region> {
            val regions = mutableListOf<Region>()
            val visited = mutableListOf<IntPair>()

            coordinates.forEach { coordinate ->
                if (coordinate !in visited) {
                    val newRegion = Region(plant, findAllRegionCoordinates(coordinate, coordinates.toList(), visited))
                    regions.add(newRegion)
                }
            }
            return regions
        }

        /**
         * Given a starting point, a set of valid plant plots of the given type, and a set of all coordinates allocated
         * to another region, find and return the coordinates in this new region.
         */
        private fun findAllRegionCoordinates(start: IntPair, plantPlots: List<IntPair>, visited: MutableList<IntPair>):
                MutableList<IntPair> {
            val regionCoordinates = mutableListOf<IntPair>()
            val toVisit = mutableListOf(start)
            while (toVisit.isNotEmpty()) {
                val current = toVisit.removeLast()
                if (current !in visited) {
                    visited.add(current)
                    regionCoordinates.add(current)
                    ALL_DIRECTIONS.map { current + it }
                        .filter { it in plantPlots}
                        .forEach { toVisit.add(it) }
                }
            }
            return regionCoordinates
        }
    }

    private fun area() = coordinates.size

    private fun perimeter() = coordinates.sumOf { coordinate ->
        ALL_DIRECTIONS.filter { (coordinate + it) !in coordinates }.size
    }

    private fun edges(): Int {
        val directionsToNeighbors = mutableMapOf<Direction, MutableList<IntPair>>()
        coordinates.sortWith(compareBy({ it.first }, { it.second }))
        coordinates.forEach{ coordinate ->
            ALL_DIRECTIONS.forEach { direction ->
                val test = (coordinate + direction)
                if (test !in coordinates) {
                    val dirSet = directionsToNeighbors.getOrPut(direction) { mutableListOf() }
                    dirSet.add(test)
                    val prevEdge = test + direction.perpendicularCheck()
                    dirSet.remove(prevEdge)
                }
            }
        }
        return directionsToNeighbors.values.sumOf { it.size }
    }

    fun fenceCost() = area() * perimeter()
    fun fenceCostBulk() = area() * edges()

    override fun toString(): String {
        return "${plant}: " +
//                "${area()} ${perimeter()} = ${fenceCost()}" +
                "${area()} ${edges()} ${fenceCostBulk()}"
    }
}

fun main() {
//    println(GardenCalculator().calculateFenceCost())
    println(GardenCalculator().calculateFenceCostBulk())
}