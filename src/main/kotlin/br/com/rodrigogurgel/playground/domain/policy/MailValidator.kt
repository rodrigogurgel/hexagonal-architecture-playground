package br.com.rodrigogurgel.playground.domain.policy

import br.com.rodrigogurgel.playground.domain.entity.Mail

sealed interface MailValidator {
    fun validate(mail: Mail)
}
