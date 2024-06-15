package br.com.rodrigogurgel.playground.application.port.out.producer

import br.com.rodrigogurgel.playground.domain.Transaction
import com.github.michaelbull.result.Result

interface ProducerOutputPort<T> {
    suspend fun processed(mail: Transaction<T>): Result<Unit, Throwable>
    suspend fun command(mail: Transaction<T>): Result<Unit, Throwable>
}
