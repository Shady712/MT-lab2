package syntax.regex

data class Concatenation(val left: Regex, val right: Regex) : Regex()