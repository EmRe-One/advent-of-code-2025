package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day04>(false) {
        Resources.resourceAsList("day04_example.txt")
            .joinToString("\n") part1 13 part2 43
    }

}
