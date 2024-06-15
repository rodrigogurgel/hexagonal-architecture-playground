package br.com.rodrigogurgel.playground.application.exception.event

import org.apache.avro.generic.GenericRecord

data class StrategyNotFoundException(val record: GenericRecord) : RuntimeException(
    "No strategy found for record [$record]"
)
