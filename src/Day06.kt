fun main() {

	fun indexOfFirstWindow(size: Int, input: String): Int =
		input.windowed(size)
			.indexOfFirst { it.toSet().size == it.length }


	fun part1(input: List<String>): List<Int> {
		return input.map { indexOfFirstWindow(4, it) + 4 }
	}

	fun part2(input: List<String>): List<Int> {
		return input.map { indexOfFirstWindow(14, it) + 14 }
	}

	fun readerSolution(input: Sequence<String>): Int {
		return input.mapIndexed { index, s ->
			if (s.length == s.toSet().size) {
				index + s.length
			} else {
				-1
			}
		}
			.first { it != -1 }
	}


	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day06_test")
	check(part1(testInput) == listOf(7, 5, 6, 10, 11))
	check(part2(testInput) == listOf(19, 23, 23, 29, 26)) {
		println(part2(testInput))
	}

	val input = readInput("Day06")
	println(part1(input))
	println(part2(input))
}
