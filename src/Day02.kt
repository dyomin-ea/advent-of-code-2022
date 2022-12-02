fun main() {
    /*
    * A X rock
    * B Y paper
    * C Z scissors
    * */
    fun pointsOf(value: String): Int =
        when (value) {
            "A", "X" -> 1
            "B", "Y" -> 2
            "C", "Z" -> 3
            else     -> error("unknown value")
        }

    fun getScore(l: String, r: String): Int {
        return when (l to r) {
            "A" to "X", "B" to "Y", "C" to "Z" -> 3
            "A" to "Y", "B" to "Z", "C" to "X" -> 6
            "A" to "Z", "B" to "X", "C" to "Y" -> 0
            else                               -> error("unknown value")
        } + pointsOf(r)
    }

    fun String.win(): String =
        when (this) {
            "A"  -> "B"
            "B"  -> "C"
            "C"  -> "A"
            else -> error("unknown value")
        }

    fun String.loose(): String =
        when (this) {
            "A"  -> "C"
            "B"  -> "A"
            "C"  -> "B"
            else -> error("unknown value")
        }

    fun getTrueScore(l: String, r: String): Int =
        when (r) {
            "X"  -> 0 + pointsOf(l.loose())
            "Y"  -> 3 + pointsOf(l)
            "Z"  -> 6 + pointsOf(l.win())
            else -> error("unknown value")
        }

    fun part1(input: List<String>): Int =
        input.sumOf {
            getScore(
                it.substringBefore(" "),
                it.substringAfter(" ")
            )
        }

    fun part2(input: List<String>): Int =
        input.sumOf {
            getTrueScore(
                it.substringBefore(" "),
                it.substringAfter(" ")
            )
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}