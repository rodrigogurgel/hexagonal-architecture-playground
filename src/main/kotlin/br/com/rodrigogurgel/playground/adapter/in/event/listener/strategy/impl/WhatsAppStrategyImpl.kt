package br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl

import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.GenericRecordStrategy
import br.com.rodrigogurgel.playground.adapter.mapper.event.toDomain
import br.com.rodrigogurgel.playground.domain.usecase.MailUseCase
import br.com.rodrigogurgel.playground.`in`.event.dto.MailCommand
import br.com.rodrigogurgel.playground.`in`.event.dto.MailTypeCommand
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import org.apache.avro.generic.GenericRecord
import org.springframework.beans.factory.annotation.Qualifier

class WhatsAppStrategyImpl(
    @Qualifier("whatsAppService") private val whatsAppInputPort: MailUseCase,
) : GenericRecordStrategy<MailCommand> {
    override suspend fun process(value: MailCommand): Result<Unit, Throwable> = value.toDomain()
        .andThen { mail -> whatsAppInputPort.send(mail) }

    override fun canProcess(record: GenericRecord): Boolean {
        return record is MailCommand && record.type == MailTypeCommand.WHATSAPP
    }
}
