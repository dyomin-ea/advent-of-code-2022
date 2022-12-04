import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) =
	File("src", "$name.txt")
		.readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() =
	BigInteger(
		1,
		MessageDigest.getInstance("MD5")
			.digest(toByteArray())
	)
		.toString(16)
		.padStart(32, '0')

fun <T> MutableList<T>.pop(): T =
	last().also { removeLast() }

fun String.splitBy(delimiter: String): Pair<String, String> =
	substringBefore(delimiter) to substringAfter(delimiter)

fun <L, R, M> Pair<L, R>.mapLeft(mapper: (L) -> M): Pair<M, R> =
	mapper(first) to second

fun <L, R, M> Pair<L, R>.map(mapper: (R) -> M): Pair<L, M> =
	first to mapper(second)