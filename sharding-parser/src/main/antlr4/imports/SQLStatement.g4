
grammar SQLStatement;

import Symbol, Keyword, Literals;

use
    : USE schemaName
    ;
    
schemaName
    : identifier
    ;
    
insert
    : INSERT INTO? tableName columnNames? VALUE assignmentValues
    ;
  
assignmentValues
    : LP_ assignmentValue (COMMA_ assignmentValue)* RP_
    ;

assignmentValue
    : identifier
    ;
    
columnNames
    : LP_ columnName (COMMA_ columnName)* RP_
    ;

columnName
    : identifier
    ;
   
tableName
    : identifier
    ;
    
identifier
    : IDENTIFIER_ | STRING_ | NUMBER_
    ;

select
    : SELECT selectElements FROM tableName (whereClause)?
    ;

selectElements
    : (ASTERISK_ | columnName ) (COMMA_ columnName)*
    ;

whereClause
    : WHERE logicExpression
    ;

logicExpression
   : columnName comparisonOperator assignmentValue
   ;

comparisonOperator
    : GT_ | GTE_ | LT_ | LTE_ | EQ_ | NEQ_
    ;