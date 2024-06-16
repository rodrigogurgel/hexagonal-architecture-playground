package br.com.rodrigogurgel.playground.adapter.out.sender

import br.com.rodrigogurgel.playground.adapter.out.sender.extension.intermittent
import br.com.rodrigogurgel.playground.application.port.out.MailSenderOutputPort
import br.com.rodrigogurgel.playground.domain.entities.Mail
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
