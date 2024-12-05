package parser

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReportTest {
    private val safeAsc:List<Int> = listOf(1, 2, 3, 6, 9)
    private val safeAscDupeAtStart:List<Int> = listOf(1, 1, 2, 3, 6, 9)
    private val safeDesc:List<Int> = safeAsc.reversed()
    private val safeDescDupeAtStart:List<Int> = listOf(9, 9, 8, 7, 6, 4)
    private val lilBastard:List<Int> = listOf(43, 40, 41, 44, 45, 46, 48, 51)
    private val misleading:List<Int> = listOf(65, 66, 63, 60)
    private val problemAtEnd:List<Int> = safeAsc.toMutableList().apply { add(16) }
    private val problemAtStart:List<Int> = safeAsc.toMutableList().apply { add(0, 10) }
    private val problemSwitchDescToAsc:List<Int> = listOf(5, 3, 6, 7, 8, 9)
    private val problemSwitchAscToDesc:List<Int> = listOf(9, 10, 7, 6, 3)
    private val problemSwitchAscToDesc2:List<Int> = listOf(30, 6, 7, 8, 9)
    private val badguy:List<Int> = listOf(1,3,5,4,8)
    private val twoOnly:List<Int> = listOf(1, 3)
    private val one:List<Int> = listOf(1)
    private val twoProblemsAsc:List<Int> = listOf(1, 5, 9, 10, 11, 12)

    @Test
    fun isSafe() {
        listOf(safeAsc, safeAscDupeAtStart, safeDesc, safeDescDupeAtStart, problemAtEnd, problemAtStart, problemSwitchDescToAsc,
            problemSwitchAscToDesc, problemSwitchAscToDesc2, twoOnly, one, misleading, badguy, lilBastard).forEach {
                assertTrue(Report(it).isSafe, "$it was unsafe!")
            }
        listOf(twoProblemsAsc).forEach {
            assertFalse(Report(it).isSafe, "$it was safe!")
        }
    }

    @Test
    fun limitSwitch() {
        assertTrue(Report(lilBastard).isSafe)
    }

 }

