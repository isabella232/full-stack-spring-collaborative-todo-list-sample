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

external interface HeaderInputState : State {
    var title: String
}

class HeaderInput : RComponent<HeaderInputProps, HeaderInputState>() {

    override fun componentWillMount() {
        setState {
            title = ""
        }
    }

    override fun RBuilder.render() {
        header(classes = "header") {
            h1 {
                +"todos".translate()
            }
            input(classes = "new-todo", type = InputType.text) {
                attrs {
                    autoFocus = true
                    placeholder = "What needs to be done?".translate()
                    value = state.title

                    onChangeFunction = { event ->
                        val newValue = event.value

                        setState {
                            title = newValue
                        }
                    }

                    onKeyDownFunction = { keyEvent ->
                        val key = Keys.fromString(keyEvent.asDynamic().key as String)

                        if (key == Keys.Enter) {
                            if (state.title.isNotBlank()) {
                                props.create(Todo(title = state.title.trim()))
                            }

                            setState {
                                title = ""
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.headerInput(create: (Todo) -> Unit) = child(HeaderInput::class) {
    attrs.create = create
}
