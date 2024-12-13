package parser

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.function.BiFunction
import java.util.function.Consumer

object InputParser {
    private const val PATH = "resources/input/input.txt"
    private val LOG: Logger = LogManager.getLogger(InputParser::class.java)

    @JvmStatic
    @JvmOverloads
    fun parseInput(path: String = PATH): List<String> {
        val result: MutableList<String> = ArrayList()
        try {
            BufferedReader(FileReader(path)).use { reader ->
                var line: String
                while ((reader.readLine().also { line = it }) != null) {
                    result.add(line)
                    LOG.trace(line)
                }
            }
        } catch (ioe: IOException) {
            LOG.error("Exception in reading file: ", ioe)
        }
        return result
    }

    @JvmStatic
    val sortedLists: LeftRightList<Int>
        get() {
            val result = parseInput()
            val left: MutableList<Int> = ArrayList()
            val right: MutableList<Int> = ArrayList()
            result.forEach { line: String ->
                val pair =
                    line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                left.add(pair[0].toInt())
                right.add(pair[1].toInt())
            }
            return LeftRightList(left.stream().sorted().toList(), right.stream().sorted().toList())
        }

    @JvmStatic
    val frequencyMaps: LeftRightFrequencyMap<Int>
        get() {
            val result = parseInput()
            val left: MutableMap<Int, Int> = HashMap()
            val right: MutableMap<Int, Int> = HashMap()
            result.forEach { line: String ->
                try {
                    val pair = line.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val l = pair[0].toInt()
                    left.compute(l, incrementElseOne)
                    val r = pair[1].toInt()
                    right.compute(r, incrementElseOne)
                } catch (e: NumberFormatException) {
                    LOG.error("Line could not be parsed into integers: {}", line, e)
                }
            }
            return LeftRightFrequencyMap(left, right)
        }

    private val incrementElseOne = BiFunction { k: Int?, v: Int? -> if (v == null) 1 else v + 1 }
}
