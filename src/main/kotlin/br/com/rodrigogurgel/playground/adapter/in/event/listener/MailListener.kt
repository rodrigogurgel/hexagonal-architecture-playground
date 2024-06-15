package br.com.rodrigogurgel.playground.adapter.`in`.event.listener

import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.GenericRecordStrategy
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.extension.default
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.extension.findStrategy
import br.com.rodrigogurgel.playground.application.exception.event.DefaultStrategyNotFoundException
import br.com.rodrigogurgel.playground.application.exception.event.MultipleStrategyMatchException
import br.com.rodrigogurgel.playground.application.exception.event.StrategyNotFoundException
import com.github.michaelbull.result.andThenRecoverIf
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.runBlocking
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class MailListener(
    private val processors: List<GenericRecordStrategy<GenericRecord>>,
) {
    private val logger = LoggerFactory.getLogger(MailListener::class.java)

    @KafkaListener(
        id = "playground",
        idIsGroup = true,
        autoStartup = "true",
        topics = ["mail-command"],
        containerFactory = "mailCommandContainerFactory"
    )
    fun onMessage(
        consumerRecords: List<ConsumerRecord<String, GenericRecord>>,
        acknowledgment: Acknowledgment,
    ) {
        consumerRecords.parallelStream().forEach { consumerRecord ->
            runBlocking {
                val record = consumerRecord.value()
                val (strategy) = processors.findStrategy(record)
                    .onFailure { throwable ->
                        when (throwable) {
                            is MultipleStrategyMatchException, is StrategyNotFoundException,
                            -> logger.warn(throwable.message)

                            else -> logger.error("Uncaught exception during find strategy", throwable)
                        }
                    }
                    .onSuccess { logger.info("Using strategy [${it::class.qualifiedName}]") }
                    .andThenRecoverIf({ throwable -> throwable is StrategyNotFoundException }) {
                        processors.default()
                    }.onFailure { throwable ->
                        when (throwable) {
                            is DefaultStrategyNotFoundException -> logger.warn(throwable.message)
                            else -> logger.error("Uncaught exception during find default strategy", throwable)
                        }
                    }

                strategy?.process(record)
            }
        }

        acknowledgment.acknowledge()
    }
}
