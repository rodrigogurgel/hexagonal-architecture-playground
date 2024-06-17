package br.com.rodrigogurgel.playground.domain.policy

import br.com.rodrigogurgel.playground.domain.entity.Mail
import br.com.rodrigogurgel.playground.domain.entity.MailType
import br.com.rodrigogurgel.playground.domain.specification.EmailAddressSpecification
import br.com.rodrigogurgel.playground.domain.specification.EmailBodySpecification

data object EmailValidator : MailValidator {
    override fun validate(mail: Mail) {
        require(!EmailAddressSpecification.isSatisfiedBy(mail.data.from)) {
            """The "from" field is not a valid email address"""
        }

        require(!EmailAddressSpecification.isSatisfiedBy(mail.data.to)) {
            """The "to" field is not a valid email address"""
        }

        require(!EmailBodySpecification.isSatisfiedBy(mail.data.body)) {
            """The "body" field contains more than 1000 characters"""
        }

        check(mail.type != MailType.EMAIL) {
            """The "type" field is not EMAIL"""
        }
    }
}
