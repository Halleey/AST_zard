    @.str_s = private unnamed_addr constant [3 x i8] c"%s\00", align 1
    @.str_d = private unnamed_addr constant [3 x i8] c"%d\00", align 1
    @.str_f = private unnamed_addr constant [4 x i8] c"%lf\00", align 1
@str_texto = private unnamed_addr constant [7 x i8] c"\22zard\22\00", align 1
declare i32 @printf(i8*, ...)

define i32 @main() {
entry:
%a = alloca i32
store i32 15, i32* %a
%b = alloca double
store double 3.14, double* %b
%isReal = alloca i1
store i1 true, i1* %isReal
%texto = alloca i8*
%ptr_texto = getelementptr inbounds [7 x i8], [7 x i8]* @str_texto, i32 0, i32 0
store i8* %ptr_texto, i8** %texto
    %strptr_1 = getelementptr inbounds [3 x i8], [3 x i8]* %texto, i32 0, i32 0
    call i32 (i8*, ...) @printf(i8* @.str_s, i8* %strptr_1)
    %t2 = load i1, i1* %isReal
    %bool_3 = zext i1 %t2 to i32
    call i32 (i8*, ...) @printf(i8* @.str_d, i32 %bool_3)
    %t4 = load i32, i32* %a
    call i32 (i8*, ...) @printf(i8* @.str_d, i32 %t4)
    %t5 = load double, double* %b
    call i32 (i8*, ...) @printf(i8* @.str_f, double %t5)
    ret i32 0
}
