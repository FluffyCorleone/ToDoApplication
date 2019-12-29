package io.fluffy.taskapplication

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfiguration( private val todoHandler: ToDoHandler ) {

    @Bean
    fun mainRouter() = coRouter {
        accept(APPLICATION_JSON).nest {
            GET("/", todoHandler::index )
            GET("/greeting/{name}", todoHandler::greeting )
            GET("/redirect", todoHandler::redirect )
        }

        "/api/tasks".nest {
            POST("/new", todoHandler::newTask )
            DELETE("/delete/{id}", todoHandler::deleteTask)
        }
    }
}