import io.kotest.matchers.shouldBe
import syntax.regex.*

class HappyPathParserTests : ParserTests() {

    init {
        "Empty regex" - {
            listOf(
                "", "()", "\t   \r\n  \n ", "  (  \t )"
            ).map {
                parser.parse(it.byteInputStream()) shouldBe End
            }
        }

        "Single letter regex" {
            parser.parse("x".byteInputStream()) shouldBe Literal('x')
        }

        "Multiple letters" {
            parser.parse("abc".byteInputStream()) shouldBe Concatenation(
                left = Literal('a'),
                right = Concatenation(Literal('b'), Literal('c'))
            )
        }

        "Asterisk" {
            parser.parse("x*".byteInputStream()) shouldBe Asterisk(Literal('x'))
        }

        "Or" {
            parser.parse("a|b".byteInputStream()) shouldBe Or(Literal('a'), Literal('b'))
        }

        "Asterisk on regex" {
            parser.parse("(a|b)*".byteInputStream()) shouldBe Asterisk(Or(Literal('a'), Literal('b')))
        }

        "Or on regex" {
            parser.parse("a*|b".byteInputStream()) shouldBe Or(Asterisk(Literal('a')), Literal('b'))
        }

        "Or with empty left regex" {
            parser.parse("|b".byteInputStream()) shouldBe Or(End, Literal('b'))
        }

        "Or with empty right regex" {
            parser.parse("a|".byteInputStream()) shouldBe Or(Literal('a'), End)
        }

        "Or with empty left and right regexes" {
            parser.parse("|".byteInputStream()) shouldBe Or(End, End)
        }

        "Two ors in a row" {
            parser.parse("a||b".byteInputStream()) shouldBe Or(Literal('a'), Or(End, Literal('b')))
        }

        "Concatenation of regexes" {
            parser.parse("(a|b)c*".byteInputStream()) shouldBe Concatenation(
                Or(Literal('a'), Literal('b')),
                Asterisk(Literal('c'))
            )
        }

        "Sample regex" {
            parser.parse("((abc*b|a)*ab(aa|b*)b)*".byteInputStream()) shouldBe Asterisk(
                Concatenation(
                    left = Asterisk(
                        Or(
                            left = Concatenation(
                                Literal('a'),
                                Concatenation(Literal('b'), Concatenation(Asterisk(Literal('c')), Literal('b')))
                            ),
                            right = Literal('a')
                        )
                    ),
                    right = Concatenation(
                        Literal('a'),
                        Concatenation(
                            Literal('b'), Concatenation(
                                Or(
                                    left = Concatenation(Literal('a'), Literal('a')),
                                    right = Asterisk(Literal('b'))
                                ),
                                Literal('b')
                            )
                        )
                    )
                )
            )

        }
    }
}
