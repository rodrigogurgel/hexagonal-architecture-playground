package br.com.rodrigogurgel.playground.domain.usecase

import br.com.rodrigogurgel.playground.domain.entities.Mail
import com.github.michaelbull.result.Result
import java.util.UUID

interface MailUseCase {
    suspend fun send(mail: Mail): Result<Unit, Throwable>
    suspend fun findMailById(id: UUID): Result<Mail?, Throwable>
}
