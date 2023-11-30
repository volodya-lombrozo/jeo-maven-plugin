grammar Decompiler;

tokens{ LABEL, DLOAD, DCONST_0, DCMP, IFLE, ICONST_5, IRETURN, BIPUSH }

program
    :   (instruction)* EOF
    ;

instruction
    :   LABEL
    |   DLOAD
    |   DCONST_0
    |   DCMP
    |   IFLE
    |   ICONST_5
    |   IRETURN
    |   BIPUSH
    ;
