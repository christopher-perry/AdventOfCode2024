package calculators

data class Trail(val trailhead:IntPair) {
    var reachablePeaks = mutableSetOf<IntPair>()
    fun trailScore():Int = reachablePeaks.size
}
val TRAIL_LIST = (0..9).toList()
class TopologicalLavaHikingCalculator: MapCalculator(FILE) {
    companion object {
        const val FILE = "hiking.txt"
    }

    private val intMap:MutableList<MutableList<Int>> = map.map { it.map { char -> char.toString().toInt() }.toMutableList() }.toMutableList()
    private val trails = mutableListOf<Trail>()

    private fun getTrailheads() = intMap.findCoordinates { it == 0 }.map { Trail(it) }

    fun calculateTrailheadSum():Int {
        getTrailheads().forEach { trail ->
            trail.reachablePeaks.addAll(intMap.seekUniqueCoordinates(TRAIL_LIST, trail.trailhead))
            println(trail.reachablePeaks)
            trails.add(trail)
        }
        return trails.sumOf { it.trailScore() }
    }
}

fun main() {
    println(TopologicalLavaHikingCalculator().calculateTrailheadSum())
}