interface Tree {

	val height: Int

	val u: Tree
	val d: Tree
	val l: Tree
	val r: Tree
}

fun main() {

	operator fun List<String>.get(r: Int, c: Int): Int =
		this[r][c] - '0'

	operator fun <V> MutableMap<Pair<Int, Int>, V>.set(r: Int, c: Int, value: V) {
		set(r to c, value)
	}

	val plain = object : Tree {
		override val height = -1
		override val u = this
		override val d = this
		override val l = this
		override val r = this
	}

	fun Tree.iterateBy(body: (Tree) -> Tree): Iterable<Tree> {
		var pointer = this
		val iterator = iterator {
			while (pointer != plain) {
				pointer = body(pointer)
				if (pointer != plain) {
					yield(pointer)
				}
			}
		}

		return Iterable { iterator }
	}

	fun Tree.isShaded(): Boolean =
		iterateBy { it.u }.any { it.height >= height } &&
				iterateBy { it.d }.any { it.height >= height } &&
				iterateBy { it.l }.any { it.height >= height } &&
				iterateBy { it.r }.any { it.height >= height }


	fun Tree.onWay(next: (Tree) -> Tree): Int {
		var result = 0

		for (tree in iterateBy(next)) {
			when {
				tree.height < height  -> result++

				tree.height >= height -> {
					result++
					break
				}
			}
		}

		return result
	}

	fun Tree.score(): Int =
		onWay(Tree::u) *
				onWay(Tree::d) *
				onWay { it.l } *
				onWay(Tree::r)

	fun resolve(input: List<String>): List<Tree> {
		val cache = mutableMapOf<Pair<Int, Int>, Tree>()
		val maxRow = input.lastIndex
		val maxCol = input.first().lastIndex

		operator fun MutableMap<Pair<Int, Int>, Tree>.get(r: Int, c: Int): Tree? =
			if (r < 0 || c < 0 || r > maxRow || c > maxCol) {
				plain
			} else {
				get(r to c)
			}

		fun tree(row: Int, col: Int): Tree =
			cache[row, col] ?: object : Tree {

				override val height = input[row, col]

				override val u by lazy { tree(row + 1, col) }
				override val d by lazy { tree(row - 1, col) }
				override val l by lazy { tree(row, col - 1) }
				override val r by lazy { tree(row, col + 1) }

				init {
					cache[row, col] = this
				}
			}

		val cols = input.first().indices
		return input.indices.flatMap { r -> cols.map { c -> tree(r, c) } }
	}

	fun part1(input: List<String>): Int {
		return resolve(input).count { !it.isShaded() }
	}

	fun part2(input: List<String>): Int =
		resolve(input)
			.filter { !it.isShaded() }
			.maxOf { it.score() }

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day08_test")
	check(part1(testInput) == 21)
	check(part2(testInput) == 8) {
		resolve(testInput)
			.map { it.score() }
			.chunked(5)
			.joinToString(prefix = "\n", separator = "\n") { it.joinToString(separator = " ") }
	}

	val input = readInput("Day08")
	println(part1(input))
	println(part2(input))
}