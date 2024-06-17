package br.com.rodrigogurgel.playground.application.port.`in`

import br.com.rodrigogurgel.playground.application.port.out.MailProducerOutputPort
import br.com.rodrigogurgel.playground.application.usecase.SendMailUseCase
import br.com.rodrigogurgel.playground.domain.entity.Mail
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service

@Service("asyncInputPort")
class AsyncInputPort(
    private val mailProducerOutputPort: MailProducerOutputPort,
) : SendMailUseCase {
    override suspend fun send(mail: Mail): Result<Unit, Throwable> =
        mailProducerOutputPort.command(mail)
}
