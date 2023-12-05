grammar Decompiler;

tokens{
LABEL,  DCONST_0, DCMPL, IFLE, ICONST_5,
BIPUSH, SVALUE, IVALUE,
INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE,
LOAD, DLOAD, LDC,
STORE, TYPE,
NEW, DUP, POP,
GETSTATIC, RETURN, IRETURN, UNDEFINED
}

program
    :   (instruction)* EOF
    ;

instruction
    :   label
    |   dload
    |   dconst_0
    |   dcmpl
    |   ifle
    |   iconst_5
    |   return
    |   bipush
    |   invokevirtual
    |   invokespecial
    |   ldc
    |   getstatic
    |   new
    |   store
    |   load
    |   dup
    |   pop
    |   undefined
    ;

label
    :   LABEL SVALUE
    ;

dload
    :   DLOAD IVALUE
    ;

dconst_0
    :   DCONST_0
    ;

dcmpl
    :   DCMPL
    ;

ifle
    :   IFLE LABEL
    ;

iconst_5
    :   ICONST_5
    ;


return
    :   RETURN | IRETURN
    ;

bipush
    :   BIPUSH IVALUE
    ;

invokevirtual
    :   INVOKEVIRTUAL SVALUE SVALUE SVALUE
    ;

invokespecial
    :   INVOKESPECIAL SVALUE SVALUE SVALUE
    ;

ldc
    :   LDC SVALUE
    ;

getstatic
    :   GETSTATIC SVALUE SVALUE SVALUE
    ;

new
    :   NEW SVALUE
    ;

store
    :   STORE TYPE IVALUE
    ;

load
    :   LOAD TYPE IVALUE
    ;

dup
    :   DUP
    ;

pop
    :   POP
    ;

undefined
    :   UNDEFINED (SVALUE)*
    ;
