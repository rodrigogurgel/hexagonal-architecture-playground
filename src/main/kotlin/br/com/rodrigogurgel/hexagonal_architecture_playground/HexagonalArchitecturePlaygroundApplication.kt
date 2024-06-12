package br.com.rodrigogurgel.hexagonal_architecture_playground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HexagonalArchitecturePlaygroundApplication

fun main(args: Array<String>) {
	runApplication<HexagonalArchitecturePlaygroundApplication>(*args)
}
