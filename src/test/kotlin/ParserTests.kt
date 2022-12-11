import io.kotest.core.spec.style.FreeSpec
import lex.Token
import syntax.Parser
import syntax.Tree

open class ParserTests : FreeSpec() {
    protected val parser: Parser = Parser()

    protected fun Tree.addNothingChild() = children.add(Tree(Token.NOTHING))
}
