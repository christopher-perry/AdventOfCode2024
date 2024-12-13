package calculators

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import parser.InputParser.sortedLists
import parser.LeftRightList
import kotlin.math.abs

class DistanceCalculator @JvmOverloads constructor(lists: LeftRightList<Int> = sortedLists) {
    private val left: List<Int> = lists.left
    private val right: List<Int> = lists.right

    val distance: Int
        get() {
            var distance = 0
            for (i in left.indices) {
                val l = left[i]
                val r = right[i]
                val difference = abs((l - r).toDouble()).toInt()
                LOG.trace("{} {}: {}", l, r, distance)
                distance += difference
            }
            return distance
        }

    companion object {
        private val LOG: Logger = LogManager.getLogger(
            DistanceCalculator::class.java
        )
    }
}
