package br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl

import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.GenericRecordStrategy
import br.com.rodrigogurgel.playground.adapter.mapper.event.toDomain
import br.com.rodrigogurgel.playground.domain.usecase.MailUseCase
import br.com.rodrigogurgel.playground.`in`.event.dto.MailCommand
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import org.apache.avro.generic.GenericRecord

class FallbackStrategyImpl(
    private val isFallbackStrategy: Boolean,
    private val mailInputPort: MailUseCase,
) : GenericRecordStrategy<MailCommand> {
    override suspend fun process(value: MailCommand): Result<Unit, Throwable> = value.toDomain()
        .andThen { mail -> mailInputPort.send(mail) }

    override fun canProcess(record: GenericRecord): Boolean = false

    override fun isDefault() = isFallbackStrategy
}
