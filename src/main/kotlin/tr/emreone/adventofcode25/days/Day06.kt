package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day06 : Day(
    6,
    2025,
    "Trash Compactor",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Long {
        var sum = 0L

        val inputEdited = inputAsList.map { it.trim().split("\\s+".toRegex()) }

        for (x in 0 until inputEdited[0].size) {
            val operator = inputEdited.last()[x]
            val numbers = (0 until inputEdited.size - 1).map { inputEdited[it][x].toLong() }

            val currentSum = when (operator) {
                "+" -> numbers.sum()
                "*" -> numbers.reduce { acc, i -> acc * i }
                else -> throw IllegalArgumentException("Unknown operator: $operator")
            }

            sum += currentSum
        }
        return sum
    }

    private fun solveRightToLeftProblem(numbers: List<String>, operator: String): Long {
        val length = numbers.first().length
        val transformedNumbers = (length - 1 downTo 0)
            .map { x ->
                numbers.joinToString("") { it[x].toString() }.trim().toLong()
            }

        return when (operator) {
            "+" -> transformedNumbers.sum()
            "*" -> transformedNumbers.reduce { acc, i -> acc * i }
            else -> throw IllegalArgumentException("Unknown operator: $operator")
        }
    }

    override fun part2(): Long {
        var sum = 0L

        val maxLength = inputAsList.maxOf { it.length }
        val modifiedInputList = inputAsList.map { it.padEnd(maxLength, ' ') }
        val operatorLine = modifiedInputList.last()

        var currentIndex = 0
        var nextIndex: Int

        while (true) {
            nextIndex = operatorLine.indexOfAny(charArrayOf('+', '*'), currentIndex + 1)
            val numbers = modifiedInputList.dropLast(1).map {
                it.substring(currentIndex, if (nextIndex == -1) it.length else (nextIndex - 1))
            }

            val operator = operatorLine[currentIndex].toString()

            sum += solveRightToLeftProblem(numbers, operator)
            if (nextIndex == -1) {
                break
            }
            currentIndex = nextIndex
        }

        return sum
    }

}
