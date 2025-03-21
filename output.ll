@.str = private unnamed_addr constant [4 x i8] c"%s\00", align 1
%a = alloca i32
store i32 15, i32* %a
%b = alloca double
store double 3.14, double* %b
%isReal = alloca i1
store i1 true, i1* %isReal
%texto = alloca i8*
store i8* "zard", i8** %texto
%t0 = load i8*, i8** %texto
call i32 (i8*, ...) @printf(i8* %t0)
