package br.com.rodrigogurgel.playground.adapter.out.event.producer

import br.com.rodrigogurgel.playground.adapter.exception.MapperException
import br.com.rodrigogurgel.playground.adapter.exception.ProducerException
import br.com.rodrigogurgel.playground.adapter.mapper.event.toMailCommand
import br.com.rodrigogurgel.playground.adapter.mapper.event.toMailProcessed
import br.com.rodrigogurgel.playground.application.port.out.MailProducerOutputPort
import br.com.rodrigogurgel.playground.domain.entity.Mail
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.future.await
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MailProducer(
    private val kafkaTemplate: KafkaTemplate<String, GenericRecord>,
) : MailProducerOutputPort {
    private val logger = LoggerFactory.getLogger(MailProducer::class.java)

    override suspend fun processed(mail: Mail): Result<Unit, Throwable> = mail.toMailProcessed()
        .andThen { record -> produce("mail-processed", record) }

    override suspend fun command(mail: Mail): Result<Unit, Throwable> = mail.toMailCommand()
        .andThen { record -> produce("mail-command", record) }

    private suspend fun produce(topic: String, record: GenericRecord): Result<Unit, Throwable> =
        runSuspendCatching<Unit> {
            val producerRecord = ProducerRecord(
                topic,
                null,
                null,
                UUID.randomUUID().toString(),
                record,
                null
            )
            kafkaTemplate.send(producerRecord).await()
        }.onSuccess {
            logger.info("Processed message produced with success")
        }.mapError { throwable ->
            when (throwable) {
                is MapperException -> {
                    logger.error(
                        "Something went wrong while try convert to avro",
                        throwable
                    )
                    throwable
                }

                else -> {
                    logger.error("Some error occurred while produce message to topic mail-processed", throwable)
                    ProducerException(null, throwable)
                }
            }
        }
}
