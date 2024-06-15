package br.com.rodrigogurgel.playground.application.service

import br.com.rodrigogurgel.playground.application.port.`in`.ProducerInputPort
import br.com.rodrigogurgel.playground.application.port.out.producer.ProducerOutputPort
import br.com.rodrigogurgel.playground.domain.Mail
import br.com.rodrigogurgel.playground.domain.Transaction
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service

@Service
class MailProducerService(
    private val mailProducerOutputPort: ProducerOutputPort<Mail>,
) : ProducerInputPort<Mail> {
    override suspend fun produce(mail: Transaction<Mail>): Result<Unit, Throwable> =
        mailProducerOutputPort.command(mail)
}
