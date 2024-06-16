package br.com.rodrigogurgel.playground.adapter.mapper.event

import br.com.rodrigogurgel.playground.adapter.exception.MapperException
import br.com.rodrigogurgel.playground.adapter.`in`.extension.toUUID
import br.com.rodrigogurgel.playground.domain.entity.Mail
import br.com.rodrigogurgel.playground.domain.entity.MailData
import br.com.rodrigogurgel.playground.domain.entity.MailType
import br.com.rodrigogurgel.playground.domain.entity.Transaction
import br.com.rodrigogurgel.playground.domain.entity.TransactionStatus
import br.com.rodrigogurgel.playground.`in`.event.dto.MailCommand
import br.com.rodrigogurgel.playground.`in`.event.dto.MailDataCommand
import br.com.rodrigogurgel.playground.`in`.event.dto.MailTypeCommand
import br.com.rodrigogurgel.playground.`in`.event.dto.TransactionCommand
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord
import java.time.Instant
import java.util.UUID

fun MailCommand.toDomain(): Result<Mail, Throwable> = runCatching {
    Mail(
        id = UUID.randomUUID(),
        data = data.run {
            MailData(
                body = body.toString(),
                from = from.toString(),
                subject = subject.toString(),
                to = to.toString(),
            )
        },
        transaction = transaction.run {
            Transaction(
                correlationId = correlationId.toUUID(),
                status = TransactionStatus.PROCESSING,
                createdBy = createdBy.toString(),
                createdFrom = createdFrom.toString(),
            )
        },
        type = type.toDomain()
    )
}.mapError { throwable ->
    MapperException(
        "An error occurred while try convert MailCommand to Mai",
        throwable
    )
}

fun MailTypeCommand.toDomain(): MailType = when (this) {
    MailTypeCommand.SMS -> MailType.SMS
    MailTypeCommand.EMAIL -> MailType.EMAIL
    MailTypeCommand.WHATSAPP -> MailType.WHATSAPP
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
    MailType.EMAIL -> MailTypeCommand.EMAIL
    MailType.SMS -> MailTypeCommand.SMS
    MailType.WHATSAPP -> MailTypeCommand.WHATSAPP
}
