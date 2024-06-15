package br.com.rodrigogurgel.playground.adapter.`in`.event.mapper

import br.com.rodrigogurgel.playground.adapter.`in`.extension.toUUID
import br.com.rodrigogurgel.playground.domain.Mail
import br.com.rodrigogurgel.playground.domain.MailType
import br.com.rodrigogurgel.playground.domain.Transaction
import br.com.rodrigogurgel.playground.domain.TransactionStatus
import br.com.rodrigogurgel.playground.exception.MapperException
import br.com.rodrigogurgel.playground.`in`.event.dto.MailCommand
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import java.time.Instant

fun MailCommand.toDomain(): Result<Transaction<Mail>, Throwable> = runCatching {
    val mail = data.run {
        Mail(
            body = body.toString(),
            from = from.toString(),
            subject = subject.toString(),
            to = to.toString(),
            type = MailType.valueOf(type.toString()),
            sentAt = null
        )
    }
    transaction.run {
        Transaction(
            transactionId = transactionId.toUUID(),
            correlationId = correlationId.toUUID(),
            status = TransactionStatus.PROCESSING,
            message = null,
            data = mail,
            createdBy = createdBy.toString(),
            createdFrom = createdFrom.toString(),
            createdAt = Instant.now(),
            updatedAt = Instant.now(),

        )
    }
}.mapError { throwable ->
    MapperException(
        "An error occurred while try convert MailCommand to Transaction<Mail>",
        throwable
    )
}
