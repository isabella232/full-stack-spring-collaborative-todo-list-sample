package com.example.demo

import com.example.demo.ui.app.AppOptions
import com.example.demo.ui.app.app
import com.example.demo.client.RSocketClient
import com.example.demo.client.create
import com.example.demo.service.TodoService
import kotlinext.js.require
import kotlinext.js.requireAll
import kotlinx.browser.document
import react.dom.render
import com.example.demo.repository.LocalStorageTodoRepository
import react.router.dom.BrowserRouter


suspend fun main(args: Array<String>) {
    initStyles()
    AppOptions.language = "en_US"

    val client = RSocketClient.create()
    val service = TodoService(LocalStorageTodoRepository())

    render(document.getElementById("root")) {
        BrowserRouter {
            app(client, service)
        }
    }

}

fun initStyles() {
    requireAll(require.context("", true, js("/\\.css$/")))
    requireAll(require.context("../../../node_modules/todomvc-app-css", true, js("/\\.css$/")))
    requireAll(require.context("../../../node_modules/todomvc-common", true, js("/\\.css$/")))
    requireAll(require.context("../../../node_modules/todomvc-common", true, js("/\\.js$/")))
}
