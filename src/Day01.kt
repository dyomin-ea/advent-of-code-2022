fun main() {
	fun part1(input: List<String>): Int =
		input.fold(mutableListOf(0)) { acc, s ->
			acc += if (s.isEmpty()) {
				0
			} else {
				acc.pop() + s.toInt()
			}

			acc
		}
			.max()

	fun part2(input: List<String>): Int =
		input.fold(mutableListOf(0)) { acc, s ->
			acc += if (s.isEmpty()) {
				0
			} else {
				acc.pop() + s.toInt()
			}

			acc
		}.sortedDescending()
			.take(3)
			.sum()

	val testInput = readInput("Day01_test")
	check(part1(testInput) == 24000)
	check(part2(testInput) == 45000)

	val input = readInput("Day01")
	println(part1(input))
	println(part2(input))
}
