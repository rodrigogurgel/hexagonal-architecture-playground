package br.com.rodrigogurgel.playground.adapter.config.strategy

import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.GenericRecordStrategy
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl.EmailStrategyImpl
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl.FallbackStrategyImpl
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl.SmsStrategyImpl
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.impl.WhatsAppStrategyImpl
import br.com.rodrigogurgel.playground.adapter.`in`.event.listener.strategy.wrapper.DummyStrategyWrapper
import br.com.rodrigogurgel.playground.domain.usecase.SendMailUseCase
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
        @Qualifier("emailInputPort") emailSenderInputPort: SendMailUseCase,
    ): GenericRecordStrategy<MailCommand> =
        DummyStrategyWrapper(EmailStrategyImpl(emailSenderInputPort))

    @Bean
    @ConditionalOnProperty(value = ["strategy.sms.enabled"], havingValue = "true", matchIfMissing = true)
    fun smsEventStrategy(
        @Qualifier("smsInputPort") smsSenderInputPort: SendMailUseCase,
    ): GenericRecordStrategy<MailCommand> =
        SmsStrategyImpl(smsSenderInputPort)

    @Bean
    @ConditionalOnProperty(value = ["strategy.whatsapp.enabled"], havingValue = "true", matchIfMissing = true)
    fun whatsAppEventStrategy(
        @Qualifier("whatsAppInputPort") whatsAppSenderInputPort: SendMailUseCase,
    ): GenericRecordStrategy<MailCommand> =
        DummyStrategyWrapper(WhatsAppStrategyImpl(whatsAppSenderInputPort))

    @Bean
    fun fallbackStrategy(
        @Qualifier("fallbackInputPort") fallbackInputPort: SendMailUseCase
    ): GenericRecordStrategy<MailCommand> =
        FallbackStrategyImpl(true, fallbackInputPort)
}
