package br.com.rodrigogurgel.playground.application.port.out

import br.com.rodrigogurgel.playground.domain.entity.Mail
import com.github.michaelbull.result.Result
import java.util.UUID

interface MailDatastoreOutputPort {
    suspend fun findMailById(id: UUID): Result<Mail?, Throwable>
}
