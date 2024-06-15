package br.com.rodrigogurgel.playground.adapter.out.event.mapper

import br.com.rodrigogurgel.playground.domain.Mail
import br.com.rodrigogurgel.playground.domain.MailType
import br.com.rodrigogurgel.playground.domain.Transaction
import br.com.rodrigogurgel.playground.domain.TransactionStatus
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

fun Transaction<Mail>.toMailProcessed(): Result<GenericRecord, MapperException> = runCatching {
    MailProcessed.newBuilder()
        .setTransaction(
            TransactionProcessed.newBuilder()
                .setTransactionId(transactionId.toString())
                .setCorrelationId(correlationId.toString())
                .setCreatedBy(createdBy)
                .setCreatedFrom(createdFrom)
                .setCreatedAt(createdAt.toEpochMilli())
                .setStatus(status.toTransactionStatusProcessed())
                .setMessage(message)
                .setUpdatedAt(updatedAt.toEpochMilli())
                .build()
        )
        .setData(
            data.run {
                MailDataProcessed.newBuilder()
                    .setBody(body)
                    .setFrom(from)
                    .setSubject(subject)
                    .setTo(to)
                    .setType(type.toMailTypeProcessed())
                    .setSentAt(sentAt?.toEpochMilli())
                    .build()
            }
        )
        .build()
}.mapError { throwable ->
    MapperException(
        "An error occurred while try convert Transaction<Mail> to MailProcessed",
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
