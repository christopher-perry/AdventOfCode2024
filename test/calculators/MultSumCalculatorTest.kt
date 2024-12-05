package calculators

import org.apache.logging.log4j.LogManager
import kotlin.test.Test

class MultSumCalculatorTest {
    val Logger = LogManager.getLogger()
    private final val testString = "asdfasdfmul(456,23)asdfasmul(3,5)"

    @Test
    fun testRegex() {
        val result = MultSumCalculator().multRegex.findAll(testString)
        val l = mutableListOf<String>()
        result.forEach { shit ->
            Logger.info(shit.groups)
            val (first, second) = shit.destructured
            println("$first, $second")
        }
        println(l)
    }
}