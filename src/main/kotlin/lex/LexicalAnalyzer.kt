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

    var isInsideSqrPar: Boolean = false

    fun nextToken() {
        while (curChar.isBlankChar()) {
            nextChar()
        }
        if (curChar == -1) {
            if (!this::curToken.isInitialized || curToken == Token.NOTHING) {
                curToken = Token.END
                input.close()
            } else {
                curToken = Token.NOTHING
            }
            return
        }
        if (isInsideSqrPar) {
            curToken = Token.RIGHT_SQR_PAR
            isInsideSqrPar = false
            nextChar()
            return
        }
        if (this::curToken.isInitialized && curToken == Token.LEFT_SQR_PAR) {
            val sb = StringBuilder()
            isInsideSqrPar = true
            while (Char(curChar) != ']') {
                val ch = Char(curChar)
                if (!ch.isLetter() && ch != '-') {
                    throw ParseException("Unexpected char '$ch'", curPos)
                }
                sb.append(ch)
                nextChar()
                if (curChar == -1) {
                    throw ParseException("Expected ']', but reached end of input", curPos)
                }
            }
            curToken = Token(sb.toString())
            return
        }
        curToken = when (Char(curChar)) {
            '(' -> Token.LEFT_PARENTHESIS
            ')' -> if (curToken == Token.NOTHING) Token.RIGHT_PARENTHESIS else Token.NOTHING
            '*' -> Token.ASTERISK
            '|' -> Token.OR
            '[' -> Token.LEFT_SQR_PAR
            ']' -> Token.RIGHT_SQR_PAR
            else -> {
                val char = Char(curChar).lowercaseChar()
                if (!char.isLetter()) {
                    throw ParseException("Illegal character '$char'", curPos)
                } else {
                    Token(char.toString())
                }
            }
        }
        nextChar()
    }

    private fun Int.isBlankChar() = this.toChar().isWhitespace()

    private fun nextChar() {
        if (this::curToken.isInitialized && curToken == Token.NOTHING) return
        curPos++
        try {
            curChar = input.read()
        } catch (ex: IOException) {
            throw ParseException(ex.message, curPos)
        }
    }
}