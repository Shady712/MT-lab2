import syntax.Parser

fun main() {
    val input = "abc"
    val parser = Parser()
    println(parser.parse(input.byteInputStream()))
}
