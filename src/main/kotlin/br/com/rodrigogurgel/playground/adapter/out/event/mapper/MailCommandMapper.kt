package br.com.rodrigogurgel.playground.adapter.out.event.mapper

import br.com.rodrigogurgel.playground.domain.Mail
import br.com.rodrigogurgel.playground.domain.MailType
import br.com.rodrigogurgel.playground.domain.Transaction
import br.com.rodrigogurgel.playground.exception.MapperException
import br.com.rodrigogurgel.playground.`in`.event.dto.MailCommand
import br.com.rodrigogurgel.playground.`in`.event.dto.MailDataCommand
import br.com.rodrigogurgel.playground.`in`.event.dto.MailTypeCommand
import br.com.rodrigogurgel.playground.`in`.event.dto.TransactionCommand
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord

fun Transaction<Mail>.toMailCommand(): Result<GenericRecord, MapperException> = runCatching {
    MailCommand.newBuilder()
        .setTransaction(
            TransactionCommand.newBuilder()
                .setTransactionId(transactionId.toString())
                .setCorrelationId(correlationId.toString())
                .setCreatedBy(createdBy)
                .setCreatedFrom(createdFrom)
                .build()
        )
        .setData(
            data.run {
                MailDataCommand.newBuilder()
                    .setBody(body)
                    .setFrom(from)
                    .setSubject(subject)
                    .setTo(to)
                    .setType(type.toMailTypeCommand())
                    .build()
            }
        )
        .build()
}.mapError { throwable ->
    MapperException(
        "An error occurred while try convert Transaction<Mail> to MailCommand",
        throwable
    )
}

fun MailType.toMailTypeCommand() = when (this) {
    MailType.EMAIL -> MailTypeCommand.EMAIL
    MailType.SMS -> MailTypeCommand.SMS
    MailType.WHATSAPP -> MailTypeCommand.WHATSAPP
}
