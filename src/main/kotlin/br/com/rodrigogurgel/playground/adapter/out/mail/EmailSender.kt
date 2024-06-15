package br.com.rodrigogurgel.playground.adapter.out.mail

import br.com.rodrigogurgel.playground.application.port.out.mail.MailSenderOutputPort
import br.com.rodrigogurgel.playground.application.utils.intermittent
import br.com.rodrigogurgel.playground.domain.Mail
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import org.springframework.stereotype.Component

@Component("emailSender")
class EmailSender : MailSenderOutputPort {
    override suspend fun send(mail: Mail): Result<Unit, Throwable> = runSuspendCatching {
        intermittent()
        mail.setAsSent()
    }
}