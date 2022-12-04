import kotlin.math.max

fun main() {

	fun IntRange.length(): Int =
		last - first + 1

	fun String.asRange(): IntRange =
		splitBy("-")
			.map(String::toInt)
			.mapLeft(String::toInt)
			.run { first..second }

	fun String.intoRangePair(): Pair<IntRange, IntRange> =
		splitBy(",")
			.map(String::asRange)
			.mapLeft(String::asRange)

	fun part1(input: List<String>): Int =
		input
			.map(String::intoRangePair)
			.count { (l, r) ->
				(l union r).size == max(l.length(), r.length())
			}

	fun part2(input: List<String>): Int {
		return input
			.map(String::intoRangePair)
			.count { (l, r) ->
				(l intersect r).isNotEmpty()
			}
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day04_test")
	check(part1(testInput) == 2)
	check(part2(testInput) == 4)

	val input = readInput("Day04")
	println(part1(input))
	println(part2(input))
}

