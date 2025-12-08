package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.extensions.combinations
import tr.emreone.kotlin_utils.math.Vector3D
import kotlin.math.sqrt

fun Vector3D.length(): Double {
    return sqrt((x * x + y * y + z * z).toDouble())
}

class Day08 : Day(
    8,
    2025,
    "Playground",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val compareFirstNCircuits: Int = if (inputAsList.size < 30) 10 else 1000

        val points = inputAsList.map {
            val (x,y,z) = it.split(",")
            Vector3D(x.toLong(), y.toLong(), z.toLong())
        }

        val circuits = points.map { mutableSetOf(it) }.toMutableList()

        points
            .combinations(2)
            .map { (a, b) -> (a to b) to (b - a).length()  }
            .sortedBy { it.second }
            .take(compareFirstNCircuits)
            .forEach { (p, d) ->
                val firstSet = circuits.indexOfFirst { it.contains(p.first) }
                val secondSet = circuits.indexOfFirst { it.contains(p.second) }
                if (firstSet != secondSet) {
                    circuits[firstSet].addAll(circuits[secondSet])
                    circuits.removeAt(secondSet)
                }
            }

        return circuits.sortedByDescending { it.size }.take(3).fold(1) { acc, a -> acc * a.size }
    }

    override fun part2(): Long {
        val points = inputAsList.map {
            val (x,y,z) = it.split(",")
            Vector3D(x.toLong(), y.toLong(), z.toLong())
        }

        val circuits = points.map { mutableSetOf(it) }.toMutableList()
        lateinit var lastConnection: Pair<Vector3D, Vector3D>

        points
            .combinations(2)
            .map { (a, b) -> (a to b) to (b - a).length()  }
            .sortedBy { it.second }
            .takeWhile { (p, d) ->
                val firstSet = circuits.indexOfFirst { it.contains(p.first) }
                val secondSet = circuits.indexOfFirst { it.contains(p.second) }
                if (firstSet != secondSet) {
                    circuits[firstSet].addAll(circuits[secondSet])
                    circuits.removeAt(secondSet)
                    lastConnection = p
                }
                circuits.size != 1
            }.toList()

        return lastConnection.first.x * lastConnection.second.x
    }

}
