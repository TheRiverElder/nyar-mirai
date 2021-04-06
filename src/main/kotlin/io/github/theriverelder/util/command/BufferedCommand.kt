package io.github.theriverelder.util.command

import java.io.PrintWriter

data class BufferedCommand<E>(val context: NyarCommandContext<E>, val executor: NyarCommandExecutor<E>) {
    fun execute(output: PrintWriter) {
        context.executor(output)
    }
}