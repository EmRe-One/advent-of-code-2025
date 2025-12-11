package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day02>(false) {
        Resources.resourceAsList("day02_example.txt")
            .joinToString("\n") part1 1_227_775_554L part2 4_174_379_265L
    }

}
