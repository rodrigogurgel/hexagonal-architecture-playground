package br.com.rodrigogurgel.playground.application.port.out

import br.com.rodrigogurgel.playground.domain.entities.Mail
import com.github.michaelbull.result.Result

interface MailSenderOutputPort {
    suspend fun send(mail: Mail): Result<Unit, Throwable>
}
