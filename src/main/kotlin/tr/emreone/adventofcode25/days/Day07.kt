package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Coords
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.plus
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y

class Day07 : Day(
    7,
    2025,
    "Laboratories",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val beams = ArrayDeque<Coords>()
        val seen = mutableSetOf<Coords>()
        lateinit var s: Coords
        val height = inputAsList.size

        fun addIfNotSeen(coord: Coords) {
            if (!seen.contains(coord)) {
                beams.add(coord)
                seen.add(coord)
            }
        }

        outer@for(y in inputAsGrid.indices) {
            for(x in inputAsGrid[y].indices) {
                val c = inputAsGrid[y][x]
                if (c == 'S') {
                    addIfNotSeen(Coords(x, y))
                    break@outer
                }
            }
        }
        var counter = 0

        while(beams.isNotEmpty()) {
            val beam = beams.removeFirst()

            if (inputAsGrid[beam.y][beam.x] in ".S") {
                if (beam.y == height - 1) continue
                addIfNotSeen(beam.plus(Direction4.SOUTH))
            }
            else if (inputAsGrid[beam.y][beam.x] == '^') {
                counter++
                addIfNotSeen(beam.plus(Direction4.WEST))
                addIfNotSeen(beam.plus(Direction4.EAST))
            }
        }
        return counter
    }

    override fun part2(): Long {
        lateinit var s: Coords
        val height = inputAsList.size

        outer@for(y in inputAsGrid.indices) {
            for(x in inputAsGrid[y].indices) {
                val c = inputAsGrid[y][x]
                if (c == 'S') {
                    s = Coords(x, y)
                    break@outer
                }
            }
        }

        val cache = mutableMapOf<Coords, Long>()
        fun possibleTimelinesFrom(coord: Coords): Long  {
            if (coord in cache) return cache[coord]!!

            val timelines = if (coord.y == height - 1) {
                1L
            }
            else if (inputAsGrid[coord.y][coord.x] in ".S") {
                possibleTimelinesFrom(coord.plus(Direction4.SOUTH))
            }
            else if (inputAsGrid[coord.y][coord.x] == '^') {
                val leftTimelines = possibleTimelinesFrom(coord.plus(Direction4.WEST))
                val rightTimelines = possibleTimelinesFrom(coord.plus(Direction4.EAST))
                leftTimelines + rightTimelines
            }
            else {
                0L
            }

            cache[coord] = timelines
            return timelines
        }

        return possibleTimelinesFrom(s)
    }

}
