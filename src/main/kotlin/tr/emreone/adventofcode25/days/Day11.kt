package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import kotlin.collections.mutableMapOf
import kotlin.text.get

class Day11 : Day(
    11,
    2025,
    "Reactor",
    session = Resources.resourceAsString("session.cookie")
) {

    class Node(val name: String, val adjacent: List<String>) {
        companion object {
            fun of(line: String): Node {
                val (a, b) = line.split(':')
                val adjacent = b.trim().split(' ')

                return Node(a.trim(), adjacent)
            }
        }
    }

    class Reactor(
        val nodes: Map<String, Node>
    ) {
        private val cache = mutableMapOf<Pair<String, String>, Long>()

        init {
            cache.clear()
        }

        fun countPaths(src: String, dst: String): Long {
            if (cache.containsKey(src to dst)) {
                return cache[src to dst]!!
            }
            if (src == dst) {
                return 1L
            }
            val sum = nodes[src]?.adjacent?.sumOf { neighbor ->
                countPaths(neighbor, dst)
            } ?: 0L
            cache[src to dst] = sum
            return sum
        }
    }

    override fun part1(): Int {
        val nodes = inputAsList.associate {
            val node = Node.of(it)
            node.name to node
        }

        val reactor = Reactor(nodes)

        return reactor.countPaths("you", "out").toInt()
    }

    override fun part2(): Long {
        val nodes = inputAsList.associate {
            val node = Node.of(it)
            node.name to node
        }
        val reactor = Reactor(nodes)

        return reactor.run {
            val a = countPaths("svr", "dac")
                .times(countPaths("dac", "fft"))
                .times(countPaths("fft", "out"))

            val b = countPaths("svr", "fft")
                .times(countPaths("fft", "dac"))
                .times(countPaths("dac", "out"))

            a + b
        }
    }

}
