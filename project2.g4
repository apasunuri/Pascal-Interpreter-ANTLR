grammar project2;

start: program EOF;

program:
    program_name (program_variables)? (functions|procedures)* program_block
;

procedures:
    PROCEDURE ID OPENPAREN (params)? CLOSEPAREN SEMI
    (function_variables)?
    function_block
;

functions:
    FUNCTION ID OPENPAREN (params)? CLOSEPAREN COLON TYPE SEMI
    (function_variables)?
    function_block
;

function_variables:
    VAR vars
;

function_block:
    BEGIN function_statements END SEMI
;

params:
    param_list (SEMI param_list)*
; 

param_list:
    ID (COMMA ID)* COLON TYPE
; 

program_name:
    PROGRAM ID SEMI
;

program_variables:
    VAR vars
;

vars:
    var (SEMI var)* SEMI
; 

var:
    ID (COMMA ID)* COLON TYPE
; 

program_block:
    BEGIN program_statements END DOT
;

function_statements:
    function_statement (SEMI function_statement)*
;

program_statements:
    program_statement (SEMI program_statement)*
;

program_statement:
    assignment  
    | conditional   
    | r_case    
    | function_call 
    | for_loop  
    | while_loop    
    | writeln   
    | readln   
;

function_statement:
    assignment  
    | conditional
    | function_call    
    | r_case    
    | for_loop  
    | while_loop
    | writeln
    | readln
;

function_call:
    ID OPENPAREN (args)? CLOSEPAREN
; 

args:
    arg (COMMA arg)* 
; 

arg:
    expr
;

for_loop:
    (loop_variables)?
    FOR (VAR)? ID ASSIGNMENT index TO index DO
    loop_block
;

index:
    ID  #indexId
    | REAL_NUM  #indexRealNum
;

while_loop:
    (loop_variables)?
    WHILE condition DO
    loop_block
;

loop_variables:
    VAR vars
;

loop_block:
    BEGIN loop_statements END   #loopStatementsBlock
    | loop_statement    #loopStatementBlock
; 

loop_statements:
    loop_statement (SEMI loop_statement)*
;

loop_statement:
    assignment  #loopAssignment
    | conditional_loop  #loopConditional
    | r_case_loop   #loopCase
    | function_call #loopFunctionCall
    | writeln   #loopWriteln
    | readln    #loopReadln
    | for_loop  #loopForLoop
    | while_loop    #loopWhileLoop
    | BREAK #loopBreak
    | CONTINUE  #loopContinue
;

conditional_loop:
    IF 
    condition 
    THEN
    conditional_block_loop
    ELSE
    conditional_block_loop
;

block_statement_loop:
    assignment  #conditionalLoopAssignment
    | function_call #conditionalLoopAssignment
    | for_loop  #conditionalLoopForLoop
    | while_loop    #conditionalLoopWhileLoop
    | writeln   #conditionalLoopWritelnLoop
    | readln    #conditionalLoopReadlnLoop
    | BREAK #conditionalLoopBreakLoop
    | CONTINUE  #conditionalLoopContinueLoop
;

block_statements_loop:
    (block_statement_loop (SEMI block_statement_loop)*)?
;

conditional_block_loop:
    block_statement_loop    #conditionalBlockStatementLoop
    | BEGIN block_statements_loop END   #conditionalBlockStatementsLoop  
;

r_case_loop:
    R_CASE case_expression OF
    (case_label 
    COLON
    conditional_block_loop
    SEMI)+
    END
;

assignment:
    ID ASSIGNMENT OPENPAREN expr CLOSEPAREN
    | ID ASSIGNMENT expr
;

expr:
    arithmetic_expr #arithmeticExpr
    | logical_expr  #logicalExpr
    | relational_expr   #relationalExprStart
;

relational_expr:
    OPENPAREN relational_expr CLOSEPAREN    #relationalExprParen
    | arithmetic_expr EQUAL arithmetic_expr #equalExpr
    | arithmetic_expr NOT_EQUAL arithmetic_expr #notEqualExprArithmetic
    | arithmetic_expr LESS_THAN arithmetic_expr #lessThanExpr
    | arithmetic_expr LESS_EQUAL arithmetic_expr    #lessEqualExpr
    | arithmetic_expr GREATER_THAN arithmetic_expr  #greaterThanExpr
    | arithmetic_expr GREATER_EQUAL arithmetic_expr #greaterEqualExpr
;     

logical_expr:
    OPENPAREN logical_expr CLOSEPAREN   #logicalExprParen
    | NOT OPENPAREN logical_expr CLOSEPAREN #notLogicalExprParen
    | NOT logical_expr  #notExpr
    | logical_expr AND logical_expr #andExpr
    | logical_expr OR logical_expr  #orExpr
    | logical_expr EQUAL logical_expr   #equalExprLogical
    | logical_expr NOT_EQUAL logical_expr   #notEqualExprLogical
    | relational_expr   #relationalExpr
    | function_call #functionCallLogicalExpr
    | TRUE  #trueExpr
    | FALSE #falseExpr
    | ID    #IdExprLogical
; 

arithmetic_expr: 
    OPENPAREN arithmetic_expr CLOSEPAREN    #arithmeticExprParen
    | SQRT OPENPAREN arithmetic_expr CLOSEPAREN #sqrtExpr
    | SIN OPENPAREN arithmetic_expr CLOSEPAREN  #sinExpr
    | COS OPENPAREN arithmetic_expr CLOSEPAREN  #cosExpr
    | LN OPENPAREN arithmetic_expr CLOSEPAREN   #lnExpr
    | EXP OPENPAREN arithmetic_expr CLOSEPAREN  #expExpr
    | arithmetic_expr MULTIPLICATION arithmetic_expr    #multiplicationExpr
    | arithmetic_expr DIVISION arithmetic_expr  #divisionExpr
    | arithmetic_expr ADDITION arithmetic_expr  #additionExpr
    | arithmetic_expr SUBTRACTION arithmetic_expr   #subtractionExpr
    | function_call #functionCallArithmeticExpr
    | REAL_NUM  #realNumExpr
    | ID    #IdExprArithmetic
;

condition:
    OPENPAREN condition CLOSEPAREN  #conditionParen
    | logical_expr  #conditionLogicalExpr
    | relational_expr   #conditionRelationExpr
;

block_statement:
    assignment
    | function_call
    | for_loop
    | while_loop
    | writeln
    | readln 
;

block_statements:
    (block_statement (SEMI block_statement)*)?
;

conditional_block:
    block_statement
    | BEGIN block_statements END
;

conditional:
    IF 
    condition 
    THEN
    conditional_block
    ELSE
    conditional_block
;

case_expression:
    OPENPAREN case_expression CLOSEPAREN    #caseExpressionParen
    | ID    #caseExpressionId
    | REAL_NUM  #caseExpressionRealNum
    | TRUE  #caseExpressionTrue
    | FALSE #caseExpressionFalse
;

case_label:
    REAL_NUM    #caseLabelRealNum
    | TRUE  #caseLabelTrue
    | FALSE #caseLabelFalse
; 

r_case:
    R_CASE case_expression OF
    (case_label 
    COLON
    conditional_block
    SEMI)+
    END
;

value:
    ID #valueId
    | arithmetic_expr #valueArithmetic
    | logical_expr #valueLogical
    | relational_expr #valueRelational
    | function_call #valueFunction
    | STR #valueStr
;

writeln:
    WRITELN (OPENPAREN (value)? CLOSEPAREN)?
;

readln:
    READLN (OPENPAREN (ID)? CLOSEPAREN)?
;

PROGRAM: P R O G R A M;
VAR: V A R;
FUNCTION: F U N C T I O N;
PROCEDURE: P R O C E D U R E;
TYPE: B O O L E A N|R E A L;
BOOLEAN: B O O L E A N;
REAL: R E A L; 
BEGIN: B E G I N;
END: E N D;
IF: I F;
THEN: T H E N;
ELSE: E L S E;
TRUE: T R U E;
FALSE: F A L S E;
R_CASE: C A S E;
OF: O F;
FOR: F O R;
WHILE: W H I L E;
BREAK: B R E A K;
CONTINUE: C O N T I N U E;
TO: T O;
DO: D O;
WRITELN: W R I T E L N;
READLN: R E A D L N;
ASSIGNMENT: ':=';
DOT: '.';
COMMA: ',';
QUOTE: '\'';
OPENPAREN: '(';
CLOSEPAREN: ')';
SEMI: ';';
COLON: ':';
EQUAL: '=';
NOT_EQUAL: '<>';
LESS_THAN: '<';
LESS_EQUAL: '<=';
GREATER_THAN: '>';
GREATER_EQUAL: '>=';
ADDITION: '+';
SUBTRACTION: '-';
MULTIPLICATION: '*';
DIVISION: '/';
AND: A N D;
OR: O R;
NOT: N O T;
SQRT: S Q R T;
SIN: S I N;
COS: C O S;
LN: L N;
EXP: E X P;
ID: [A-Za-z][_A-Za-z0-9]*;
REAL_NUM: [-+]?[0-9]+('.'[0-9]+)?;
STR: '\'' .*? '\'';
WS: [ \t\r\n]+ -> skip;
COMMENT: '(*' .*? '*)' -> skip;

fragment A : [aA];
fragment B : [bB]; 
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];