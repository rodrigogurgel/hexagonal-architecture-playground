package br.com.rodrigogurgel.playground.application.service

import br.com.rodrigogurgel.playground.application.port.`in`.MailSenderInputPort
import br.com.rodrigogurgel.playground.application.port.out.mail.MailSenderOutputPort
import br.com.rodrigogurgel.playground.application.port.out.producer.ProducerOutputPort
import br.com.rodrigogurgel.playground.domain.Mail
import br.com.rodrigogurgel.playground.domain.Transaction
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import io.micrometer.core.annotation.Timed
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service("smsService")
class SmsService(
    @Qualifier("smsSender") private val smsOutputPort: MailSenderOutputPort,
    private val transactionService: TransactionService,
    private val producerOutputPort: ProducerOutputPort<Mail>,
) : MailSenderInputPort {
    private val logger = LoggerFactory.getLogger(SmsService::class.java)

    @Timed("sms_send_message")
    override suspend fun send(transaction: Transaction<Mail>): Result<Unit, Throwable> =
        transactionService.runTransaction(transaction) {
            smsOutputPort.send(transaction.data)
        }
            .onSuccess {
                logger.info("SMS sent with success")
                transaction.toSuccess()
                producerOutputPort.processed(transaction)
            }
            .onFailure { throwable ->
                logger.error("SMS sent with failure", throwable)
                transaction.toFailure(throwable.message ?: "Unknown error")
                producerOutputPort.processed(transaction)
            }
}
