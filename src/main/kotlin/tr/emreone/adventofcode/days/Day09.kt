package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.extensions.combinations
import tr.emreone.kotlin_utils.math.Point2D
import kotlin.math.max
import kotlin.math.min

class Day09 : Day(
    9,
    2025,
    "Movie Theater",
    session = Resources.resourceAsString("session.cookie")
) {

    data class PointD(val x: Double, val y: Double)

    fun isPointInPolygon(p: PointD, polygon: List<Point2D>): Boolean {
        var inside = false
        var j = polygon.size - 1

        for(i in polygon.indices) {
            val pi = polygon[i]
            val pj = polygon[j]

            if (((pi.y > p.y) != (pj.y > p.y))
                && (p.x < (pj.x - pi.x) * (p.y - pi.y) / (pj.y - pi.y) + pi.x)) {
                inside = !inside
            }
            j = i
        }
        return inside
    }

    fun isValidRectangle(topLeft: Point2D, bottomRight: Point2D, polygon: List<Point2D>): Boolean {
        val minX = topLeft.x
        val maxX = bottomRight.x
        val minY = topLeft.y
        val maxY = bottomRight.y

        for(k in polygon.indices) {
            val v1 = polygon[k]
            val v2 = polygon[(k + 1) % polygon.size]

            if (v1.x == v2.x) {
                val edgeX = v1.x
                val edgeMinY = min(v1.y, v2.y)
                val edgeMaxY = max(v1.y, v2.y)

                if (edgeX in (minX + 1)..<maxX) {
                    if (max(minY, edgeMinY) < min(maxY, edgeMaxY)) {
                        return false
                    }
                }
            }
            else if (v1.y == v2.y) {
                val edgeY = v1.y
                val edgeMinX = min(v1.x, v2.x)
                val edgeMaxX = max(v1.x, v2.x)

                if (edgeY in (minY + 1)..<maxY) {
                    if (max(minX, edgeMinX) < min(maxX, edgeMaxX)) {
                        return false
                    }
                }
            }
        }

        val midX = (minX + maxX) / 2.0
        val midY = (minY + maxY) / 2.0

        return isPointInPolygon(PointD(midX, midY), polygon)
    }

    override fun part1(): Long {
        val redTiles = inputAsList
            .map { l ->
                val (x,y) = l.split(',').map { v -> v.trim().toLong() }
                Point2D(x, y)
            }

        return redTiles
            .combinations(2)
            .toList()
            .maxOf { (p1, p2) ->
                val minX = min(p1.x, p2.x)
                val minY = min(p1.y, p2.y)
                val maxX = max(p1.x, p2.x)
                val maxY = max(p1.y, p2.y)

                (maxX - minX + 1) * (maxY - minY + 1)
            }
    }

    override fun part2(): Long {
        val polygon = inputAsList
            .map { l ->
                val (x, y) = l.split(',').map { v -> v.trim().toLong() }
                Point2D(x, y)
            }

        var maxArea = 0L
        val n = polygon.size

        for(i in 0..<n) {
            for (j in (i+1)..<n) {
                val p1 = polygon[i]
                val p2 = polygon[j]

                val topLeft = Point2D(min(p1.x, p2.x), min(p1.y, p2.y))
                val bottomRight = Point2D(max(p1.x, p2.x), max(p1.y, p2.y))

                val width = bottomRight.x - topLeft.x + 1
                val height = bottomRight.y - topLeft.y + 1
                val area = width * height

                // doesn't matter if valid or invalid... it is not bigger than max valid rectangle --> next
                if (area <= maxArea) continue

                if (isValidRectangle(topLeft, bottomRight, polygon)) {
                    maxArea = area
                }
            }
        }

        return maxArea
    }

}
