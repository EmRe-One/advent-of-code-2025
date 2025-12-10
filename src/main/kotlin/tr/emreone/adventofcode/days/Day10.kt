package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import java.util.BitSet
import java.util.PriorityQueue
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToLong

class Day10 : Day(
    10,
    2025,
    "Factory",
    session = Resources.resourceAsString("session.cookie")
) {

    data class State(val presses: Long, val current: IntArray) : Comparable<State> {
        override fun compareTo(other: State): Int = this.presses.compareTo(other.presses)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is State) return false
            return current.contentEquals(other.current)
        }

        override fun hashCode(): Int = current.contentHashCode()
    }

    class Manual(
        val rawDiagram: String,
        val buttons: List<List<Int>>,
        val targetJoltages: List<Int>
    ) {
        val lightCount = rawDiagram.length
        val targetState: BitSet = BitSet(lightCount).apply {
            rawDiagram.forEachIndexed { index, ch ->
                if (ch == '#') this.set(index)
            }
        }

        @Deprecated("It takes toooooooooooo long to solve with this")
        fun solveBrutForce(): Int {
            var minTotalPresses = Int.MAX_VALUE
            val numCounters = targetJoltages.size

            val currentCounters = IntArray(numCounters)

            fun search(buttonIndex: Int, currentPresses: Int) {
                if (currentPresses >= minTotalPresses) return

                // calculate the number of minimum (direct presses), if theoretically every button is pressed directly
                // this number is the min of presses. in current constellation, we need definitely more
                var maxRemainder = 0
                for (i in 0 until numCounters) {
                    val rem = targetJoltages[i] - currentCounters[i]
                    if (rem < 0) return // overpressed
                    if (rem > maxRemainder) maxRemainder = rem
                }

                // need more presses to reach the number of targetPresses then the current best score, return
                if (currentPresses + maxRemainder >= minTotalPresses) return

                // if all buttons pressed and remainder is exactly 0,
                // targetJoltage reached :D
                if (buttonIndex == buttons.size) {
                    if (maxRemainder == 0) {
                        minTotalPresses = currentPresses
                    }
                    return
                }

                // now press buttons and start recursion
                val affectedCounters = buttons[buttonIndex]
                var limit = Int.MAX_VALUE
                if (affectedCounters.isEmpty()) {
                    limit = 0
                } else {
                    for (counterIdx in affectedCounters) {
                        val possible = targetJoltages[counterIdx] - currentCounters[counterIdx]
                        limit = min(limit, possible)
                    }
                }

                for (k in 0..limit) {
                    if (k > 0) {
                        for (c in affectedCounters) currentCounters[c]++
                    }

                    search(buttonIndex + 1, currentPresses + k)
                }

                if (limit > 0) {
                    for (c in affectedCounters) currentCounters[c] -= limit
                }
            }

            search(0, 0)
            return if (minTotalPresses == Int.MAX_VALUE) 0 else minTotalPresses
        }

        @Deprecated("It takes toooooooooooo long to solve with this")
        fun solveDijkstra(): Long {
            val numCounters = targetJoltages.size
            val pq = PriorityQueue<State>()
            val visited = HashSet<String>()

            // Start with 0 and initial pressStates
            pq.add(State(0L, IntArray(numCounters)))

            while (pq.isNotEmpty()) {
                val (presses, current) = pq.poll()

                // 1. Check: Haben wir das Ziel erreicht?
                var matches = true
                var overshot = false
                for (i in 0 until numCounters) {
                    if (current[i] != targetJoltages[i]) matches = false
                    if (current[i] > targetJoltages[i]) overshot = true
                }

                if (matches) return presses // Because PriorityQueue, is this definitely the minimum
                if (overshot) continue

                val key = current.joinToString(",")
                if (!visited.add(key)) continue // because already visited

                // press all button one after one, and add to the queue only if not overshot
                for ((btnIdx, btnEffects) in buttons.withIndex()) {
                    var canPress = true
                    for (effectIdx in btnEffects) {
                        if (current[effectIdx] + 1 > targetJoltages[effectIdx]) {
                            canPress = false
                            break
                        }
                    }

                    if (canPress) {
                        val nextCounts = current.copyOf()
                        for (effectIdx in btnEffects) {
                            nextCounts[effectIdx]++
                        }
                        pq.add(State(presses + 1, nextCounts))
                    }
                }
            }
            return -1 // should be not reachable, because there should be always one minimum
        }

        companion object {
            fun of(manual: String): Manual {
                val i = manual.substringAfter('[').substringBefore(']').trim()
                val b = manual.substringAfter(']').substringBefore('{').trim()
                val j = manual.substringAfter('{').substringBefore('}').trim()

                val bb = b.split(" ").map { l ->
                    l.trimStart('(').trimEnd(')').split(',').map { it.trim().toInt() }
                }
                val jj = j.split(",").map { l -> l.trim().toInt() }

                return Manual(i, bb, jj)
            }
        }
    }

    override fun part1(): Int {
        val manuals = inputAsList.map { line -> Manual.of(line) }

        fun solve(manual: Manual): Int {
            var minPresses = Int.MAX_VALUE

            // recursive approach
            fun search(buttonIndex: Int, currentLights: BitSet, pressCount: Int) {
                if (pressCount >= minPresses) return

                if (buttonIndex == manual.buttons.size) {
                    if (currentLights == manual.targetState) {
                        minPresses = min(pressCount, minPresses)
                    }
                    return
                }

                // press next button schemata
                search(buttonIndex + 1, currentLights, pressCount)

                // press current button schemata
                manual.buttons[buttonIndex].forEach { i ->
                    currentLights.flip(i)
                }
                search(buttonIndex + 1, currentLights, pressCount + 1)

                // reverse button press to iterate over all button schematics from fresh state
                manual.buttons[buttonIndex].forEach { i ->
                    currentLights.flip(i)
                }
            }

            // start Search algorithm with a fresh bitset (all lights off)
            search(0, BitSet(manual.lightCount), 0)

            return if (minPresses == Int.MAX_VALUE) 0 else minPresses
        }

        return manuals.sumOf { solve(it) }
    }

    // ====================================================================
    // ============        Helper methods for part II          ============
    // ====================================================================

    fun solveMathematical(manual: Manual, manualId: Int): Long {
        val numEquations = manual.targetJoltages.size
        val numVars = manual.buttons.size

        // Wir suchen eine 'Basis-Lösung'.
        // Wenn wir 10 Gleichungen haben, brauchen wir meist 10 Knöpfe.
        val rank = numEquations.coerceAtMost(numVars)

        var minTotalPresses = Long.MAX_VALUE
        var foundSolution = false

        // Rekursion
        fun combinations(start: Int, depth: Int, selectedIndices: IntArray) {
            if (depth == rank) {
                val presses = solveGauss(manual, selectedIndices)
                if (presses != -1L) {
                    if (presses < minTotalPresses) minTotalPresses = presses
                    foundSolution = true
                }
                return
            }

            val end = numVars - (rank - depth) + 1
            for (i in start until end) {
                selectedIndices[depth] = i
                combinations(i + 1, depth + 1, selectedIndices)
            }
        }

        if (numVars >= numEquations) {
            combinations(0, 0, IntArray(rank))
        } else {
            // Sonderfall: Weniger Knöpfe als Targets
            val allIndices = IntArray(numVars) { it }
            val res = solveGauss(manual, allIndices)
            if (res != -1L) return res
        }

        return if (foundSolution) minTotalPresses else 0L
    }

    private fun solveGauss(manual: Manual, selectedIndices: IntArray): Long {
        val matrixSize = selectedIndices.size
        val targets = manual.targetJoltages

        // A Matrix bauen
        val matrix = Array(matrixSize) { DoubleArray(matrixSize) }
        val rhs = DoubleArray(matrixSize)

        // Wir nutzen die ersten 'matrixSize' Gleichungen zum Lösen
        for (i in 0 until matrixSize) {
            rhs[i] = targets[i].toDouble()
        }

        for (col in 0 until matrixSize) {
            val btnIdx = selectedIndices[col]
            val btnEffects = manual.buttons[btnIdx]
            for (row in 0 until matrixSize) {
                if (btnEffects.contains(row)) {
                    matrix[row][col] = 1.0
                } else {
                    matrix[row][col] = 0.0
                }
            }
        }

        // --- Gauß Elimination ---
        for (i in 0 until matrixSize) {
            var pivotRow = i
            var maxVal = abs(matrix[i][i])
            for (k in i + 1 until matrixSize) {
                if (abs(matrix[k][i]) > maxVal) {
                    maxVal = abs(matrix[k][i])
                    pivotRow = k
                }
            }

            // Wenn Matrix singulär ist (keine eindeutige Lösung für diese Kombi) -> Abbruch
            if (maxVal < 1e-9) return -1L

            // Swap
            val tempRow = matrix[i]; matrix[i] = matrix[pivotRow]; matrix[pivotRow] = tempRow
            val tempRhs = rhs[i]; rhs[i] = rhs[pivotRow]; rhs[pivotRow] = tempRhs

            // Eliminieren
            for (j in i + 1 until matrixSize) {
                val factor = matrix[j][i] / matrix[i][i]
                rhs[j] -= factor * rhs[i]
                for (k in i until matrixSize) {
                    matrix[j][k] -= factor * matrix[i][k]
                }
            }
        }

        // Rückwärts einsetzen
        val solution = DoubleArray(matrixSize)
        var totalPresses = 0L

        for (i in matrixSize - 1 downTo 0) {
            var sum = 0.0
            for (j in i + 1 until matrixSize) {
                sum += matrix[i][j] * solution[j]
            }
            val x = (rhs[i] - sum) / matrix[i][i]

            // Validierung 1: Nicht negativ
            if (x < -1e-4) return -1L

            // Validierung 2: Ganzzahlig
            val rounded = x.roundToLong()
            if (abs(x - rounded) > 1e-4) return -1L

            solution[i] = x
            totalPresses += rounded
        }

        // --- SCHLUSS-VALIDIERUNG ---
        // Das fehlte vorher vielleicht:
        // Wir müssen prüfen, ob diese Drücke auch ALLE anderen Gleichungen erfüllen
        // (falls wir nur eine Teilmenge der Gleichungen für die Matrix genutzt haben)

        // Array mit Anzahl Drücken pro Knopf (Index in manual.buttons)
        val pressesPerButton = LongArray(manual.buttons.size)
        for (i in 0 until matrixSize) {
            pressesPerButton[selectedIndices[i]] = solution[i].roundToLong()
        }

        // Prüfe gegen ALLE Targets
        for (targetIdx in manual.targetJoltages.indices) {
            var currentJoltage = 0L
            for ((btnIdx, btnCount) in pressesPerButton.withIndex()) {
                if (btnCount > 0 && manual.buttons[btnIdx].contains(targetIdx)) {
                    currentJoltage += btnCount
                }
            }
            if (currentJoltage.toInt() != manual.targetJoltages[targetIdx]) {
                // Die Lösung funktioniert für die gewählte Sub-Matrix,
                // aber verletzt eine andere Bedingung.
                return -1L
            }
        }

        return totalPresses
    }

    override fun part2(): Long {
        val manuals = inputAsList.map { line -> Manual.of(line) }

        return manuals.mapIndexed { index, manual ->
            val result = solveMathematical(manual, index + 1)
            println("Manual #$index solved: $result")
            result
        }.sum()
    }
}
