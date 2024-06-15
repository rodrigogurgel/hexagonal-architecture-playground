package br.com.rodrigogurgel.playground.application.exception.event

import org.apache.avro.generic.GenericRecord

data class MultipleStrategyMatchException(val record: GenericRecord) :
    RuntimeException("Multiple strategy match exception [$record]")
