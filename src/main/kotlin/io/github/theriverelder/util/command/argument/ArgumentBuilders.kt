package io.github.theriverelder.util.command.argument

import io.github.theriverelder.data.CommandEnv
import io.github.theriverelder.util.Dice
import io.github.theriverelder.util.command.HintCollection
import io.github.theriverelder.util.command.NyarCommandExecutor

//public typealias ArgumentNodeBuilder = ArgumentNode<Game>.() -> Unit
//public typealias ArgumentValidator = (arg: Any) -> String?
public typealias ArgumentProcessor<E> = (env: E, arg: Any, hints: HintCollection) -> Any?

//public typealias literal = LiteralArgumentNode<CommandEnv>
//public typealias string = StringArgumentNode<CommandEnv>
//public typealias number = NumberArgumentNode<CommandEnv>
//public typealias dice = DiceArgumentNode<CommandEnv>
//public typealias expression = ExpressionArgumentNode<CommandEnv>
//public typealias end = EndArgumentParser<CommandEnv>

typealias ArgumentNodeInitiator<E> = ArgumentNode<E>.() -> Unit

fun <E> command(
    vararg literals: String,
    init: ArgumentNodeInitiator<E>
): LiteralArgumentNode<E> {
    val node = LiteralArgumentNode<E>(literals = literals)
    node.init()
    return node
}

fun <E> ArgumentNode<E>.literal(
    vararg literals: String,
    default: Any? = null,
    processor: ArgumentProcessor<E>? = null,
    init: ArgumentNodeInitiator<E>
): LiteralArgumentNode<E> {
    val node = LiteralArgumentNode<E>(
        literals = literals,
        default = default,
        processor = processor
    )
    node.init()
    this.add(node)
    return node
}

fun <E> ArgumentNode<E>.options(
    key: String,
    vararg literals: String,
    default: Any? = null,
    processor: ArgumentProcessor<E>? = null,
    init: ArgumentNodeInitiator<E>
): LiteralArgumentNode<E> {
    val node = LiteralArgumentNode<E>(
        key = key,
        literals = literals,
        default = default,
        processor = processor
    )
    node.init()
    this.add(node)
    return node
}

fun <E> ArgumentNode<E>.string(
    key: String,
    allowEmpty: Boolean = true,
    default: Any? = null,
    processor: ArgumentProcessor<E>? = null,
    init: ArgumentNodeInitiator<E>
): StringArgumentNode<E> {
    val node = StringArgumentNode<E>(
        key = key,
        allowEmpty = allowEmpty,
        default = default,
        processor = processor
    )
    node.init()
    this.add(node)
    return node
}

fun <E> ArgumentNode<E>.number(
    key: String,
    digit: Boolean = true,
    default: Number? = null,
    processor: ArgumentProcessor<E>? = null,
    init: ArgumentNodeInitiator<E>
): NumberArgumentNode<E> {
    val node = NumberArgumentNode<E>(
        key = key,
        digit = digit,
        default = default,
        processor = processor
    )
    node.init()
    this.add(node)
    return node
}

fun <E> ArgumentNode<E>.dice(
    key: String,
    default: Dice? = null,
    processor: ArgumentProcessor<E>? = null,
    init: ArgumentNodeInitiator<E>
): DiceArgumentNode<E> {
    val node = DiceArgumentNode<E>(
        key = key,
        default = default,
        processor = processor
    )
    node.init()
    this.add(node)
    return node
}

fun <E> ArgumentNode<E>.expression(
    key: String,
    processor: ArgumentProcessor<E>? = null,
    init: ArgumentNodeInitiator<E>
): ExpressionArgumentNode<E> {
    val node = ExpressionArgumentNode<E>(
        key = key,
        processor = processor
    )
    node.init()
    this.add(node)
    return node
}

fun <E> ArgumentNode<E>.end(
    executor: NyarCommandExecutor<E>
): EndArgumentParser<E> {
    val node = EndArgumentParser<E>(executor)
    this.add(node)
    return node
}