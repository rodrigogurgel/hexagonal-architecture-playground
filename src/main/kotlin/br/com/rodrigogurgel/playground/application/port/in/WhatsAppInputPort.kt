package br.com.rodrigogurgel.playground.application.port.`in`

import br.com.rodrigogurgel.playground.application.port.out.MailProducerOutputPort
import br.com.rodrigogurgel.playground.application.port.out.MailSenderOutputPort
import br.com.rodrigogurgel.playground.domain.entity.Mail
import br.com.rodrigogurgel.playground.domain.usecase.SendMailUseCase
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service("whatsAppInputPort")
class WhatsAppInputPort(
    @Qualifier("whatsAppSender") private val mailSenderOutputPort: MailSenderOutputPort,
    private val mailProducerOutputPort: MailProducerOutputPort,
) : SendMailUseCase {
    private val logger = LoggerFactory.getLogger(WhatsAppInputPort::class.java)

    @Timed("email_send_message")
    override suspend fun send(mail: Mail): Result<Unit, Throwable> = mailSenderOutputPort.send(mail)
        .onSuccess {
            logger.info("WhatsApp message sent with success")
            mail.toSentWithSuccess()
            mailProducerOutputPort.processed(mail)
        }
        .onFailure { throwable ->
            logger.error("WhatsApp message sent with failure", throwable)
            mail.toSentWithFailure(throwable.message ?: "Unknown error")
            mailProducerOutputPort.processed(mail)
        }
}
