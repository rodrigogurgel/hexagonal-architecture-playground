package br.com.rodrigogurgel.playground.adapter.`in`.extension

import java.util.UUID

fun CharSequence.toUUID(): UUID = UUID.fromString(this.toString())
