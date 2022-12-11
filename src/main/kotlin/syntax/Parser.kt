package syntax

import exception.ParseException
import lex.LexicalAnalyzer
import lex.Token
import syntax.Tree.Companion.A
import syntax.Tree.Companion.O
import syntax.Tree.Companion.R
import java.io.InputStream

class Parser {
    private lateinit var lexicalAnalyzer: LexicalAnalyzer

    fun parse(input: InputStream): Tree {
        lexicalAnalyzer = LexicalAnalyzer(input)
        lexicalAnalyzer.nextToken()
        return parseRegex().also {
            if (curToken() == Token.NOTHING) {
                lexicalAnalyzer.nextToken()
                if (curToken() != Token.END) {
                    throw unexpectedTokenParseException()
                }
            }
        }
    }

    private fun parseRegex(): Tree {
        println("R")
        return when (curToken()) {
            Token.NOTHING -> Tree(Token.NOTHING)
            Token.END -> Tree(Token.NOTHING)
            else -> Tree(R).apply {
                children.add(parseOr())
                children.add(parseRegex())
            }
        }
    }

    private fun parseOr(): Tree {
        println("O")
        return Tree(O).apply {
            children.add(parseAsterisk())
            children.add(parseOrPrime())
        }
    }

    private fun parseOrPrime(): Tree {
        println("O'")
        return when (curToken()) {
            Token.OR -> {
                lexicalAnalyzer.nextToken()
                Tree(Token.OR).apply {
                    children.add(parseAsterisk())
                }
            }

            else -> Tree(Token.NOTHING)
        }
    }

    private fun parseAsterisk(): Tree {
        println("A")
        return Tree(A).apply {
            children.add(parseMin())
            children.add(parseAsteriskPrime())
        }
    }

    private fun parseAsteriskPrime(): Tree {
        println("A'")
        return when (curToken()) {
            Token.ASTERISK -> Tree(Token.ASTERISK).also { lexicalAnalyzer.nextToken() }
            else -> Tree(Token.NOTHING)
        }
    }

    private fun parseMin(): Tree {
        println("M")
        return when {
            curToken() == Token.LEFT_PARENTHESIS -> {
                lexicalAnalyzer.nextToken()
                val ans = parseRegex()
                lexicalAnalyzer.nextToken()
                if (curToken() != Token.RIGHT_PARENTHESIS) {
                    throw ParseException("Expected right parenthesis", curPos())
                }
                lexicalAnalyzer.nextToken()
                ans
            }

            curToken().value.isLetter() -> Tree(curToken()).also { lexicalAnalyzer.nextToken() }

            else -> throw unexpectedTokenParseException()
        }
    }

    private fun unexpectedTokenParseException() = ParseException("Unexpected token", curPos())

    private fun curToken() = lexicalAnalyzer.curToken.also { println(it) }
    private fun curPos() = lexicalAnalyzer.curPos
}