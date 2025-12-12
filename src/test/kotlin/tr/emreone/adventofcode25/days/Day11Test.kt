package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day11>(false) {
        Resources.resourceAsList("day11_example.txt")
            .joinToString("\n") part1 5 part2 0L
    }

}
