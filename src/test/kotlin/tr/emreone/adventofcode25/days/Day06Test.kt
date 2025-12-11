package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day06>(false) {
        Resources.resourceAsList("day06_example.txt")
            .joinToString("\n") part1 4_277_556L part2 3_263_827L
    }

}
