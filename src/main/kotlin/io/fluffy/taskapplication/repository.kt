package io.fluffy.taskapplication

import org.springframework.data.r2dbc.core.*
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.stereotype.Component
import java.util.*

@Component
class ToDoRepository( private val client: DatabaseClient) {

    suspend fun init() {
        client.execute("CREATE TABLE IF NOT EXISTS todos (id varchar PRIMARY KEY, title varchar, desc varchar);").await()
        deleteAll()

        data class task( val title:String, val desc:String )

        listOf( task( "Take Out Trash", "Someones gotta do it or it'll stink up the house!"),
                task( "Do Dishes", "lol this is a woman's job!"),
                task( "Walk Dog", "Bro looks bored as shit lol") )
                .map { ToDo(UUID.randomUUID().toString(), it.title, it.desc ) }
                .map { save(it) }
    }

    suspend fun count() = client.execute( "SELECT COUNT(*) FROM todos").asType<Long>().fetch().awaitOne()
    suspend fun findAll() = client.select().from("todos").asType<ToDo>().fetch().flow()
    suspend fun save( todo: ToDo ) = client.insert().into<ToDo>().table("todos").using(todo).await()
    suspend fun deleteAll() = client.execute("DELETE FROM todos").await()
    suspend fun deleteById( id: String ) = client.execute( "DELETE FROM todos WHERE ID=$1 ").bind( "$1", id ).await()
    //suspend fun deleteById( id: String ) = client.delete().from( "todos" ).matching( where("ID").is(id) ).then()
}