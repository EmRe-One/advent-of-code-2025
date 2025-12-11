package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day08>(false) {
        Resources.resourceAsList("day08_example.txt")
            .joinToString("\n") part1 40 part2 25_272
    }

}
