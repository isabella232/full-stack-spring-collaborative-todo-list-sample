package com.example.demo.ui.components

import com.example.demo.utils.translate
import react.FC
import react.Props
import react.RBuilder
import react.dom.a
import react.dom.footer
import react.dom.p
import react.functionComponent

private val Info: FC<Props> = functionComponent {
    footer("info") {
        p { +"Double-click to edit a todo".translate() }
        p {
            +"Created by".translate()
            +" "
            a("https://venturus.org.br/") { +"venturus.org.br" }
        }
        p {
            +"Part of"
            +" "
            a("http://todomvc.com") { +"TodoMVC" }
        }
    }
}

fun RBuilder.info() = child(Info) {}
