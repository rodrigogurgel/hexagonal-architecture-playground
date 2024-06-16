package br.com.rodrigogurgel.playground.application.port.`in`

import br.com.rodrigogurgel.playground.application.port.out.MailDatastoreOutputPort
import br.com.rodrigogurgel.playground.application.port.out.MailProducerOutputPort
import br.com.rodrigogurgel.playground.application.port.out.MailSenderOutputPort
import br.com.rodrigogurgel.playground.domain.entities.Mail
import br.com.rodrigogurgel.playground.domain.usecase.MailUseCase
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.UUID

@Service("smsInputPort")
class SmsInputPort(
    @Qualifier("smsSender") private val mailSenderOutputPort: MailSenderOutputPort,
    private val mailProducerOutputPort: MailProducerOutputPort,
    private val mailDatastoreOutputPort: MailDatastoreOutputPort,
) : MailUseCase {
    private val logger = LoggerFactory.getLogger(SmsInputPort::class.java)

    @Timed("sms_send_message")
    override suspend fun send(mail: Mail): Result<Unit, Throwable> =
        mailSenderOutputPort.send(mail)
            .onSuccess {
                logger.info("SMS sent with success")
                mail.toSuccess()
                mailProducerOutputPort.processed(mail)
            }
            .onFailure { throwable ->
                logger.error("SMS sent with failure", throwable)
                mail.toFailure(throwable.message ?: "Unknown error")
                mailProducerOutputPort.processed(mail)
            }

    override suspend fun findMailById(id: UUID): Result<Mail?, Throwable> = mailDatastoreOutputPort.findMailById(id)
}
