package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day02 : Day(
    2,
    2025,
    "Gift Shop",
    session = Resources.resourceAsString("session.cookie")
) {

    /**
     * Alternative for part 1 without regex
     *
     * val length = idStr.length
     * if (length.mod(2) != 0) return true
     * return idStr.take(length / 2) != idStr.drop(length / 2)
     */
    private fun isValidId(id: Long, moreThanTwice: Boolean = false): Boolean {
        val idStr = id.toString()

        val pattern = if (moreThanTwice)
                "^(\\d+)\\1+$".toRegex()
            else
                "^(\\d+)\\1$".toRegex()

        return !pattern.containsMatchIn(idStr)
    }

    override fun part1(): Long {
        return inputAsString
            .split(",")
            .sumOf { l ->
                val (start, end) = l.split("-").map { it.toLong() }
                (start..end).filterNot(::isValidId).sum()
            }
    }

    override fun part2(): Long {
        return inputAsString
            .split(",")
            .sumOf { l ->
                val (start, end) = l.split("-").map { it.toLong() }
                (start..end).filter { !isValidId(it, moreThanTwice = true) }.sum()
            }

    }

}
