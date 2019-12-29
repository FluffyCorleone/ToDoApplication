package io.fluffy.taskapplication

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.support.beans

@SpringBootApplication
class TodoApplication

fun main(args: Array<String>) {
	runApplication<TodoApplication>(*args) {

		val context = beans {
			bean {
				ApplicationListener<ApplicationReadyEvent> {

					val rep = ref<ToDoRepository>()

					runBlocking {

						rep.init()
					}
				}
			}
		}

		this.addInitializers( context )
	}
}