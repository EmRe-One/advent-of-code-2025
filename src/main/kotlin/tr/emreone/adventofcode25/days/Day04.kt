package tr.emreone.adventofcode25.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day04 : Day(
    4,
    2025,
    "Printing Department",
    session = Resources.resourceAsString("session.cookie")
) {

    fun extendGrid(grid: List<List<Char>>): List<List<Char>> {
        val width = grid[0].size
        val height = grid.size

        val extendedGrid = mutableListOf<List<Char>>()

        // adding first line
        val emptyLine = ".".repeat(width + 2).toList();

        extendedGrid.add(emptyLine)

        // adding left, right border
        for (y in 0 until height) {
            val line = grid[y].toCharArray().toMutableList()
            line.add(0, '.')
            line.add('.')
            extendedGrid.add(line.toList())
        }

        // adding last line
        extendedGrid.add(emptyLine)

        return extendedGrid
    }

    fun getNeighbours(x: Int, y: Int, grid: List<List<Char>>): List<Char> {
        val neighbours = mutableListOf<Char>()

        for(dy in -1..1) {
            for (dx in -1..1) {
                if (dy == 0 && dx == 0) {
                    continue
                }

                neighbours.add(grid[y+dy][x+dx])
            }
        }

        return neighbours
    }

    fun foundRemovableCells(grid: List<List<Char>>): List<Pair<Int, Int>> {
        val removableCells = mutableListOf<Pair<Int, Int>>()

        for(y in 1 until grid.size ) {
            for (x in 1 until grid[0].size) {
                val cell = grid[y][x]
                if (cell != '@') {
                    continue
                }
                val numOfNeighbours = getNeighbours(x, y, grid)
                    .filter { it == '@' }
                    .size

                if (numOfNeighbours < 4) {
                    removableCells.add(x to y)
                }
            }
        }

        return removableCells
    }

    override fun part1(): Int {
        val grid = extendGrid(inputAsGrid)

        // only one iteration
        return foundRemovableCells(grid).size
    }

    override fun part2(): Int {
        val grid = extendGrid(inputAsGrid).map { it.toMutableList() }.toMutableList()
        var counter = 0

        while(true) {
            val removableCells = foundRemovableCells(grid)

            if (removableCells.isEmpty()) {
                break
            }

            counter += removableCells.size

            for((x, y) in removableCells) {
                grid[y][x] = '.'
            }
        }

        return counter
    }

}
