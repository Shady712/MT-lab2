package syntax

import lex.Token
import java.util.concurrent.atomic.AtomicInteger

data class Tree(val value: Token, val children: MutableList<Tree> = mutableListOf()) {
    private val id = nextId.getAndIncrement()

    override fun toString(): String = buildString {
        append("graph Tree {")
        appendLine()
        append(toStringInternal().trimEnd().prependIndent("  "))
        appendLine()
        append("}")
        appendLine()
    }

    private fun toStringInternal(): String =
        buildString {
            append("$id [label=\"$value\"]")
            appendLine()
            val childrenIds = children.map(Tree::id).joinToString(separator = " ")
            append("$id -- {$childrenIds}")
            appendLine()
            children.forEach { append(it.toStringInternal()) }
        }

    companion object {
        val nextId = AtomicInteger(0)

        class NonTerminalToken(value: String) : Token(value)

        val R = NonTerminalToken("R")
        val O = NonTerminalToken("O")
        val A = NonTerminalToken("A")
        val S = NonTerminalToken("S")
    }
}
