package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day03 : Day(
    3,
    2025,
    "Lobby",
    session = Resources.resourceAsString("session.cookie")
) {

    private fun findMaxNumber(line: String, digits: Int = 2): Long {

        val number = mutableListOf<Pair<Int, Int>>()

        repeat(digits) { index ->
            val lastDigit = number.lastOrNull() ?: (-1 to -1)

            var nextMaxNumber = line.substring(lastDigit.first + 1, line.length - (digits - index - 1))
                .mapIndexed { i, c -> i to c.digitToInt() }
                .maxBy { it.second }

            nextMaxNumber = (nextMaxNumber.first + lastDigit.first + 1) to nextMaxNumber.second
            number.add(nextMaxNumber)
        }

        // result
        return number.map { it.second }.joinToString("").toLong()
    }

    override fun part1(): Int {
        return inputAsList.sumOf { l ->
            findMaxNumber(l).toInt()
        }
    }

    override fun part2(): Long {
        return inputAsList.sumOf { l ->
            findMaxNumber(l, 12)
        }
    }

}
