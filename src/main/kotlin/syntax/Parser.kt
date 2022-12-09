package syntax

import exception.ParseException
import lex.LexicalAnalyzer
import lex.Token
import syntax.regex.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class Parser {
    private lateinit var lexicalAnalyzer: LexicalAnalyzer

    fun parse(input: InputStream): Regex {
        val baos = ByteArrayOutputStream()
        input.transferTo(baos)
        checkParenthesisSequence(ByteArrayInputStream(baos.toByteArray()))
        lexicalAnalyzer = LexicalAnalyzer(ByteArrayInputStream(baos.toByteArray()))
        lexicalAnalyzer.nextToken()
        return parseOr()
    }

    private fun parseOr(): Regex {
        val left = parseConcatenation()
        val right = parseOrPrime()
        return if (right.isPresent) Or(left, right.get()) else left
    }

    private fun parseConcatenation(): Regex {
        val left = parseAsterisk()
        println(left)
        if (left == End) {
            return left
        }
        return if (curToken() == Token.END) {
            left
        } else {
            val right = parseConcatenation()
            if (right == End) {
                left
            } else {
                Concatenation(left, right)
            }
        }
    }

    private fun parseAsterisk(): Regex {
        val left = parseMin()
        val right = parseAsteriskPrime()
        return if (!right) {
            left
        } else {
            if (left == End) {
                throw ParseException("Missing regex for asterisk", curPos())
            }
            Asterisk(left)
        }
    }

    private fun parseMin(): Regex {
        return when (curToken()) {
            Token.LEFT_PARENTHESIS -> {
                lexicalAnalyzer.nextToken()
                parseRegex().also {
                    if (curToken() != Token.RIGHT_PARENTHESIS) {
                        throw ParseException("Right parenthesis expected", curPos())
                    } else {
                        lexicalAnalyzer.nextToken()
                    }
                }
            }

            Token.OR -> End
            Token.ASTERISK -> End
            Token.RIGHT_PARENTHESIS -> End
            Token.END -> End
            else -> Literal(curToken().value).also {
                lexicalAnalyzer.nextToken()
            }
        }
    }

    private fun parseRegex(): Regex {
        return when (curToken()) {
            Token.END -> End
            else -> {
                val left = parseOr()
                val right = parseConcatenation()
                if (right is End) {
                    left
                } else {
                    Concatenation(left, right)
                }
            }
        }
    }

    private fun parseAsteriskPrime(): Boolean {
        return when (curToken()) {
            Token.ASTERISK -> true.also { lexicalAnalyzer.nextToken() }
            else -> false
        }
    }

    private fun parseOrPrime(): Optional<Regex> {
        return when (curToken()) {
            Token.OR -> {
                lexicalAnalyzer.nextToken()
                Optional.of(parseOr())
            }

            else -> Optional.empty()
        }
    }

    private fun checkParenthesisSequence(input: InputStream) {
        var balance = 0
        input.readAllBytes().toString(Charsets.UTF_8).withIndex().forEach {
            if (it.value == '(') {
                balance++
            } else if (it.value == ')') {
                balance--
                if (balance < 0) {
                    throw ParseException("Missing left parenthesis", it.index)
                }
            }
        }
    }

    private fun curToken() = lexicalAnalyzer.curToken
    private fun curPos() = lexicalAnalyzer.curPos
}