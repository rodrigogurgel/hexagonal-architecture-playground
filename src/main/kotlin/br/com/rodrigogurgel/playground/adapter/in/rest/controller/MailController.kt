package br.com.rodrigogurgel.playground.adapter.`in`.rest.controller

import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.request.AsyncMailRequest
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.request.MailRequest
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.request.MailTypeRequest
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.response.MailResponse
import br.com.rodrigogurgel.playground.adapter.mapper.rest.toDomain
import br.com.rodrigogurgel.playground.adapter.mapper.rest.toMailResponse
import br.com.rodrigogurgel.playground.domain.usecase.FindMailUseCase
import br.com.rodrigogurgel.playground.domain.usecase.SendMailUseCase
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapBoth
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    @Qualifier("emailInputPort") private val emailInputPort: SendMailUseCase,
    @Qualifier("smsInputPort") private val smsInputPort: SendMailUseCase,
    @Qualifier("whatsAppInputPort") private val whatsAppInputPort: SendMailUseCase,
    @Qualifier("asyncInputPort") private val asyncInputPort: SendMailUseCase,
    private val findMailUseCase: FindMailUseCase,
) {
    private val logger = LoggerFactory.getLogger(MailController::class.java)

    @PostMapping("/email")
    suspend fun sendEmail(
        @RequestBody mailRequest: MailRequest,
    ): ResponseEntity<MailResponse> = emailInputPort.sendMail(MailTypeRequest.EMAIL, mailRequest)

    @PostMapping("/sms")
    suspend fun sendSms(
        @RequestBody mailRequest: MailRequest,
    ): ResponseEntity<MailResponse> = smsInputPort.sendMail(MailTypeRequest.SMS, mailRequest)

    @PostMapping("/whatsapp")
    suspend fun sendWhatsApp(
        @RequestBody mailRequest: MailRequest,
    ): ResponseEntity<MailResponse> = whatsAppInputPort.sendMail(MailTypeRequest.WHATSAPP, mailRequest)

    @PostMapping("/async")
    suspend fun sendAsync(
        @RequestBody mailRequest: AsyncMailRequest,
    ): ResponseEntity<MailResponse> = asyncInputPort.sendMail(mailRequest.type, mailRequest)

    @GetMapping("/{mailId}")
    suspend fun findMailById(@PathVariable mailId: UUID): ResponseEntity<MailResponse?> {
        return findMailUseCase.findMailById(mailId)
            .andThen { mail -> mail?.toMailResponse() ?: Ok(null) }
            .mapBoth({ response ->
                response?.run {
                    ResponseEntity.status(HttpStatus.OK).body(response)
                } ?: ResponseEntity.notFound().build()
            }, {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
            })
    }

    private suspend fun SendMailUseCase.sendMail(
        type: MailTypeRequest,
        mailRequest: MailRequest,
    ): ResponseEntity<MailResponse> =
        mailRequest.toDomain(type)
            .andThen { mail -> send(mail).map { mail } }
            .andThen { mail -> mail.toMailResponse() }
            .mapBoth({ response ->
                ResponseEntity.status(HttpStatus.OK).body(response)
            }, { throwable ->
                logger.error("", throwable)
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
            })
}
