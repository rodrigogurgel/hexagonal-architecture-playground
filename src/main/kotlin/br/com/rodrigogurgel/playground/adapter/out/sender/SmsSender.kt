package br.com.rodrigogurgel.playground.adapter.out.sender

import br.com.rodrigogurgel.playground.adapter.out.sender.extension.intermittent
import br.com.rodrigogurgel.playground.application.port.out.MailSenderOutputPort
import br.com.rodrigogurgel.playground.domain.entity.Mail
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import org.springframework.stereotype.Component

@Component("smsSender")
class SmsSender : MailSenderOutputPort {
    override suspend fun send(mail: Mail): Result<Unit, Throwable> = runSuspendCatching {
        intermittent()
    }
}
