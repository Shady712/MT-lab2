import syntax.Parser
import java.io.File

fun main() {
    val input = "((abc*b|a)*ab(aa|b*)b)*"
    val parser = Parser()
    val tree = parser.parse(input.byteInputStream())
    println(tree)
    File("./tree.dot").writeText(tree.toString())
}
