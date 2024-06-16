package br.com.rodrigogurgel.playground.application.port.`in`

import br.com.rodrigogurgel.playground.application.port.out.MailProducerOutputPort
import br.com.rodrigogurgel.playground.domain.entity.Mail
import br.com.rodrigogurgel.playground.domain.usecase.SendMailUseCase
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import org.springframework.stereotype.Service

@Service("fallbackInputPort")
class FallbackInputPort(
    private val mailProducerOutputPort: MailProducerOutputPort,
) : SendMailUseCase {
    override suspend fun send(mail: Mail): Result<Unit, Throwable> = runSuspendCatching {
        mail.toSentWithFailure("No strategy found for that command! Please contact support")
        mailProducerOutputPort.processed(mail)
    }
}
