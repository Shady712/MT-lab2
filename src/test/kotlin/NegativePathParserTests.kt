import exception.ParseException
import io.kotest.assertions.throwables.shouldThrow

class NegativePathParserTests : ParserTests() {

    init {
        "Illegal character" {
            testWithParseException("a|,b")
        }

        "Asterisk without regex" {
            testWithParseException("*")
        }

        "Asterisk after or line" {
            testWithParseException("ab|*")
        }

        "Two asterisks in a row" {
            testWithParseException("abc**")
        }

        "Missing right parenthesis" {
            testWithParseException("(a|b")
        }

        "Missing left parenthesis" {
            testWithParseException("a|b)")
        }

        "Missing right parenthesis" {
            testWithParseException("(a|b")
        }
    }

    private fun testWithParseException(input: String) =
        shouldThrow<ParseException> {
            parser.parse(input.byteInputStream())
        }
}