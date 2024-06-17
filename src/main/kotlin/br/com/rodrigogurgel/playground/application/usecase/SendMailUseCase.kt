package br.com.rodrigogurgel.playground.application.usecase

import br.com.rodrigogurgel.playground.domain.entity.Mail
import com.github.michaelbull.result.Result

interface SendMailUseCase {
    suspend fun send(mail: Mail): Result<Unit, Throwable>
}
