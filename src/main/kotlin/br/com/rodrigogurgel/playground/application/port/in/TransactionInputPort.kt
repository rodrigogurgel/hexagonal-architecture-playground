package br.com.rodrigogurgel.playground.application.port.`in`

import br.com.rodrigogurgel.playground.domain.Transaction
import com.github.michaelbull.result.Result
import java.util.UUID

interface TransactionInputPort {
    suspend fun <T> findTransactionById(transactionId: UUID): Result<Transaction<T>?, Throwable>
}
