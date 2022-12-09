package lex


data class Token(val value: Char) {

    companion object {
        val LEFT_PARENTHESIS = Token('(')
        val RIGHT_PARENTHESIS = Token(')')
        val OR = Token('|')
        val ASTERISK = Token('*')
        val END = Token('$')
    }
}
