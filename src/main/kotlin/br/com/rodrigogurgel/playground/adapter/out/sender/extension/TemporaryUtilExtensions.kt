package br.com.rodrigogurgel.playground.adapter.out.sender.extension

import kotlinx.coroutines.delay

private const val NUMBER_100 = 100
private const val NUMBER_500 = 500
private const val NUMBER_1000 = 1000

suspend fun intermittent() {
    val randomMillis = (Math.random() * NUMBER_1000).toLong()
    val maxMillis = NUMBER_500
    val minMillis = NUMBER_100
    val millis = if (randomMillis in minMillis..maxMillis) randomMillis else maxMillis
    delay(millis.toLong())
    if (randomMillis > maxMillis) throw TimeoutException("Timeout Exception")
}

data class TimeoutException(override val message: String) : RuntimeException(message)
