# LISP_compiler
The project in the Programing Language course. Code this project by java to avoid stack overflow.(Java will trigger garbage collection)

## LISP 
More imformation in [LISP wiki](https://en.wikipedia.org/wiki/Lisp_(programming_language))

## Sytax
  * LEFT-PAREN -> '('
  * RIGHT-PAREN -> ')'
  * INT  -> e.g., '123', '+123', '-123'
  * STRING  -> "string's (example)." (strings do not extend across lines)
  * DOT  -> '.'
  * FLOAT -> '123.567', '123.', '.567', '+123.4', '-.123'
  * NIL -> 'nil' or '#f', but not 'NIL' nor 'nIL'
  * T -> 't' or '#t', but not 'T' nor '#T'
  * QUOTE -> '
  * SYMBOL -> a consecutive sequence of printable characters that are
                   not numbers, strings, #t or nil, and do not contain 
                   '(', ')', single-quote, double-quote, semi-colon and 
                   white-spaces ; 
                   Symbols are case-sensitive 
                   (i.e., uppercase and lowercase are different);
                   
* 'S-exp' ::= 'ATOM' 
            | LEFT-PAREN 'S-exp' { 'S-exp' } [ DOT 'S-exp' ] RIGHT-PAREN
            | QUOTE 'S-exp'
            
* 'ATOM'  ::= SYMBOL | INT | FLOAT | STRING 
            | NIL | T | LEFT-PAREN RIGHT-PAREN
            
## Parsing Tree
  * (1 . (2 . (3 . 4)))
    * ![123.4](https://github.com/hcsh1112/LISP_compiler/blob/master/img/123.4.jpg)
  * (1 2 3 4)
    * ![1234](https://github.com/hcsh1112/LISP_compiler/blob/master/img/1234.jpg)
  * ((1 2 3) . (4 . (5 . nil)))
    * ![123_4.5](https://github.com/hcsh1112/LISP_compiler/blob/master/img/123_4.5.jpg)
