package io.github.theriverelder.util.command.argument

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.data.Game
import io.github.theriverelder.util.command.HintCollection

public typealias ArgumentNodeBuilder = ArgumentNode<Game>.() -> Unit
public typealias ArgumentValidator = (arg: Any) -> String?
public typealias ArgumentProcessor<E> = (env: E, arg: Any, hints: HintCollection) -> Any?

public typealias literal = LiteralArgumentNode<CommandEnv>
public typealias string = StringArgumentNode<CommandEnv>
public typealias number = NumberArgumentNode<CommandEnv>
public typealias dice = DiceArgumentNode<CommandEnv>
public typealias expression = ExpressionArgumentNode<CommandEnv>
public typealias end = EndArgumentParser<CommandEnv>

typealias ArgumentBuilder<E> = ArgumentNode<E>.() -> Unit