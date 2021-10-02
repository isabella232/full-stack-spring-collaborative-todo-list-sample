package com.example.demo.ui.components

import com.example.demo.model.Todo
import com.example.demo.model.TodoFilter
import kotlinx.html.js.onDoubleClickFunction
import react.*
import react.dom.li
import react.dom.ul

private val TodoList: FC<TodoListProps> = functionComponent { props ->
    val (editingIdx, setEditingIdx) = useState(-1)

    fun endEditing() {
        setEditingIdx(-1)
    }

    ul(classes = "todo-list") {
        val filter = props.filter

        props.todos.filter { todo ->
            filter.filter(todo)

        }.forEachIndexed { idx, todo ->
            val isEditing = idx == editingIdx

            val classes = when {
                todo.completed -> "completed"
                isEditing -> "editing"
                else -> ""
            }


            li(classes = classes) {
                attrs.onDoubleClickFunction = {
                    setEditingIdx(idx)
                }

                todoItem(
                    todo = todo,
                    editing = isEditing,
                    endEditing = ::endEditing,
                    removeTodo = { props.removeTodo(todo) },
                    updateTodo = { title, completed ->
                        props.updateTodo(todo.copy(title = title, completed = completed))
                    }
                )
            }
        }
    }
}

external interface TodoListProps : Props {
    var removeTodo: (Todo) -> Unit
    var updateTodo: (Todo) -> Unit
    var todos: List<Todo>
    var filter: TodoFilter
}

fun RBuilder.todoList(
    removeTodo: (Todo) -> Unit,
    updateTodo: (Todo) -> Unit,
    todos: List<Todo>,
    filter: TodoFilter,
) = child(TodoList) {
    attrs.todos = todos
    attrs.removeTodo = removeTodo
    attrs.updateTodo = updateTodo
    attrs.filter = filter
}