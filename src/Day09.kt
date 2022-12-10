import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.truncate

typealias Dot = Pair<Int, Int>

fun main() {

    fun way(input: String): List<Direction> =
        buildList {
            val pair = input.splitBy(" ")
            repeat(pair.second.toInt()) {
                add(Direction(pair.first))
            }
        }

    fun Dot.move(direction: Direction) =
        when (direction) {
            Direction.U -> copy(first = first + 1)
            Direction.D -> copy(first = first - 1)
            Direction.L -> copy(second = second - 1)
            Direction.R -> copy(second = second + 1)
        }

    infix fun Dot.cl(right: Dot): Boolean =
        abs(first - right.first) <= 1 &&
                abs(second - right.second) <= 1

    fun tailMovement(lastTail: Dot, lastHead: Dot, head: Dot): Dot =
        TODO()

    fun List<Dot>.getTailPath(): List<Dot> =
        windowed(2)
            .scan(0 to 0) { t, (h1, h2) ->
                if (t cl h2) t else t + tailMovement(t, h1, h2)
            }

    fun readHeadPath(input: List<String>): List<Dot> =
        input.flatMap(::way)
            .scan(0 to 0) { h, d ->
                h.move(d)
            }

    fun part1(input: List<String>): Int =
        readHeadPath(input)
            .getTailPath()
            .toSet()
            .size

    fun part2(input: List<String>): Int {

        tailrec fun getDeepPath(
            input: List<Dot>,
            accumulator: MutableSet<Any> = HashSet(),
            depth: Int = 9,
        ): Set<Any> =
            if (depth == 0) accumulator
            else {
                val headPath = input.getTailPath()
                accumulator.addAll(headPath)

                getDeepPath(
                    headPath,
                    accumulator,
                    depth = depth - 1
                )
            }

        return getDeepPath(readHeadPath(input)).size
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13) {
        part1(testInput)
    }
    check(part2(testInput) == 9)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

enum class Direction {
    U, D, L, R;

    companion object {

        operator fun invoke(input: String): Direction =
            when (input) {
                "U" -> U
                "D" -> D
                "L" -> L
                "R" -> R
                else -> error("unknown")
            }
    }

    operator fun plus(other: Direction): Direction? =
        if (this to other == this to this) this
        else null
}

val line: MutableList<String>
    get() = (0..5).map { "." }
        .toMutableList()

fun debug(t: Dot, h1: Dot, h2: Dot) {
    val field = (0..4).map { line }
    field[t.first][t.second] = "T"
    field[h1.first][h1.second] = "1"
    field[h2.first][h2.second] = "2"

    println(
        field.reversed()
            .joinToString(separator = "\n") { it.joinToString(separator = "") }
    )
    println()
}

fun vec(start: Dot, end: Dot): Dot =
    end.first - start.first to end.second - start.second

operator fun Dot.plus(other: Dot): Dot =
    first + other.first to second + other.second

operator fun Dot.div(d: Int): Dot =
    truncate(first.toDouble() / d).toInt() to
            truncate(second.toDouble() / d).toInt()

val Dot.direction
    get() = first.sign to second.sign