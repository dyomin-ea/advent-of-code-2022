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

	infix fun String.gr(other: String): Boolean =
		length > other.length || length == other.length && this > other

	infix fun String.grOrEq(other: String): Boolean =
		gr(other) || equals(other)

	infix fun String.ls(other: String): Boolean =
		length < other.length || length == other.length && this < other

	infix fun String.lsOrEq(other: String): Boolean =
		ls(other) || equals(other)

	fun fullyIntersects(left: Pair<String, String>, right: Pair<String, String>): Boolean {
		fun condition(l: Pair<String, String>, r: Pair<String, String>): Boolean =
			l.first grOrEq r.first && l.second lsOrEq r.second

		return condition(left, right) || condition(right, left)
	}

	fun partiallyIntersects(left: Pair<String, String>, right: Pair<String, String>): Boolean {
		fun String.within(r: Pair<String, String>): Boolean =
			this grOrEq r.first && this lsOrEq r.second

		fun condition(l: Pair<String, String>, r: Pair<String, String>): Boolean =
			l.first.within(r) || l.second.within(r)

		return condition(left, right) || condition(right, left)
	}

	fun part1NoRanges(input: List<String>): Int =
		input
			.map { pair ->
				pair.splitBy(",")
					.let { (l, r) ->
						l.splitBy("-") to r.splitBy("-")
					}
			}
			.count { (l, r) ->
				fullyIntersects(l, r)
			}

	fun part2NoRanges(input: List<String>): Int =
		input
			.map { pair ->
				pair.splitBy(",")
					.let { (l, r) ->
						l.splitBy("-") to r.splitBy("-")
					}
			}
			.count { (l, r) ->
				partiallyIntersects(l, r)
			}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day04_test")
	check(part1(testInput) == 2)
	check(part2(testInput) == 4)
	check(part1NoRanges(testInput) == 2)
	check(part2NoRanges(testInput) == 4)

	val input = readInput("Day04")
	println(part1(input))
	println(part2(input))
	// decreases execution time by one order
	println(part1NoRanges(input))
	println(part2NoRanges(input))
}