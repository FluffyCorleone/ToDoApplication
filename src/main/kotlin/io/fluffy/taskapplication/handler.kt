package io.fluffy.taskapplication

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.collect
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import java.net.URI
import java.util.*

@Component
class ToDoHandler( private val todoRepository: ToDoRepository ) {

    suspend fun index(request: ServerRequest) =
            ServerResponse.ok().renderAndAwait("home", mapOf( "todos" to todoRepository.findAll() ))

    suspend fun greeting(request: ServerRequest) =
            ServerResponse.ok().renderAndAwait("greeting", mapOf("name" to request.pathVariable("name")))

    suspend fun newTask(request: ServerRequest): ServerResponse {

        val a = request.awaitFormData() ;

        todoRepository.save( ToDo(UUID.randomUUID().toString(), a["field_title"]?.get(0), a["field_desc"]?.get(0) ) )

        return index(request)
    }

    suspend fun deleteTask( request: ServerRequest ): ServerResponse {

        val payload = request.pathVariable( "id")
        println( payload )

        todoRepository.deleteById( payload )
        return ServerResponse.ok().bodyAndAwait( flow { emit( "Deleted $payload" ) } )
    }

    suspend fun redirect( request:ServerRequest): ServerResponse =
        ServerResponse
                .temporaryRedirect( URI.create( "http://www.google.com" ) )
                .buildAndAwait()

}