grammar Decompiler;

tokens{ LABEL, DLOAD, DCONST_0, DCMPL, IFLE, ICONST_5, IRETURN, BIPUSH, SVALUE, IVALUE }

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
    |   ireturn
    |   bipush
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

ireturn
    :   IRETURN
    ;

bipush
    :   BIPUSH IVALUE
    ;