package br.com.rodrigogurgel.playground.domain.policy

import br.com.rodrigogurgel.playground.domain.entity.Mail
import br.com.rodrigogurgel.playground.domain.entity.MailType
import br.com.rodrigogurgel.playground.domain.specification.BrazilianPhoneNumberSpecification
import br.com.rodrigogurgel.playground.domain.specification.SmsBodySpecification

data object SmsValidator : MailValidator {
    override fun validate(mail: Mail) {
        require(!BrazilianPhoneNumberSpecification.isSatisfiedBy(mail.data.from)) {
            """The "from" field is not a valid brazilian phone number"""
        }

        require(!BrazilianPhoneNumberSpecification.isSatisfiedBy(mail.data.to)) {
            """The "to" field is not a valid brazilian phone number"""
        }

        require(!SmsBodySpecification.isSatisfiedBy(mail.data.body)) {
            """The "body" field contains more than 1000 characters"""
        }

        check(mail.type != MailType.SMS) {
            """The "type" field is not SMS"""
        }
    }
}
