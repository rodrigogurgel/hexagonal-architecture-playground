package br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.extension

import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.GenericRecordStrategy
import br.com.rodrigogurgel.playground.exception.DefaultStrategyNotFoundException
import br.com.rodrigogurgel.playground.exception.MultipleStrategyMatchException
import br.com.rodrigogurgel.playground.exception.StrategyNotFoundException
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord

fun <T : GenericRecord> List<GenericRecordStrategy<T>>.findStrategy(
    record: GenericRecord,
): Result<GenericRecordStrategy<T>, Throwable> = runCatching {
    val strategiesMatch = filter { it.canProcess(record) }

    if (strategiesMatch.size > 1) throw MultipleStrategyMatchException()
    if (strategiesMatch.isEmpty()) throw StrategyNotFoundException()

    strategiesMatch.first()
}

fun <T : GenericRecord> List<GenericRecordStrategy<T>>.default() = runCatching {
    firstOrNull { it.isDefault() } ?: throw DefaultStrategyNotFoundException()
}
