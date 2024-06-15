package br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy

import com.github.michaelbull.result.Result
import org.apache.avro.generic.GenericRecord

interface GenericRecordStrategy<out T : GenericRecord> {
    suspend fun process(value: @UnsafeVariance T): Result<Unit, Throwable>
    fun canProcess(record: GenericRecord): Boolean
    fun isDefault(): Boolean = false
}
