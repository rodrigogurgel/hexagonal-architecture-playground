package br.com.rodrigogurgel.playground.application.port.`in`

import br.com.rodrigogurgel.playground.application.port.out.MailProducerOutputPort
import br.com.rodrigogurgel.playground.domain.entities.Mail
import br.com.rodrigogurgel.playground.domain.usecase.MailUseCase
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import org.springframework.stereotype.Service
import java.util.UUID

@Service("fallbackInputPort")
class FallbackInputPort(
    private val mailProducerOutputPort: MailProducerOutputPort,
) : MailUseCase {
    override suspend fun send(mail: Mail): Result<Unit, Throwable> = runSuspendCatching {
        mail.toFailure("No strategy found for that command! Please contact support")
        mailProducerOutputPort.processed(mail)
    }

    override suspend fun findMailById(id: UUID): Result<Mail?, Throwable> =
        Err(NotImplementedError())
}
