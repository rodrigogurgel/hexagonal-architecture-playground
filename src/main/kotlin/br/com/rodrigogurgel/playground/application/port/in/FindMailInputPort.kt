package br.com.rodrigogurgel.playground.application.port.`in`

import br.com.rodrigogurgel.playground.application.port.out.MailDatastoreOutputPort
import br.com.rodrigogurgel.playground.application.usecase.FindMailUseCase
import br.com.rodrigogurgel.playground.domain.entity.Mail
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FindMailInputPort(
    private val mailDatastoreOutputPort: MailDatastoreOutputPort,
) : FindMailUseCase {
    override suspend fun findMailById(id: UUID): Result<Mail?, Throwable> = mailDatastoreOutputPort.findMailById(id)
}
