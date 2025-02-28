package calculators

class GardenCalculator : MapCalculator(FILE) {
    companion object {
        const val FILE = "garden.txt"
    }

    private val plants = mutableMapOf<Char, MutableList<IntPair>>()

    fun calculateFenceCost(): Int {
        map.forEachIndexed { row, chars ->
            chars.forEachIndexed { column, plant ->
                plants.getOrPut(plant) { mutableListOf() }.add(row to column)
            }
        }
        val regions = plants.flatMap { (plant, coordinates) ->
            Region.divideIntoRegions(plant, coordinates)
        }
        println(regions.joinToString("\n"))
        return regions.sumOf { it.fenceCost() }
    }
}

data class Region(val plant: Char, var coordinates: MutableSet<IntPair>) {
    companion object {
        fun divideIntoRegions(plant: Char, coordinates: MutableList<IntPair>): List<Region> {
            val regions = mutableListOf<Region>()
            val visited = mutableSetOf<IntPair>()

            coordinates.forEach { coordinate ->
                if (coordinate !in visited) {
                    val newRegion = Region(plant, findAllRegionCoordinates(coordinate, coordinates.toSet(), visited))
                    regions.add(newRegion)
                }
            }
            return regions
        }

        /**
         * Given a starting point, a set of valid plant plots of the given type, and a set of all coordinates allocated
         * to another region, find and return the coordinates in this new region.
         */
        private fun findAllRegionCoordinates(start: IntPair, plantPlots: Set<IntPair>, visited: MutableSet<IntPair>):
                MutableSet<IntPair> {
            val regionCoordinates = mutableSetOf<IntPair>()
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
    private fun edges() = coordinates.sumOf { coordinate ->
        ALL_DIRECTIONS.count { (coordinate + it) !in coordinates }
    }

    fun fenceCost() = area() * perimeter()

    override fun toString(): String {
        return "${plant}: ${area()} ${perimeter()} = ${fenceCost()}"
    }
}

fun main() {
    println(GardenCalculator().calculateFenceCost())
}