package io.github.theriverelder.util.command

import java.io.PrintWriter

typealias NyarCommandExecutor<E> = NyarCommandContext<E>.(output: PrintWriter) -> Any