fun main() {
	fun List<String>.sumsBetweenEmpty(): MutableList<Int> =
		fold(mutableListOf(0)) { acc, s ->
			acc += if (s.isEmpty()) {
				0
			} else {
				acc.pop() + s.toInt()
			}

			acc
		}

	fun part1(input: List<String>): Int =
		input.sumsBetweenEmpty()
			.max()

	fun part2(input: List<String>): Int =
		input.sumsBetweenEmpty()
			.sortedDescending()
			.take(3)
			.sum()

	fun <T : Comparable<T>> List<T>.getTop(count: Int): List<T> {
		tailrec fun <T : Comparable<T>> List<T>.getTopRec(found: List<T>, count: Int, rest: List<T>): List<T> {

			val point: T by lazy { random() }
			val pair: Pair<List<T>, List<T>> by lazy { partition { it > point } }

			val right by lazy { pair.first }
			val left by lazy { pair.second }


			return when (count) {
				0          -> found
				1          -> found + max()
				right.size -> found + right

				else       -> {
					val receiver: List<T>
					val f: List<T>
					val c: Int
					val r: List<T>

					when {
						right.size > count -> {
							receiver = right
							f = found
							c = count
							r = emptyList()
						}

						this.size > count  -> {
							receiver = left
							f = found + right
							c = count - right.size
							r = emptyList()
						}

						else               -> {
							receiver = left + rest
							f = found + right
							c = count - right.size
							r = emptyList()
						}
					}

					receiver.getTopRec(f, c, r)
				}
			}
		}

		return getTopRec(emptyList(), count, emptyList())
	}


	fun part1Rec(input: List<String>): Int =
		input.sumsBetweenEmpty()
			.getTop(1)
			.sum()

	fun part2Rec(input: List<String>): Int =
		input.sumsBetweenEmpty()
			.getTop(3)
			.sum()

	val testInput = readInput("Day01_test")
	check(part1(testInput) == 24000)
	check(part1Rec(testInput) == 24000)

	check(part2(testInput) == 45000)
	check(part2Rec(testInput) == 45000)

	val input = readInput("Day01")
	println(part1(input))
	println(part1Rec(input))

	println(part2(input))
	println(part2Rec(input))
}
