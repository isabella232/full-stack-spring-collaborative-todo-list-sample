package com.example.demo.ui.components

import com.example.demo.model.TodoFilter
import com.example.demo.utils.pluralize
import kotlinx.html.LI
import kotlinx.html.js.onClickFunction
import react.FC
import react.Props
import react.RBuilder
import react.dom.*
import react.functionComponent

external interface TodoBarProps : Props {
    var pendingCount: Int
    var anyCompleted: Boolean
    var clearCompleted: () -> Unit
    var updateFilter: (TodoFilter) -> Unit
    var currentFilter: TodoFilter
}

private fun RDOMBuilder<LI>.filterItem(props: TodoBarProps, filter: TodoFilter, text: String) {
    val classes = if (props.currentFilter == filter) {
        "selected"
    } else {
        ""
    }

    a(classes = classes) {
        +text
        attrs.onClickFunction = { props.updateFilter(filter) }
    }
}

private val TodoBar: FC<TodoBarProps> = functionComponent { props ->

    footer("footer") {
        span("todo-count") {
            strong { +props.pendingCount.toString() }
            +" "
            +"item left".pluralize(props.pendingCount)
        }

        ul(classes = "filters") {
            li {
                filterItem(props, TodoFilter.ANY, "All")
            }
            span {}

            li {
                filterItem(props, TodoFilter.PENDING, "Active")
            }
            span {}
            li {
                filterItem(props, TodoFilter.COMPLETED, "Completed")
            }
            span {}
        }

        if (props.anyCompleted) {
            button(classes = "clear-completed") {
                +"Clear completed"
                attrs.onClickFunction = { props.clearCompleted() }
            }
        }
    }
}

fun RBuilder.todoBar(
    pendingCount: Int,
    anyCompleted: Boolean,
    clearCompleted: () -> Unit,
    currentFilter: TodoFilter,
    updateFilter: (TodoFilter) -> Unit,
) = child(TodoBar) {
    attrs.pendingCount = pendingCount
    attrs.clearCompleted = clearCompleted
    attrs.anyCompleted = anyCompleted
    attrs.updateFilter = updateFilter
    attrs.currentFilter = currentFilter
}