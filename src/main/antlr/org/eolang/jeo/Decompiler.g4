grammar Decompiler;

tokens{
LABEL, DLOAD, DCONST_0, DCMPL, IFLE, ICONST_5,
 BIPUSH, SVALUE, IVALUE, INVOKEVIRTUAL, LDC,
GETSTATIC, RETURN, IRETURN
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
    |   ldc
    |   getstatic
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

ldc
    :   LDC SVALUE
    ;

getstatic
    :   GETSTATIC SVALUE SVALUE SVALUE
    ;
