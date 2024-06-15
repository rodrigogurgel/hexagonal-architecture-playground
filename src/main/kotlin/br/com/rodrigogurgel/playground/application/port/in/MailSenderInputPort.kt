package br.com.rodrigogurgel.playground.application.port.`in`

import br.com.rodrigogurgel.playground.domain.Mail
import br.com.rodrigogurgel.playground.domain.Transaction
import com.github.michaelbull.result.Result

interface MailSenderInputPort {
    suspend fun send(transaction: Transaction<Mail>): Result<Unit, Throwable>
}
