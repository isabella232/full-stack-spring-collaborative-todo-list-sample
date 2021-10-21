package com.example.demo.ui.components

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onKeyDownFunction
import com.example.demo.model.Todo
import react.*
import react.dom.attrs
import react.dom.h1
import react.dom.header
import react.dom.input
import com.example.demo.utils.Keys
import com.example.demo.utils.translate
import com.example.demo.utils.value

external interface HeaderInputProps : Props {
    var create: (Todo) -> Unit
}

private val HeaderInput : FC<HeaderInputProps> = functionComponent { props ->
    val (title, setTitle) = useState("")

        header(classes = "header") {
            h1 {
                +"todos".translate()
            }
            input(classes = "new-todo", type = InputType.text) {
                attrs {
                    autoFocus = true
                    placeholder = "What needs to be done?".translate()
                    value = title

                    onChangeFunction = { event ->
                        val newValue = event.value
                        setTitle(newValue)
                    }

                    onKeyDownFunction = { keyEvent ->
                        val key = Keys.fromString(keyEvent.asDynamic().key as String)

                        if (key == Keys.Enter) {
                            if (title.isNotBlank()) {
                                props.create(Todo(title = title.trim()))
                            }
                            setTitle("")
                        }
                    }
                }
            }
        }

}

fun RBuilder.headerInput(create: (Todo) -> Unit) = child(HeaderInput) {
    attrs.create = create
}
