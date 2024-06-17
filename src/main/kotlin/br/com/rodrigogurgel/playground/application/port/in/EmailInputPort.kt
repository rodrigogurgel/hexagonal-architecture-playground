package br.com.rodrigogurgel.playground.application.port.`in`

import br.com.rodrigogurgel.playground.application.port.out.MailProducerOutputPort
import br.com.rodrigogurgel.playground.application.port.out.MailSenderOutputPort
import br.com.rodrigogurgel.playground.application.usecase.SendMailUseCase
import br.com.rodrigogurgel.playground.domain.entity.Mail
import br.com.rodrigogurgel.playground.domain.policy.EmailValidator
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.github.michaelbull.result.runCatching
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service("emailInputPort")
class EmailInputPort(
    @Qualifier("emailSender") private val mailSenderOutputPort: MailSenderOutputPort,
    private val mailProducerOutputPort: MailProducerOutputPort,
) : SendMailUseCase {
    private val logger = LoggerFactory.getLogger(EmailInputPort::class.java)

    @Timed("email_send_message")
    override suspend fun send(mail: Mail): Result<Unit, Throwable> =
        runCatching { mail.validate(EmailValidator) }
            .andThen { mailSenderOutputPort.send(mail) }
            .onSuccess {
                logger.info("Email sent with success")
                mail.toSentWithSuccess()
                mailProducerOutputPort.processed(mail)
            }
            .onFailure { throwable ->
                logger.error("Email sent with failure", throwable)
                mail.toSentWithFailure(throwable.message ?: "Unknown error")
                mailProducerOutputPort.processed(mail)
            }
}
