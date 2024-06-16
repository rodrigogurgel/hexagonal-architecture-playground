package br.com.rodrigogurgel.playground.adapter.mapper.rest

import br.com.rodrigogurgel.playground.adapter.exception.MapperException
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.request.MailRequest
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.request.MailTypeRequest
import br.com.rodrigogurgel.playground.domain.entity.Mail
import br.com.rodrigogurgel.playground.domain.entity.MailData
import br.com.rodrigogurgel.playground.domain.entity.MailType
import br.com.rodrigogurgel.playground.domain.entity.Transaction
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import java.time.Instant

fun MailRequest.toDomain(type: MailTypeRequest): Result<Mail, Throwable> = runCatching {
    Mail(
        id = id,
        data = data.run {
            MailData(
                body = body,
                from = from,
                subject = subject,
                to = to,
            )
        },
        transaction = transaction.run {
            Transaction(
                correlationId = correlationId,
                createdBy = createdBy,
                createdFrom = "PLAYGROUND_API",
            )
        },
        type = type.toDomain(),
    )
}.mapError { throwable ->
    MapperException(
        "Something went wrong while try convert EmailCommand to Mail",
        throwable
    )
}

fun MailTypeRequest.toDomain(): MailType = when (this) {
    MailTypeRequest.SMS -> MailType.SMS
    MailTypeRequest.EMAIL -> MailType.EMAIL
    MailTypeRequest.WHATSAPP -> MailType.WHATSAPP
}
