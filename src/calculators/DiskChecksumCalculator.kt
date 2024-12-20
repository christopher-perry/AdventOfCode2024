package calculators

fun main() {
    println(DiskChecksumCalculator().calculateCheckSum())
}

open class DataFile (val id:Int, fileSize: Int, cursor: Int) {
    val indices:MutableList<Int> = (cursor until cursor + fileSize).toMutableList()
    open fun checksum():Long = indices.sumOf { it * id.toLong() }
    override fun toString() = "File(${id}: ${indices.joinToString(", ")} | ${checksum()})"

    /**
     * Swap emptySpace with the highest indices in this DataFile. Returns -1 if this file used up all of the free space
     * and still needs moved, 0 if all free space was used AND the freeSpace was exhausted, 1 if this file is completely
     * relocated and freeSpace still has room for more.
     * 95564 95565 95567 95568 3 4
     * 8 9
      */
    fun fillIndices(freeSpace: EmptySpace): Int {
        val freeSpaceCursor = freeSpace.indices.withIndex().iterator()
        if (!freeSpaceCursor.hasNext()) {
            return -1
        }
        var freeSpaceIndex = freeSpace.indices.first()
        val startingPoint = greatestIndexPositionLessThanGiven(freeSpaceIndex)
        if (startingPoint == -1) return 1

        val indicesIterator = indices.subList(0, startingPoint + 1).withIndex().reversed().iterator()

        var indexEntry = indicesIterator.next()
        while (indexEntry.value > freeSpaceIndex) {
            indices[indexEntry.index] = freeSpaceIndex
            freeSpace.indices.removeFirst()
            if (!indicesIterator.hasNext()) { // if we have no more space to fill, return 0 if we just swapped our last
                return if (freeSpace.indices.isEmpty()) 0 else 1  // element, otherwise 1
            }
            indexEntry = indicesIterator.next()
            if (freeSpace.indices.isEmpty()) {
                return -1
            }
            freeSpaceIndex = freeSpace.indices.first()
        }

        return 1 // freeSpace still usable
    }

    private fun greatestIndexPositionLessThanGiven(index: Int) = indices.indexOfLast { it > index }
}

class EmptySpace(fileSize: Int, cursor: Int):DataFile (-1, fileSize, cursor) {
    override fun checksum(): Long = 0
    override fun toString() = "EmptySpace(${indices.joinToString(", ")})"
}

const val FILE = "files.txt"
class DiskChecksumCalculator: Calculator(FILE) {
    private val input = parseInput()[0].map { it.toString() }
    private val fileList = mutableListOf<DataFile>()
    private val emptiesList = mutableListOf<EmptySpace>()

    private fun parseDiskFiles() {
        var isFile = true
        var diskIndex = 0
        var fileId = 0
        input.forEach { fileSize ->
            val size = fileSize.toInt()
            if (isFile) {
                fileList.add(DataFile(fileId, size, diskIndex))
                fileId ++
            } else {
                emptiesList.add(EmptySpace(size, diskIndex))
            }
            isFile = !isFile
            diskIndex += size
        }
    }

    private fun reorderFiles() {
        val lastFileIterator = fileList.reversed().iterator()
        var lastFile = lastFileIterator.next()
        emptiesList.forEach {
            var result:Int
            do {
                result = lastFile.fillIndices(it)
                if (!lastFileIterator.hasNext()) {
                    return
                }
                if (result > -1) {
                    lastFile = lastFileIterator.next()
                }
            } while (result > 0)
        }
    }

    fun calculateCheckSum(): Long {
        parseDiskFiles()
        reorderFiles()
        val sum = fileList.sumOf { it.checksum() }
        println(fileList.joinToString("\n"))
        return sum
    }
}