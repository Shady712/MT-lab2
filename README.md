## Отчёт 
Recursive descending parser for regular expressions.
For example: "(abc)|(de)*"

| Terminal  |    Description    |
|:---------:|:-----------------:|
|   char    |    any letter     |
|     (     | left parenthesis  |
|     )     | right parenthesis |
|    Or     |   Vertical line   |
|     *     |  Klini operation  |
|     $     |        End        |
|     _     |      Nothing      |
| char-char |    Chars range    |
|   chars   |       Chars       |


| NonTerminal |   First    |        Follow        |          Description           |
|:-----------:|:----------:|:--------------------:|:------------------------------:|
|      R      | char, (, _ |         ), $         |         Correct regex          |
|      O      |  char, (   |    char, (, ), $     |         Part before VL         |
|     O'      |   VL, _    |    char, (, ), $     | (if present) VL and part after |
|      A      |  char, (   |  VL, char, (, ), $   |         Part before *          |
|     A'      |    *, _    |  VL, char, (, ), $   |         (if present) *         |
|      M      |  char, (   | *, VL, char, (, ), $ |     Minimal correct regex      |

#### Правила грамматики
* R &rarr; OR
* R &rarr; _
* O &rarr; AO'
* O' &rarr; |A
* O' &rarr; _
* A &rarr; MA'
* A' &rarr; *
* A' &rarr; _
* M &rarr; (R)
* M &rarr; char
* M &rarr; S
* S -> chars / char-char