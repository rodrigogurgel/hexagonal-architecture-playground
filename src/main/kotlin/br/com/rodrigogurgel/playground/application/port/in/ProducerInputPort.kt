package br.com.rodrigogurgel.playground.application.port.`in`

import br.com.rodrigogurgel.playground.domain.Transaction
import com.github.michaelbull.result.Result

interface ProducerInputPort<T> {
    suspend fun produce(mail: Transaction<T>): Result<Unit, Throwable>
}
