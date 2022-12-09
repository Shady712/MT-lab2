package syntax.regex

data class Or(val left: Regex, val right: Regex) : Regex()