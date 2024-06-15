package br.com.rodrigogurgel.playground.application.service

import br.com.rodrigogurgel.playground.application.port.`in`.TransactionInputPort
import br.com.rodrigogurgel.playground.domain.Transaction
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransactionService : TransactionInputPort {
    suspend fun <T> runTransaction(
        transaction: Transaction<T>,
        block: suspend () -> Result<Unit, Throwable>,
    ): Result<Unit, Throwable> = block()
        .onSuccess { transaction.toSuccess() }
        .onFailure { throwable -> transaction.toFailure(throwable.message ?: "Unknown error") }

    override suspend fun <T> findTransactionById(transactionId: UUID): Result<Transaction<T>?, Throwable> =
        runSuspendCatching { null }
}
