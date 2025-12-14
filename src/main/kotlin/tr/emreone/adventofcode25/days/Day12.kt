package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day12 : Day(
    12,
    2025,
    "Christmas Tree Farm",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Long {
        val parts = inputAsString.split("\n\n")

        val presents = parts.dropLast(1)

        val sizes = mutableListOf<Int>()
        for (present in presents) {
            val lines = present.lines()

            var size = 0
            for (row in lines.drop(1)) {
                for (c in row) {
                    if (c == '#') {
                        size += 1
                    }
                }
            }
            sizes.add(size)
        }

        var ans = 0L
        val regionsBlock = parts.last()

        // Zeilenweise durch den letzten Block iterieren
        for (region in regionsBlock.lines()) {
            if (region.isBlank()) continue // Leere Zeilen überspringen

            val (sz, nsStr) = region.split(": ")
            val (rStr, cStr) = sz.split("x")

            val r = rStr.toInt()
            val c = cStr.toInt()
            val totalGridSize = r * c

            val ns = nsStr.trim().split(Regex("\\s+")).map { it.toInt() }

            var totalPresentSize = 0
            ns.forEachIndexed { i, n ->
                totalPresentSize += n * sizes[i]
            }

            // Vergleich (Achtung: Double Vergleich für 1.3)
            if (totalPresentSize * 1.3 < totalGridSize) {
                ans += 1
            }
        }

        return ans
    }

}
