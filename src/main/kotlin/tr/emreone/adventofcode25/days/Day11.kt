package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

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

    override fun part1(): Int {
        val nodes = inputAsList.associate {
            val node = Node.of(it)
            node.name to node
        }

        val possiblePaths = mutableSetOf<List<Node>>()
        fun traverse(currentNodeName: String, path: List<Node> = listOf()) {
            if (currentNodeName == "out") {
                possiblePaths.add(path)
                return
            }

            val currentNode = nodes[currentNodeName]!!
            if (currentNode in path) {
                println("WARNING: Block recursive call, because ${currentNode.name} is already in path. Loop detected!!")
                return
            }

            for (n in currentNode.adjacent) {
                val newPath = path + currentNode
                traverse(n, newPath)
            }
        }

        traverse("you")
        return possiblePaths.size
    }

    override fun part2(): Long {
        val nodes = inputAsList.associate {
            val node = Node.of(it)
            node.name to node
        }
        var validPathCount = 0L

        val currentPathVisited = mutableSetOf<String>()

        fun traverse(currentNodeName: String, seenFft: Boolean, seenDac: Boolean) {
            if (currentNodeName == "out") {
                if (seenFft && seenDac) {
                    validPathCount++
                    if (validPathCount % 1_000_000 == 0L) println("Bereits $validPathCount Pfade gefunden...")
                }
                return
            }

            val currentNode = nodes[currentNodeName] ?: return

            if (currentNodeName in currentPathVisited) {
                return
            }

            currentPathVisited.add(currentNodeName)

            val nowSeenFft = seenFft || (currentNodeName == "fft")
            val nowSeenDac = seenDac || (currentNodeName == "dac")

            for (neighbor in currentNode.adjacent) {
                traverse(neighbor, nowSeenFft, nowSeenDac)
            }

            currentPathVisited.remove(currentNodeName)
        }
        
        traverse("svr", seenFft = false, seenDac = false)
        return validPathCount
    }

}
