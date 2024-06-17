package br.com.rodrigogurgel.playground.domain.entity

data class MailData(
    val body: String,
    val from: String,
    val subject: String?,
    val to: String,
) {
    init {
        body.ifBlank { throw IllegalArgumentException("""The field "body" must not be blank""") }
        from.ifBlank { throw IllegalArgumentException("""The field "from" must not be blank""") }
        subject?.ifBlank { throw IllegalArgumentException("""The field "subject" must not be blank if present""") }
        to.ifBlank { throw IllegalArgumentException("""The field "to" must not be blank""") }
    }
}
