package com.example.demo.ui.components

import react.RBuilder
import react.RComponent
import react.dom.*
import com.example.demo.utils.translate
import react.Props
import react.State

class Info: RComponent<Props, State>() {

    override fun RBuilder.render() {
        footer("info") {
            p { + "Double-click to edit a todo".translate() }
            p {
                + "Created by".translate()
                + " "
                a("https://venturus.org.br/") { + "venturus.org.br" }
            }
            p {
                + "Part of"
                + " "
                a("http://todomvc.com") { + "TodoMVC" }
            }
        }
    }
}

fun RBuilder.info() = child(Info::class) {}
