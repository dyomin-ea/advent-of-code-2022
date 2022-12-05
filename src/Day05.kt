fun main() {
	fun stacksFrom(input: List<String>): List<MutableList<Char>> =
		(1..input.first().length step 4)
			.map { c ->
				input.map { it[c] }
					.filterNot(Char::isWhitespace)
					.reversed()
					.toMutableList()
			}

	fun moves9000From(input: List<String>): List<Move9000> {
		fun movesFrom(input: String): List<Move9000> {
			val (count, f, t) = """move (\d+) from (\d+) to (\d+)"""
				.toRegex()
				.find(input)!!
				.destructured

			return (1..count.toInt()).map { Move9000(f.toInt() - 1, t.toInt() - 1) }
		}

		return input.flatMap(::movesFrom)
	}

	fun moves9001From(input: List<String>): List<Move9001> {
		fun movesFrom(input: String): List<Move9001> {
			val (count, f, t) = """move (\d+) from (\d+) to (\d+)"""
				.toRegex()
				.find(input)!!
				.destructured

			return listOf(Move9001(f.toInt() - 1, t.toInt() - 1, count.toInt()))
		}

		return input.flatMap(::movesFrom)
	}

	fun makeMoves(moves: List<(List<MutableList<Char>>) -> Unit>, stacks: List<MutableList<Char>>) {
		moves.forEach { it(stacks) }
	}

	fun part1(input: List<String>): String {
		val stacks = stacksFrom(
			input.takeWhile { !it.contains('1') }
		)

		val moves = moves9000From(
			input.filter { it.startsWith("move ") }
		)

		makeMoves(moves, stacks)

		return stacks.joinToString(separator = "") {
			it.last()
				.toString()
		}
	}

	fun part2(input: List<String>): String {
		val stacks = stacksFrom(
			input.takeWhile { !it.contains('1') }
		)

		val moves = moves9001From(
			input.filter { it.startsWith("move ") }
		)

		makeMoves(moves, stacks)

		return stacks.joinToString(separator = "") {
			it.last()
				.toString()
		}
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day05_test")
	check(part1(testInput) == "CMZ")
	check(part2(testInput) == "MCD")

	val input = readInput("Day05")
	println(part1(input))
	println(part2(input))
}

data class Move9000(
	val from: Int,
	val to: Int,
) : (List<MutableList<Char>>) -> Unit {

	override operator fun invoke(input: List<MutableList<Char>>) {
		val last = input[from].last()
		input[from].removeLast()
		input[to].add(last)
	}
}

data class Move9001(
	val from: Int,
	val to: Int,
	val count: Int,
) : (List<MutableList<Char>>) -> Unit {

	override operator fun invoke(input: List<MutableList<Char>>) {
		val load = input[from].takeLast(count)
		input[from].removeLast(count)
		input[to].addAll(load)
	}

	private fun <T> MutableList<T>.removeLast(count: Int) {
		var c = count
		while (c > 0) {
			c--
			removeLast()
		}
	}
}