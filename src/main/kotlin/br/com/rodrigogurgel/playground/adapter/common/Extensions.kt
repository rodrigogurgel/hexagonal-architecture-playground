package br.com.rodrigogurgel.playground.adapter.common

import java.util.UUID

fun CharSequence.toUUID(): UUID = UUID.fromString(this.toString())
