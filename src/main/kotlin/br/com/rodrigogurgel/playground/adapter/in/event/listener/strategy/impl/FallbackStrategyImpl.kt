package br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl

import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.GenericRecordStrategy
import br.com.rodrigogurgel.playground.adapter.`in`.event.mapper.toDomain
import br.com.rodrigogurgel.playground.application.port.`in`.ProducerInputPort
import br.com.rodrigogurgel.playground.domain.Mail
import br.com.rodrigogurgel.playground.`in`.event.dto.MailCommand
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.coroutines.runSuspendCatching
import org.apache.avro.generic.GenericRecord

class FallbackStrategyImpl(
    private val isFallbackStrategy: Boolean,
    private val mailProducerInputPort: ProducerInputPort<Mail>,
) : GenericRecordStrategy<MailCommand> {
    override suspend fun process(value: MailCommand): Result<Unit, Throwable> = runSuspendCatching {
        value.toDomain()
            .andThen { transaction ->
                transaction.toFailure("No strategy found for that command! Please contact support")
                mailProducerInputPort.produce(transaction)
            }
    }

    override fun canProcess(record: GenericRecord): Boolean = false

    override fun isDefault() = isFallbackStrategy
}
