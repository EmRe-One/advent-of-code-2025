package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day05 : Day(
    5,
    2025,
    "Cafeteria",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val blocks = inputAsString.split("\n\n")
        val ingredientRanges = blocks[0]
            .split('\n')
            .map { line ->
                val ranges = line.split("-")
                LongRange(ranges[0].toLong(), ranges[1].toLong())
            }

        return blocks[1]
            .split('\n')
            .count {
                ingredientRanges.any { range ->
                    it.toLong() in range
                }
            }
    }

    private fun mergeRanges(ranges: MutableList<LongRange>, newRange: LongRange) {
        var start = newRange.first
        var end = newRange.last
        val toRemove = mutableListOf<LongRange>()

        for (range in ranges) {
            if (range.last < start || range.first > end) {
                continue // No overlap, do nothing
            }
            else {
                // Overlapping range found
                start = minOf(start, range.first)
                end = maxOf(end, range.last)
                toRemove.add(range)
            }
        }

        ranges.removeAll(toRemove)
        ranges.add(LongRange(start, end))
    }

    override fun part2(): Long {
        val blocks = inputAsString.split("\n\n")
        val ingredientRanges = blocks[0]
            .split('\n')
            .map { line ->
                val ranges = line.split("-")
                LongRange(ranges[0].toLong(), ranges[1].toLong())
            }

        val combinedIngredientRanges = mutableListOf<LongRange>()

        for (range in ingredientRanges) {
            mergeRanges(combinedIngredientRanges, range)
        }

        return combinedIngredientRanges.sumOf { range ->
            range.last - range.first + 1
        }
    }

}
