package br.com.rodrigogurgel.playground.adapter.config.strategy

import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.GenericRecordStrategy
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl.EmailStrategyImpl
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl.FallbackStrategyImpl
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl.SmsStrategyImpl
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl.WhatsAppStrategyImpl
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.wrapper.DummyStrategyWrapper
import br.com.rodrigogurgel.playground.domain.usecase.MailUseCase
import br.com.rodrigogurgel.playground.`in`.event.dto.MailCommand
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GenericRecordStrategyConfig {
    @Bean
    @ConditionalOnProperty(value = ["strategy.email.enabled"], havingValue = "true", matchIfMissing = true)
    fun emailEventStrategy(
        @Qualifier("emailInputPort") emailSenderInputPort: MailUseCase,
    ): GenericRecordStrategy<MailCommand> =
        DummyStrategyWrapper(EmailStrategyImpl(emailSenderInputPort))

    @Bean
    @ConditionalOnProperty(value = ["strategy.sms.enabled"], havingValue = "true", matchIfMissing = true)
    fun smsEventStrategy(
        @Qualifier("smsInputPort") smsSenderInputPort: MailUseCase,
    ): GenericRecordStrategy<MailCommand> =
        SmsStrategyImpl(smsSenderInputPort)

    @Bean
    @ConditionalOnProperty(value = ["strategy.whatsapp.enabled"], havingValue = "true", matchIfMissing = true)
    fun whatsAppEventStrategy(
        @Qualifier("whatsAppInputPort") whatsAppSenderInputPort: MailUseCase,
    ): GenericRecordStrategy<MailCommand> =
        DummyStrategyWrapper(WhatsAppStrategyImpl(whatsAppSenderInputPort))

    @Bean
    fun fallbackStrategy(
        @Qualifier("fallbackInputPort") fallbackInputPort: MailUseCase
    ): GenericRecordStrategy<MailCommand> =
        FallbackStrategyImpl(true, fallbackInputPort)
}
