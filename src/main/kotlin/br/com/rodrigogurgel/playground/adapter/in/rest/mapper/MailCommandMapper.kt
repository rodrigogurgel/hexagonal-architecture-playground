package br.com.rodrigogurgel.playground.adapter.`in`.rest.mapper

import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.AsyncMailCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.EmailCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.MailTypeCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.SmsCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.WhatsAppCommand
import br.com.rodrigogurgel.playground.application.exception.common.MapperException
import br.com.rodrigogurgel.playground.domain.Mail
import br.com.rodrigogurgel.playground.domain.MailType
import br.com.rodrigogurgel.playground.domain.Transaction
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import java.time.Instant

fun EmailCommand.toDomain(): Result<Transaction<Mail>, Throwable> = runCatching {
    transaction.run {
        Transaction(
            transactionId = transactionId,
            correlationId = correlationId,
            data = data.run {
                Mail(
                    body = body,
                    from = from,
                    subject = subject,
                    to = to,
                    type = MailType.EMAIL,
                )
            },
            createdBy = createdBy,
            createdFrom = "PLAYGROUND_API",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )
    }
}.mapError { throwable ->
    MapperException(
        "Something went wrong while try convert EmailCommand to Transaction<Mail>",
        throwable
    )
}

fun SmsCommand.toDomain(): Result<Transaction<Mail>, Throwable> = runCatching {
    transaction.run {
        Transaction(
            transactionId = transactionId,
            correlationId = correlationId,
            data = data.run {
                Mail(
                    body = body,
                    from = from,
                    subject = subject,
                    to = to,
                    type = MailType.SMS,
                )
            },
            createdBy = createdBy,
            createdFrom = "PLAYGROUND_API",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )
    }
}.mapError { throwable ->
    MapperException(
        "Something went wrong while try convert SmsCommand to Transaction<Mail>",
        throwable
    )
}

fun WhatsAppCommand.toDomain(): Result<Transaction<Mail>, Throwable> = runCatching {
    transaction.run {
        Transaction(
            transactionId = transactionId,
            correlationId = correlationId,
            data = data.run {
                Mail(
                    body = body,
                    from = from,
                    subject = subject,
                    to = to,
                    type = MailType.WHATSAPP,
                )
            },
            createdBy = createdBy,
            createdFrom = "PLAYGROUND_API",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )
    }
}.mapError { throwable ->
    MapperException(
        "Something went wrong while try convert WhatsAppCommand to Transaction<Mail>",
        throwable
    )
}

fun AsyncMailCommand.toDomain(): Result<Transaction<Mail>, Throwable> = runCatching {
    transaction.run {
        Transaction(
            transactionId = transactionId,
            correlationId = correlationId,
            data = data.run {
                Mail(
                    body = body,
                    from = from,
                    subject = subject,
                    to = to,
                    type = type.toDomain(),
                )
            },
            createdBy = createdBy,
            createdFrom = "PLAYGROUND_API",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
        )
    }
}.mapError { throwable ->
    MapperException(
        "Something went wrong while try convert WhatsAppCommand to Transaction<Mail>",
        throwable
    )
}

fun MailTypeCommand.toDomain(): MailType = when (this) {
    MailTypeCommand.SMS -> MailType.SMS
    MailTypeCommand.EMAIL -> MailType.EMAIL
    MailTypeCommand.WHATSAPP -> MailType.WHATSAPP
}
