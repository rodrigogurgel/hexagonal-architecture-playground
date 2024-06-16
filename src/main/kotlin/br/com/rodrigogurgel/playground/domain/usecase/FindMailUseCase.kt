package br.com.rodrigogurgel.playground.domain.usecase

import br.com.rodrigogurgel.playground.domain.entity.Mail
import com.github.michaelbull.result.Result
import java.util.UUID

interface FindMailUseCase {
    suspend fun findMailById(id: UUID): Result<Mail?, Throwable>
}
