package br.com.rodrigogurgel.playground.adapter.mapper.event

import br.com.rodrigogurgel.playground.domain.entities.Mail
import br.com.rodrigogurgel.playground.domain.entities.MailType
import br.com.rodrigogurgel.playground.domain.entities.TransactionStatus
import br.com.rodrigogurgel.playground.exception.MapperException
import br.com.rodrigogurgel.playground.`out`.event.dto.MailDataProcessed
import br.com.rodrigogurgel.playground.`out`.event.dto.MailProcessed
import br.com.rodrigogurgel.playground.`out`.event.dto.MailTypeProcessed
import br.com.rodrigogurgel.playground.`out`.event.dto.TransactionProcessed
import br.com.rodrigogurgel.playground.`out`.event.dto.TransactionStatusProcessed
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import org.apache.avro.generic.GenericRecord

fun Mail.toMailProcessed(): Result<GenericRecord, MapperException> = runCatching {
    MailProcessed.newBuilder()
        .setId(id.toString())
        .setTransaction(
            transaction.run {
                TransactionProcessed.newBuilder()
                    .setCorrelationId(correlationId.toString())
                    .setCreatedBy(createdBy)
                    .setCreatedFrom(createdFrom)
                    .setCreatedAt(createdAt.toEpochMilli())
                    .setStatus(status.toTransactionStatusProcessed())
                    .setMessage(message)
                    .setUpdatedAt(updatedAt.toEpochMilli())
                    .build()
            }
        )
        .setData(
            data.run {
                MailDataProcessed.newBuilder()
                    .setBody(body)
                    .setFrom(from)
                    .setSubject(subject)
                    .setTo(to)
                    .build()
            }
        ).setType(type.toMailTypeProcessed())
        .setSentAt(sentAt?.toEpochMilli())
        .build()
}.mapError { throwable ->
    MapperException(
        "An error occurred while try convert Mai to MailProcessed",
        throwable
    )
}

fun TransactionStatus.toTransactionStatusProcessed(): TransactionStatusProcessed = when (this) {
    TransactionStatus.FAILURE -> TransactionStatusProcessed.FAILURE
    TransactionStatus.SUCCESS -> TransactionStatusProcessed.SUCCESS
    TransactionStatus.PROCESSING -> error("Expected TransactionStatus with value: [SUCCESS, FAILURE]")
}

fun MailType.toMailTypeProcessed(): MailTypeProcessed = when (this) {
    MailType.WHATSAPP -> MailTypeProcessed.WHATSAPP
    MailType.SMS -> MailTypeProcessed.SMS
    MailType.EMAIL -> MailTypeProcessed.EMAIL
}