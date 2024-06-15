package br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.wrapper

import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.GenericRecordStrategy
import br.com.rodrigogurgel.playground.`in`.event.dto.MailCommand
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import org.apache.avro.generic.GenericRecord
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

class DummyStrategyWrapper(
    private val strategy: GenericRecordStrategy<MailCommand>,
) : GenericRecordStrategy<MailCommand> {
    private val logger = LoggerFactory.getLogger(DummyStrategyWrapper::class.java)

    override suspend fun process(value: MailCommand): Result<Unit, Throwable> = runSuspendCatching {
        logger.info("Wrapped up strategy [${strategy::class.qualifiedName}]")
        val timeMillis = measureTimeMillis {
            strategy.process(value)
        }
        logger.info("Mail command processed: $timeMillis ms")
    }

    override fun canProcess(record: GenericRecord): Boolean {
        return strategy.canProcess(record)
    }
}
