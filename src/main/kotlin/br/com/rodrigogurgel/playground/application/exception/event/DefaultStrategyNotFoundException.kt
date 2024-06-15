package br.com.rodrigogurgel.playground.application.exception.event

class DefaultStrategyNotFoundException : RuntimeException(
    "No default strategies found! please consider creating a default strategy"
)
