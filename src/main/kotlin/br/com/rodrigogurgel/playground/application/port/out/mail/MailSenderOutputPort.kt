package br.com.rodrigogurgel.playground.application.port.out.mail

import br.com.rodrigogurgel.playground.domain.Mail
import com.github.michaelbull.result.Result

interface MailSenderOutputPort {
    suspend fun send(mail: Mail): Result<Unit, Throwable>
}
