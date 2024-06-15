package br.com.rodrigogurgel.playground.adapter.config.metrics

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MeterRegistryConfig {
    @Bean
    fun timedAspect(registry: MeterRegistry) = TimedAspect(registry)
}
