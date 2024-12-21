package calculators

data class Trail(val trailhead:IntPair, var allPossibleTrails: TreeNode<Pair<Int, IntPair>>?) {
    var reachablePeaks = mutableSetOf<IntPair>()
    fun trailScore():Int = allPossibleTrails?.getLeafNodes()?.filter { it.first == 9 }?.size ?: 0
}
val TRAIL_LIST = (0..9).toList()
class TopologicalLavaHikingCalculator: MapCalculator(FILE) {
    companion object {
        const val FILE = "hiking.txt"
    }

    private val intMap:MutableList<MutableList<Int>> = map.map { it.map { char -> char.toString().toInt() }.toMutableList() }.toMutableList()
    private val trails = mutableListOf<Trail>()

    private fun getTrailheads() = intMap.findCoordinates { it == 0 }

    fun calculateTrailheadSum():Int {
        getTrailheads().forEach { head ->
            val trail = Trail(head, intMap.seekAllPathsTo(TRAIL_LIST, head))
            println(trail.allPossibleTrails)
            trails.add(trail)
        }
        return trails.sumOf { it.trailScore() }
    }
}

fun main() {
    println(TopologicalLavaHikingCalculator().calculateTrailheadSum())
}