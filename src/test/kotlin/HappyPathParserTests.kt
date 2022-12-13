import io.kotest.matchers.shouldBe
import lex.Token
import lex.Token.Companion.ASTERISK
import lex.Token.Companion.NOTHING
import lex.Token.Companion.OR
import syntax.Tree
import syntax.Tree.Companion.A
import syntax.Tree.Companion.O
import syntax.Tree.Companion.R
import syntax.Tree.Companion.S

class HappyPathParserTests : ParserTests() {

    init {
        "Empty regex" - {
            listOf(
                "", "\t   \r\n  \n "
            ).map {
                "Empty regex '$it'" {
                    parser.parse(it.byteInputStream()) shouldBe Tree(NOTHING)
                }
            }
        }

        "Empty regex inside brackets" - {
            listOf("()", "  (  \t )")
                .map {
                    "Empty regex inside brackets '$it'" {
                        parser.parse(it.byteInputStream()) shouldBe Tree(R).apply {
                            children.add(
                                Tree(O).apply {
                                    children.add(
                                        Tree(A).apply {
                                            addNothingChild()
                                            addNothingChild()
                                        }
                                    )
                                    addNothingChild()
                                }
                            )
                            addNothingChild()
                        }
                    }
                }
        }

        "Single letter regex" {
            parser.parse("x".byteInputStream()) shouldBe Tree(R).apply {
                children.add(
                    Tree(O).apply {
                        children.add(
                            Tree(A).apply {
                                children.add(Tree(Token("x")))
                                addNothingChild()
                            }
                        )
                        addNothingChild()
                    }
                )
                addNothingChild()
            }
        }

        "Multiple letters" {
            parser.parse("abc".byteInputStream()) shouldBe Tree(R).apply {
                children.add(
                    Tree(O).apply {
                        children.add(
                            Tree(A).apply {
                                children.add(Tree(Token("a")))
                                addNothingChild()
                            }
                        )
                        addNothingChild()
                    }
                )
                children.add(
                    Tree(R).apply {
                        children.add(
                            Tree(O).apply {
                                children.add(
                                    Tree(A).apply {
                                        children.add(Tree(Token("b")))
                                        addNothingChild()
                                    }
                                )
                                addNothingChild()
                            }
                        )
                        children.add(
                            Tree(R).apply {
                                children.add(
                                    Tree(O).apply {
                                        children.add(
                                            Tree(A).apply {
                                                children.add(Tree(Token("c")))
                                                addNothingChild()
                                            }
                                        )
                                        addNothingChild()
                                    }
                                )
                                addNothingChild()
                            }
                        )
                    }
                )
            }
        }

        "Asterisk" {
            parser.parse("x*".byteInputStream()) shouldBe Tree(R).apply {
                children.add(
                    Tree(O).apply {
                        children.add(
                            Tree(A).apply {
                                children.add(Tree(Token("x")))
                                children.add(Tree(ASTERISK))
                            }
                        )
                        addNothingChild()
                    }
                )
                addNothingChild()
            }
        }

        "Or" {
            parser.parse("a|b".byteInputStream()) shouldBe Tree(R).apply {
                children.add(
                    Tree(O).apply {
                        children.add(
                            Tree(A).apply {
                                children.add(Tree(Token("a")))
                                addNothingChild()
                            }
                        )
                        children.add(
                            Tree(OR).apply {
                                children.add(
                                    Tree(A).apply {
                                        children.add(Tree(Token("b")))
                                        addNothingChild()
                                    }
                                )
                            }
                        )
                    }
                )
                addNothingChild()
            }
        }

        "Asterisk on regex" {
            parser.parse("(a|b)*".byteInputStream()) shouldBe Tree(R).apply {
                children.add(
                    Tree(O).apply {
                        children.add(
                            Tree(A).apply {
                                children.add(
                                    Tree(R).apply {
                                        children.add(
                                            Tree(O).apply {
                                                children.add(
                                                    Tree(A).apply {
                                                        children.add(Tree(Token("a")))
                                                        addNothingChild()
                                                    }
                                                )
                                                children.add(
                                                    Tree(OR).apply {
                                                        children.add(
                                                            Tree(A).apply {
                                                                children.add(Tree(Token("b")))
                                                                addNothingChild()
                                                            }
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                        addNothingChild()
                                    }
                                )
                                children.add(Tree(ASTERISK))
                            }
                        )
                        addNothingChild()
                    }
                )
                addNothingChild()
            }
        }

        "Or on regex" {
            parser.parse("a*|b".byteInputStream()) shouldBe Tree(R).apply {
                children.add(
                    Tree(O).apply {
                        children.add(
                            Tree(A).apply {
                                children.add(Tree(Token("a")))
                                children.add(Tree(ASTERISK))
                            }
                        )
                        children.add(
                            Tree(OR).apply {
                                children.add(
                                    Tree(A).apply {
                                        children.add(Tree(Token("b")))
                                        addNothingChild()
                                    }
                                )
                            }
                        )
                    }
                )
                addNothingChild()
            }
        }

        "Concatenation of regexes" {
            parser.parse("(a|b)c*".byteInputStream()) shouldBe Tree(R).apply {
                children.add(
                    Tree(O).apply {
                        children.add(
                            Tree(A).apply {
                                children.add(
                                    Tree(R).apply {
                                        children.add(
                                            Tree(O).apply {
                                                children.add(
                                                    Tree(A).apply {
                                                        children.add(Tree(Token("a")))
                                                        addNothingChild()
                                                    }
                                                )
                                                children.add(
                                                    Tree(OR).apply {
                                                        children.add(
                                                            Tree(A).apply {
                                                                children.add(Tree(Token("b")))
                                                                addNothingChild()
                                                            }
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                        addNothingChild()
                                    }
                                )
                                addNothingChild()
                            }
                        )
                        addNothingChild()
                    }
                )
                children.add(
                    Tree(R).apply {
                        children.add(
                            Tree(O).apply {
                                children.add(
                                    Tree(A).apply {
                                        children.add(Tree(Token("c")))
                                        children.add(Tree(ASTERISK))
                                    }
                                )
                                addNothingChild()
                            }
                        )
                        addNothingChild()
                    }
                )
            }
        }

        "Sample regex" {
            parser.parse("((abc*b|a)*ab(aa|b*)b)*".byteInputStream()) shouldBe Tree(R).apply {
                children.add(
                    Tree(O).apply {
                        children.add(
                            Tree(A).apply {
                                children.add(
                                    Tree(R).apply {
                                        children.add(
                                            Tree(O).apply {
                                                children.add(
                                                    Tree(A).apply {
                                                        children.add(
                                                            Tree(R).apply {
                                                                children.add(
                                                                    Tree(O).apply {
                                                                        children.add(
                                                                            Tree(A).apply {
                                                                                children.add(Tree(Token("a")))
                                                                                addNothingChild()
                                                                            }
                                                                        )
                                                                        addNothingChild()
                                                                    }
                                                                )
                                                                children.add(
                                                                    Tree(R).apply {
                                                                        children.add(
                                                                            Tree(O).apply {
                                                                                children.add(
                                                                                    Tree(A).apply {
                                                                                        children.add(Tree(Token("b")))
                                                                                        addNothingChild()
                                                                                    }
                                                                                )
                                                                                addNothingChild()
                                                                            }
                                                                        )
                                                                        children.add(
                                                                            Tree(R).apply {
                                                                                children.add(
                                                                                    Tree(O).apply {
                                                                                        children.add(
                                                                                            Tree(A).apply {
                                                                                                children.add(Tree(Token("c")))
                                                                                                children.add(Tree(ASTERISK))
                                                                                            }
                                                                                        )
                                                                                        addNothingChild()
                                                                                    }
                                                                                )
                                                                                children.add(
                                                                                    Tree(R).apply {
                                                                                        children.add(
                                                                                            Tree(O).apply {
                                                                                                children.add(
                                                                                                    Tree(A).apply {
                                                                                                        children.add(Tree(Token("b")))
                                                                                                        addNothingChild()
                                                                                                    }
                                                                                                )
                                                                                                children.add(
                                                                                                    Tree(OR).apply {
                                                                                                        children.add(
                                                                                                            Tree(A).apply {
                                                                                                                children.add(Tree(Token("a")))
                                                                                                                addNothingChild()
                                                                                                            }
                                                                                                        )
                                                                                                    }
                                                                                                )
                                                                                            }
                                                                                        )
                                                                                        addNothingChild()
                                                                                    }
                                                                                )
                                                                            }
                                                                        )
                                                                    }
                                                                )
                                                            }
                                                        )
                                                        children.add(Tree(ASTERISK))
                                                    }
                                                )
                                                addNothingChild()
                                            }
                                        )
                                        children.add(
                                            Tree(R).apply {
                                                children.add(
                                                    Tree(O).apply {
                                                        children.add(
                                                            Tree(A).apply {
                                                                children.add(Tree(Token("a")))
                                                                addNothingChild()
                                                            }
                                                        )
                                                        addNothingChild()
                                                    }
                                                )
                                                children.add(
                                                    Tree(R).apply {
                                                        children.add(
                                                            Tree(O).apply {
                                                                children.add(
                                                                    Tree(A).apply {
                                                                        children.add(Tree(Token("b")))
                                                                        addNothingChild()
                                                                    }
                                                                )
                                                                addNothingChild()
                                                            }
                                                        )
                                                        children.add(
                                                            Tree(R).apply {
                                                                children.add(
                                                                    Tree(O).apply {
                                                                        children.add(
                                                                            Tree(A).apply {
                                                                                children.add(
                                                                                    Tree(R).apply {
                                                                                        children.add(
                                                                                            Tree(O).apply {
                                                                                                children.add(
                                                                                                    Tree(A).apply {
                                                                                                        children.add(Tree(Token("a")))
                                                                                                        addNothingChild()
                                                                                                    }
                                                                                                )
                                                                                                addNothingChild()
                                                                                            }
                                                                                        )
                                                                                        children.add(
                                                                                            Tree(R).apply {
                                                                                                children.add(
                                                                                                    Tree(O).apply {
                                                                                                        children.add(
                                                                                                            Tree(A).apply {
                                                                                                                children.add(Tree(Token("a")))
                                                                                                                addNothingChild()
                                                                                                            }
                                                                                                        )
                                                                                                        children.add(
                                                                                                            Tree(OR).apply {
                                                                                                                children.add(
                                                                                                                    Tree(A).apply {
                                                                                                                        children.add(Tree(Token("b")))
                                                                                                                        children.add(Tree(ASTERISK))
                                                                                                                    }
                                                                                                                )
                                                                                                            }
                                                                                                        )
                                                                                                    }
                                                                                                )
                                                                                                addNothingChild()
                                                                                            }
                                                                                        )
                                                                                    }
                                                                                )
                                                                                addNothingChild()
                                                                            }
                                                                        )
                                                                        addNothingChild()
                                                                    }
                                                                )
                                                                children.add(
                                                                    Tree(R).apply {
                                                                        children.add(
                                                                            Tree(O).apply {
                                                                                children.add(
                                                                                    Tree(A).apply {
                                                                                        children.add(Tree(Token("b")))
                                                                                        addNothingChild()
                                                                                    }
                                                                                )
                                                                                addNothingChild()
                                                                            }
                                                                        )
                                                                        addNothingChild()
                                                                    }
                                                                )
                                                            }
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                                children.add(Tree(ASTERISK))
                            }
                        )
                        addNothingChild()
                    }
                )
                addNothingChild()
            }
        }

        "Sqr pars" {
            parser.parse("[a-x]".byteInputStream()) shouldBe Tree(R).apply {
                children.add(
                    Tree(O).apply {
                        children.add(
                            Tree(A).apply {
                                children.add(
                                    Tree(S).apply {
                                        children.add(Tree(Token("a-x")))
                                    }
                                )
                                addNothingChild()
                            }
                        )
                        addNothingChild()
                    }
                )
                addNothingChild()
            }
        }
    }
}
