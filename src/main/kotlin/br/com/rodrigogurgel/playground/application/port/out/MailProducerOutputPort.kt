package br.com.rodrigogurgel.playground.application.port.out

import br.com.rodrigogurgel.playground.domain.entities.Mail
import com.github.michaelbull.result.Result

interface MailProducerOutputPort {
    suspend fun processed(mail: Mail): Result<Unit, Throwable>
    suspend fun command(mail: Mail): Result<Unit, Throwable>
}
