package exception

class ParseException(message: String?, private val position: Int) : RuntimeException("$message; position: $position") {

    override fun toString(): String =
        "ParseException on pos $position: $message"
}