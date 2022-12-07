fun main() {
	val cd = "\\$ cd (.+)".toRegex()
	val dir = "dir (.+)".toRegex()
	val file = "(\\d+) (.+)".toRegex()
	val ls = "\\$ ls".toRegex()

	fun EditableDir.dir(name: String): EditableDir =
		object : EditableDir {
			override val children = mutableListOf<Node>()

			override val parent
				get() = this@dir

			override val name
				get() = name

			override fun toString(): String =
				"/$name $size"

			override val size: Int by lazy {
				children.sumOf { it.size }
			}
		}

	fun EditableDir.parse(input: Iterator<String>): Node {
		while (input.hasNext()) {
			val line = input.next()
			when {
				line matches ls   -> Unit

				line matches file -> {
					val (size, name) = file.find(line)!!.destructured
					children += Fl(this, name, size.toInt())
				}

				line matches dir  -> {
					val (name) = dir.find(line)!!.destructured
					children += dir(name)
				}

				line matches cd   -> {
					val (name) = cd.find(line)!!.destructured

					val parseDir =
						if (name == "..") parent
						else children.first { it.name == name } as EditableDir

					parseDir.parse(input)
				}
			}
		}

		return this
	}

	fun part1(root: EditableDir): Int {
		return root.iterable
			.filter { it is Dir && it.size < 100000 }
			.sumOf { it.size }
	}

	fun part2(root: EditableDir): Int {
		val alreadyFree = 70_000_000 - root.size
		val toFreeUp = 30_000_000 - alreadyFree

		return root.iterable
			.filter { it is Dir && it.size > toFreeUp }
			.minByOrNull { it.size }!!
			.size
	}

	fun root() =
		object : EditableDir {
			override val parent: EditableDir = this
			override val children = mutableListOf<Node>()
			override val name: String = "/"

			override val size: Int by lazy { children.sumOf { it.size } }

			override fun toString(): String {
				return "/"
			}
		}

	val testRoot = root()
		.apply {
			val inputIterator = readInput("Day07_test").iterator()

			inputIterator.next()
			parse(inputIterator)
		}

	check(part1(testRoot) == 95437)
	check(part2(testRoot) == 24933642)

	val solutionRoot = root()
		.apply {
			val inputIterator = readInput("Day07").iterator()

			inputIterator.next()
			parse(inputIterator)
		}

	println(part1(solutionRoot))
	println(part2(solutionRoot))

}

sealed interface Node {

	val parent: Node
	val name: String
	val size: Int
}

data class Fl(
	override val parent: Node,
	override val name: String,
	override val size: Int,
) : Node {

	override fun toString(): String {
		return "$name $size"
	}
}

interface Dir : Node {

	val children: List<Node>
}

interface EditableDir : Dir {

	override val parent: EditableDir

	override val children: MutableList<Node>
}

val Node.iterable
	get() = Iterable { nodeIterator(this) }

fun nodeIterator(node: Node): Iterator<Node> {
	return when (node) {
		is Fl  -> iterator { yield(node) }
		is Dir -> iterator {
			yield(node)
			node.children
				.forEach { yieldAll(nodeIterator(it)) }
		}
	}
}

fun printNode(prefix: String, node: Node) {
	println("$prefix$node")

	when (node) {
		is Fl  -> Unit
		is Dir -> node.children.forEach { printNode("    $prefix", it) }
	}
}