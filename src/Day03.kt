fun main() {

	fun intersection(input: List<String>): Iterable<Char> =
		input.fold(
			input.first()
				.toSet()
		) { acc, s ->
			acc.intersect(s.toSet())
		}

	fun pointsOf(c: Char): Int =
		when (c) {
			in 'a'..'z' -> c - 'a' + 1
			in 'A'..'Z' -> c - 'A' + 27
			else        -> error("unsupported")
		}

	fun String.splitByHalf(): List<String> =
		listOf(
			substring(0..lastIndex / 2),
			substring(length / 2..lastIndex),
		)

	fun part1(input: List<String>): Int =
		input
			.flatMap { intersection(it.splitByHalf()) }
			.sumOf(::pointsOf)

	fun part2(input: List<String>): Int =
		input.chunked(3)
			.flatMap(::intersection)
			.sumOf(::pointsOf)

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day03_test")
	check(part1(testInput) == 157)
	check(part2(testInput) == 70)

	val input = readInput("Day03")
	println(part1(input))
	println(part2(input))
}