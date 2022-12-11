package lex


open class Token(val value: Char) {

    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    companion object {
        val LEFT_PARENTHESIS = Token('(')
        val RIGHT_PARENTHESIS = Token(')')
        val OR = Token('|')
        val ASTERISK = Token('*')
        val END = Token('$')
        val NOTHING = Token('_')
    }
}
