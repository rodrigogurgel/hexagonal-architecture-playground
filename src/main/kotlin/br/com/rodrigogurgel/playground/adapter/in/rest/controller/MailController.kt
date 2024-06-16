package br.com.rodrigogurgel.playground.adapter.`in`.rest.controller

import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.MailCommand
import br.com.rodrigogurgel.playground.adapter.mapper.rest.toDomain
import br.com.rodrigogurgel.playground.domain.usecase.MailUseCase
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
    @Qualifier("emailInputPort") private val emailInputPort: MailUseCase,
    @Qualifier("smsInputPort") private val smsInputPort: MailUseCase,
    @Qualifier("whatsAppInputPort") private val whatsAppInputPort: MailUseCase,
    @Qualifier("asyncInputPort") private val asyncInputPort: MailUseCase,
) {
    @PostMapping("/email")
    suspend fun sendEmail(
        @RequestBody emailCommand: MailCommand.EmailCommand,
    ): Any {
        return emailCommand.toDomain()
            .andThen { transaction -> emailInputPort.send(transaction).map { transaction } }
    }

    @PostMapping("/sms")
    suspend fun sendSms(
        @RequestBody smsCommand: MailCommand.SmsCommand,
    ) {
        smsCommand.toDomain().andThen { transaction -> smsInputPort.send(transaction) }
    }

    @PostMapping("/whatsapp")
    suspend fun sendWhatsApp(
        @RequestBody whatsAppCommand: MailCommand.WhatsAppCommand,
    ) {
        whatsAppCommand.toDomain().andThen { mail -> whatsAppInputPort.send(mail) }
    }

    @PostMapping("/async")
    suspend fun sendAsync(
        @RequestBody asyncMailCommand: MailCommand.AsyncMailCommand,
    ) {
        asyncMailCommand.toDomain().andThen { mail -> asyncInputPort.send(mail) }
    }

    @GetMapping("/{mailId}")
    suspend fun findMailById(@PathVariable mailId: UUID): Any? {
        return emailInputPort.findMailById(mailId)
    }
}
