package br.com.rodrigogurgel.playground.adapter.mapper.rest

import br.com.rodrigogurgel.playground.adapter.exception.MapperException
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.response.MailDataResponse
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.response.MailResponse
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.response.MailTypeResponse
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.response.TransactionResponse
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.response.TransactionStatusResponse
import br.com.rodrigogurgel.playground.domain.entity.Mail
import br.com.rodrigogurgel.playground.domain.entity.MailType
import br.com.rodrigogurgel.playground.domain.entity.TransactionStatus
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import java.time.OffsetDateTime
import java.time.ZoneId

fun Mail.toMailResponse(): Result<MailResponse, MapperException> = runCatching {
    MailResponse(
        id = id,
        data = data.run {
            MailDataResponse(
                body = body,
                from = from,
                subject = subject,
                to = to,
            )
        },
        transaction = transaction.run {
            TransactionResponse(
                correlationId = correlationId,
                status = status.toTransactionStatusResponse(),
                message = message,
                createdBy = createdBy,
                createdFrom = createdFrom,

                )
        },
        type = type.toMailTypeResponse(),
        sentAt = sentAt?.let { OffsetDateTime.ofInstant(it, ZoneId.systemDefault()) },
        createdAt = OffsetDateTime.ofInstant(createdAt, ZoneId.systemDefault()),
        updatedAt = OffsetDateTime.ofInstant(updatedAt, ZoneId.systemDefault()),
    )
}.mapError { throwable ->
    MapperException(
        "An error occurred while try convert Mail to MailResponse",
        throwable
    )
}

fun MailType.toMailTypeResponse(): MailTypeResponse = when (this) {
    MailType.EMAIL -> MailTypeResponse.EMAIL
    MailType.SMS -> MailTypeResponse.SMS
    MailType.WHATSAPP -> MailTypeResponse.WHATSAPP
}

fun TransactionStatus.toTransactionStatusResponse(): TransactionStatusResponse = when (this) {
    TransactionStatus.FAILURE -> TransactionStatusResponse.FAILURE
    TransactionStatus.SUCCESS -> TransactionStatusResponse.SUCCESS
    TransactionStatus.PROCESSING -> TransactionStatusResponse.PROCESSING
}
