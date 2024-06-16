package br.com.rodrigogurgel.playground.adapter.mapper.rest

import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.MailCommand.AsyncMailCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.MailCommand.EmailCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.MailCommand.SmsCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.MailCommand.WhatsAppCommand
import br.com.rodrigogurgel.playground.adapter.`in`.rest.dto.command.MailTypeCommand
import br.com.rodrigogurgel.playground.domain.entities.Mail
import br.com.rodrigogurgel.playground.domain.entities.MailData
import br.com.rodrigogurgel.playground.domain.entities.MailType
import br.com.rodrigogurgel.playground.domain.entities.Transaction
import br.com.rodrigogurgel.playground.exception.MapperException
import br.com.rodrigogurgel.playground.`in`.event.dto.MailCommand
import br.com.rodrigogurgel.playground.`in`.event.dto.MailDataCommand
import br.com.rodrigogurgel.playground.`in`.event.dto.TransactionCommand
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord
import java.time.Instant
import java.util.UUID

fun EmailCommand.toDomain(): Result<Mail, Throwable> = runCatching {
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
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            )
        },
        type = MailType.EMAIL,
    )
}.mapError { throwable ->
    MapperException(
        "Something went wrong while try convert EmailCommand to Mai",
        throwable
    )
}

fun SmsCommand.toDomain(): Result<Mail, Throwable> = runCatching {
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
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            )
        },
        type = MailType.SMS,
    )
}.mapError { throwable ->
    MapperException(
        "Something went wrong while try convert SmsCommand to Mai",
        throwable
    )
}

fun WhatsAppCommand.toDomain(): Result<Mail, Throwable> = runCatching {
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
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            )
        },
        type = MailType.WHATSAPP,
    )
}.mapError { throwable ->
    MapperException(
        "Something went wrong while try convert WhatsAppCommand to Mai",
        throwable
    )
}

fun AsyncMailCommand.toDomain(): Result<Mail, Throwable> = runCatching {
    transaction.run {
        Mail(
            id = UUID.randomUUID(),
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
                    createdAt = Instant.now(),
                    updatedAt = Instant.now(),
                )
            },
            type = type.toDomain(),
        )
    }
}.mapError { throwable ->
    MapperException(
        "Something went wrong while try convert WhatsAppCommand to Mai",
        throwable
    )
}

fun Mail.toMailCommand(): Result<GenericRecord, MapperException> = runCatching {
    MailCommand.newBuilder()
        .setTransaction(
            transaction.run {
                TransactionCommand.newBuilder()
                    .setCorrelationId(correlationId.toString())
                    .setCreatedBy(createdBy)
                    .setCreatedFrom(createdFrom)
                    .build()
            }
        )
        .setData(
            data.run {
                MailDataCommand.newBuilder()
                    .setBody(body)
                    .setFrom(from)
                    .setSubject(subject)
                    .setTo(to)
                    .build()
            }
        )
        .setType(type.toMailTypeCommand())
        .build()
}.mapError { throwable ->
    MapperException(
        "An error occurred while try convert Mai to MailCommand",
        throwable
    )
}

fun MailType.toMailTypeCommand() = when (this) {
    MailType.EMAIL -> br.com.rodrigogurgel.playground.`in`.event.dto.MailTypeCommand.EMAIL
    MailType.SMS -> br.com.rodrigogurgel.playground.`in`.event.dto.MailTypeCommand.SMS
    MailType.WHATSAPP -> br.com.rodrigogurgel.playground.`in`.event.dto.MailTypeCommand.WHATSAPP
}

fun MailTypeCommand.toDomain(): MailType = when (this) {
    MailTypeCommand.SMS -> MailType.SMS
    MailTypeCommand.EMAIL -> MailType.EMAIL
    MailTypeCommand.WHATSAPP -> MailType.WHATSAPP
}
