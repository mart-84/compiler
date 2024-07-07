grammar C;

@header {
package fr.martinbonnefoy.compiler.antlr4;
}

prog:
	function? EOF
	;
	
function:
	'int' 'main' OPEN_PAREN CLOSE_PAREN OPEN_CURLY_BRACKET returnInstruction CLOSE_CURLY_BRACKET
	;

returnInstruction:
	RETURN NUMBER SEMICOLON
	;

NUMBER: [0-9]+;

RETURN: 'return';

SEMICOLON: ';';
OPEN_PAREN: '(';
CLOSE_PAREN: ')';
OPEN_CURLY_BRACKET: '{';
CLOSE_CURLY_BRACKET: '}';


COMMENT: '/*' .*? '*/' -> skip ;
SINGLE_LINE_COMMENT: '//' .*? '\n' -> skip ;
DIRECTIVE: '#' .*? '\n' -> skip ;
WS: [ \t\r\n] -> channel(HIDDEN);
