package tr.emreone.adventofcode

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import org.reflections.Reflections
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.dayNumber
import tr.emreone.kotlin_utils.automation.execute
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

private fun getAllDayClasses(): Collection<Class<out Day>> =
    Reflections("").getSubTypesOf(Day::class.java).filter { it.simpleName.matches(Regex("Day\\d+")) }

@OptIn(ExperimentalTime::class)
fun main() {

    val aocTerminal = Terminal()

    with(aocTerminal) {
        println(TextColors.red("\n~~~ Advent Of Code Runner ~~~\n"))
        val dayClasses = getAllDayClasses().sortedBy(::dayNumber)

        val dayNumber = prompt(
            "Wählen Sie einen Tag aus (0 für alle, 1-12 für bestimmten Tag):"
        )

        val totalDuration = dayClasses
            .map { it.execute() }
            .reduceOrNull(Duration::plus)

        println("\nTotal runtime: ${TextColors.red("$totalDuration")}")
    }
}
