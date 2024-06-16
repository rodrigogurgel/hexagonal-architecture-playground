package br.com.rodrigogurgel.playground.adapter.out.datastore

import br.com.rodrigogurgel.playground.application.port.out.MailDatastoreOutputPort
import br.com.rodrigogurgel.playground.domain.entity.Mail
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MailDatastore : MailDatastoreOutputPort {
    override suspend fun findMailById(id: UUID): Result<Mail?, Throwable> = runCatching {
        null
    }
}
