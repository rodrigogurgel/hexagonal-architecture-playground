package br.com.rodrigogurgel.playground.adapter.`in`.rest.controller

import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.AsyncMailCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.EmailCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.SmsCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.WhatsAppCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.mapper.toDomain
import br.com.rodrigogurgel.playground.application.port.`in`.MailSenderInputPort
import br.com.rodrigogurgel.playground.application.port.`in`.ProducerInputPort
import br.com.rodrigogurgel.playground.application.port.`in`.TransactionInputPort
import br.com.rodrigogurgel.playground.domain.Mail
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/mail")
class MailController(
    @Qualifier("emailService") private val emailSenderInputPort: MailSenderInputPort,
    @Qualifier("smsService") private val smsInputPort: MailSenderInputPort,
    @Qualifier("whatsAppService") private val whatsAppInputPort: MailSenderInputPort,
    private val mailProducerInputPort: ProducerInputPort<Mail>,
    private val transactionInputPort: TransactionInputPort,
) {
    @PostMapping("/email")
    suspend fun sendEmail(
        @RequestBody emailCommand: EmailCommand,
    ): Any {
        return emailCommand.toDomain()
            .andThen { transaction -> emailSenderInputPort.send(transaction).map { transaction } }
    }

    @PostMapping("/sms")
    suspend fun sendSms(
        @RequestBody smsCommand: SmsCommand,
    ) {
        smsCommand.toDomain().andThen { transaction -> smsInputPort.send(transaction) }
    }

    @PostMapping("/whatsapp")
    suspend fun sendWhatsApp(
        @RequestBody whatsAppCommand: WhatsAppCommand,
    ) {
        whatsAppCommand.toDomain().andThen { transaction -> whatsAppInputPort.send(transaction) }
    }

    @PostMapping("/async")
    suspend fun sendAsync(
        @RequestBody asyncMailCommand: AsyncMailCommand,
    ) {
        asyncMailCommand.toDomain().andThen { transaction -> mailProducerInputPort.produce(transaction) }
    }

    @GetMapping("/transaction/{transactionId}")
    suspend fun findTransactionById(@PathVariable transactionId: UUID): Any? {
        return transactionInputPort.findTransactionById<Mail>(transactionId)
    }
}
