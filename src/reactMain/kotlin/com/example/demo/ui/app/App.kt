package com.example.demo.ui.app

import com.example.demo.client.Client
import com.example.demo.model.EventType
import com.example.demo.model.Todo
import com.example.demo.model.TodoEvent
import com.example.demo.model.TodoFilter
import com.example.demo.service.TodoService
import com.example.demo.ui.components.headerInput
import com.example.demo.ui.components.info
import com.example.demo.ui.components.todoBar
import com.example.demo.ui.components.todoList
import com.example.demo.utils.translate
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.title
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.url.URLSearchParams
import react.*
import react.dom.attrs
import react.dom.input
import react.dom.label
import react.dom.section
import react.router.dom.useLocation

object AppOptions {
    var language = "no-language"
    var localStorageKey = "todos-koltin-react"
}

fun useQuery(): URLSearchParams = URLSearchParams(useLocation().search)

private val App: FC<AppProps> = functionComponent { props ->
    val (todos, setTodos) = useState(emptyList<Todo>())
    val query = useQuery()

    useEffect(dependencies = emptyArray()) {
        props.client.handleTodos {
            props.service.handleEvent(it)
            setTodos( props.service.listTodos())
        }
    }

    fun pendingTodos(): List<Todo> {
        return todos.filter { todo -> !todo.completed }
    }

    fun countPending() = pendingTodos().size

    fun removeTodo(todo: Todo) {
        console.log("removeTodo [${todo.id}] ${todo.title}")
        props.client.removeTodo(todo)
        props.service.handleEvent(TodoEvent(EventType.REMOVE, todo))
        setTodos(props.service.listTodos())
    }

    fun createTodo(todo: Todo) {
        console.log("createTodo [${todo.id}] ${todo.title}")

        props.client.addTodo(todo)

        props.service.handleEvent(TodoEvent(EventType.ADD, todo))

        setTodos(props.service.listTodos())
    }

    fun updateTodo(todo: Todo) {
        console.log("updateTodo [${todo.id}] ${todo.title}")

        props.client.updateTodo(todo)

        props.service.handleEvent(TodoEvent(EventType.UPDATE, todo))
        setTodos(props.service.listTodos())
    }

    fun setAllStatus(newStatus: Boolean) {
        todos.forEach { todo -> updateTodo(todo.copy(completed = newStatus)) }
    }

    fun clearCompleted() {
        todos.filter { todo -> todo.completed }
            .forEach { todo -> removeTodo(todo.copy(removed = true)) }
    }

    fun isAllCompleted(): Boolean {
        return todos.fold(true) { allCompleted, todo ->
            allCompleted && todo.completed
        }
    }

    val currentFilter = when (query.get("route")) {
        "pending" -> TodoFilter.PENDING
        "completed" -> TodoFilter.COMPLETED
        else -> TodoFilter.ANY
    }

    section("todoapp") {
        headerInput(::createTodo)


        if (todos.isNotEmpty()) {

            val allChecked = isAllCompleted()

            section("main") {
                input(InputType.checkBox, classes = "toggle-all") {
                    attrs {
                        id = "toggle-all"
                        checked = allChecked

                        onChangeFunction = { event ->
                            val isChecked = (event.currentTarget as HTMLInputElement).checked

                            setAllStatus(isChecked)
                        }
                    }
                }
                label {
                    attrs["htmlFor"] = "toggle-all"
                    attrs.title = "Mark all as complete".translate()
                }

                todoList(::removeTodo, ::updateTodo, todos, currentFilter)
            }

            todoBar(
                pendingCount = countPending(),
                anyCompleted = todos.any { todo -> todo.completed },
                clearCompleted = ::clearCompleted,
                currentFilter = currentFilter,
            )
        }

    }
    info()
}

external interface AppProps : Props {
    var client: Client
    var service: TodoService
}

fun RBuilder.app(client: Client, service: TodoService) = child(App) {
    attrs.client = client
    attrs.service = service
}
