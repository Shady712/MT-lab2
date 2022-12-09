package lex

import exception.ParseException
import java.io.IOException
import java.io.InputStream

class LexicalAnalyzer(
    private val input: InputStream
) {
    private var curChar: Int = -1
    var curPos: Int = 0
    lateinit var curToken: Token

    init {
        nextChar()
    }

    fun nextToken() {
        while (curChar.isBlankChar()) {
            nextChar()
        }
        if (curChar == -1) {
            curToken = Token.END
            input.close()
            return
        }
        curToken = when (Char(curChar)) {
            '(' -> Token.LEFT_PARENTHESIS
            ')' -> Token.RIGHT_PARENTHESIS
            '*' -> Token.ASTERISK
            '|' -> Token.OR
            else -> {
                val char = Char(curChar).lowercaseChar()
                if (!char.isLetter()) {
                    throw ParseException("Illegal character '$char'", curPos)
                } else {
                    Token(char)
                }
            }
        }
        nextChar()
    }

    private fun Int.isBlankChar() = this.toChar().isWhitespace()

    private fun nextChar() {
        curPos++
        try {
            curChar = input.read()
        } catch (ex: IOException) {
            throw ParseException(ex.message, curPos)
        }
    }
}