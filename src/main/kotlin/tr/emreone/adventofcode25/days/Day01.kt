package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Logger
import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day01 : Day(
    1,
    2025,
    "Secret Entrance",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        var currentPosition = 50

        return inputAsList.count { l ->
            val direction = if (l[0] == 'L') -1 else 1
            val steps = l.substring(1).toInt()
            currentPosition = (currentPosition + steps * direction).mod(100)

            currentPosition == 0
        }
    }

    override fun part2(): Int {
        var lastPosition = -1
        var currentPosition = 50

        return inputAsList.sumOf { l ->
            lastPosition = currentPosition

            val direction = if (l[0] == 'L') -1 else 1
            val steps = l.substring(1).toInt()
            val fullRotation = steps.div(100)
            val restRotation = steps.mod(100)

            val tempPosition = currentPosition + restRotation * direction
            currentPosition = (tempPosition).mod(100)

            // only if last position was NOT 0, check if zero is crossed
            val crossZero = (lastPosition != 0) and (tempPosition !in 1..<100)

            // return fullRotations plus initial cross
            fullRotation + if (crossZero) 1 else 0
        }
    }
}
