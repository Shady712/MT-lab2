import syntax.Parser
import java.io.File

fun main() {
    val input = "[a-x]*|b[xd]"
    val parser = Parser()
    val tree = parser.parse(input.byteInputStream())
    println(tree)
    File("./tree.dot").writeText(tree.toString())
}
