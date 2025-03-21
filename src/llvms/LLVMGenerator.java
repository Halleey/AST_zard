package llvms;

import expressions.Expression;
import expressions.LiteralExpression;
import expressions.TypedValue;
import interpreter.MainBlock;
import prints.PrintStatement;
import variables.Statement;
import variables.VariableDeclaration;
import variables.VariableReference;
import variables.VariableTable;

public class LLVMGenerator {
    private StringBuilder llvmCode;
    private StringBuilder globalDeclarations;
    private int tempVarCount = 0;
    private final VariableTable variableTable;

    public LLVMGenerator(VariableTable variableTable) {
        this.variableTable = variableTable;
        this.llvmCode = new StringBuilder();
        this.globalDeclarations = new StringBuilder();
        initLLVM();
    }

    private void initLLVM() {
        llvmCode.append("declare i32 @printf(i8*, ...)\n\n");
        llvmCode.append("define i32 @main() {\n");
        llvmCode.append("entry:\n");
    }

    public void generate(MainBlock mainBlock) {
        for (Statement stmt : mainBlock.getStatements()) {
            if (stmt instanceof VariableDeclaration) {
                generateVariableDeclaration((VariableDeclaration) stmt);
            } else if (stmt instanceof PrintStatement) {
                generatePrintStatement((PrintStatement) stmt);
            }
        }
    }

    private void generateVariableDeclaration(VariableDeclaration decl) {
        String varName = "%" + decl.getName();
        String llvmType = getLLVMType(decl.getType().getValue());

        if (decl.getType().getValue().equals("string")) {
            String strConstName = "@str_" + decl.getName();
            String strValue = decl.getValue().toString();

            globalDeclarations.append(strConstName)
                    .append(" = private unnamed_addr constant [")
                    .append(strValue.length() + 1)
                    .append(" x i8] c\"")
                    .append(strValue.replace("\"", "\\22"))
                    .append("\\00\", align 1\n");

            llvmCode.append(varName).append(" = alloca i8*\n");
            llvmCode.append("%ptr_").append(decl.getName())
                    .append(" = getelementptr inbounds [")
                    .append(strValue.length() + 1)
                    .append(" x i8], [")
                    .append(strValue.length() + 1)
                    .append(" x i8]* ")
                    .append(strConstName)
                    .append(", i32 0, i32 0\n");

            llvmCode.append("store i8* %ptr_")
                    .append(decl.getName())
                    .append(", i8** ")
                    .append(varName)
                    .append("\n");
        } else {
            llvmCode.append(varName).append(" = alloca ").append(llvmType).append("\n");
            llvmCode.append("store ").append(llvmType).append(" ").append(decl.getValue().toString())
                    .append(", ").append(llvmType).append("* ").append(varName).append("\n");
        }
    }
    private void generatePrintStatement(PrintStatement stmt) {
        Expression expr = stmt.getExpression();
        String varName;
        String type;

        if (expr instanceof VariableReference varRef) {
            varName = "%" + varRef.getName();
            TypedValue varValue = variableTable.getVariable(varRef.getName());
            if (varValue == null) {
                throw new RuntimeException("Variável não encontrada: " + varRef.getName());
            }
            type = getLLVMType(varValue.getType());
        } else if (expr instanceof LiteralExpression litExpr) {
            varName = "%t" + tempVarCount++;
            type = getLLVMType(litExpr.token.getType().toString());

            String value = litExpr.token.getValue();
            switch (type) {
                case "i32" -> llvmCode.append("    ").append(varName).append(" = add i32 0, ").append(value).append("\n");
                case "double" -> llvmCode.append("    ").append(varName).append(" = fadd double 0.0, ").append(value).append("\n");
                case "i1" -> llvmCode.append("    ").append(varName).append(" = icmp ne i1 ").append(value.equals("true") ? "1" : "0").append(", 0\n");
                case "i8*" -> {
                    String strLabel = "@.str_" + tempVarCount++;
                    globalDeclarations.append(strLabel).append(" = private unnamed_addr constant [")
                            .append(value.length() + 1).append(" x i8] c\"").append(value).append("\\00\", align 1\n");
                    varName = strLabel;
                }
                default -> throw new RuntimeException("Tipo desconhecido: " + type);
            }
        } else {
            throw new RuntimeException("Expressão de impressão inválida: " + expr.getClass().getSimpleName());
        }

        String printVar = "%t" + tempVarCount++;
        String formatPtr;

        if (!type.equals("i8*")) {
            llvmCode.append("    ").append(printVar)
                    .append(" = load ").append(type).append(", ").append(type)
                    .append("* ").append(varName).append("\n");
        } else {
            // Se for string, precisamos de getelementptr para pegar o endereço correto
            String strPtr = "%strptr_" + tempVarCount++;
            llvmCode.append("    ").append(strPtr)
                    .append(" = getelementptr inbounds [").append(varName.length() - 3)
                    .append(" x i8], [").append(varName.length() - 3).append(" x i8]* ")
                    .append(varName).append(", i32 0, i32 0\n");
            printVar = strPtr;
        }

        switch (type) {
            case "i8*" -> formatPtr = "@.str_s";
            case "i32" -> formatPtr = "@.str_d";
            case "double" -> {
                formatPtr = "@.str_f";
            }
            case "i1" -> {
                String boolInt = "%bool_" + tempVarCount++;
                llvmCode.append("    ").append(boolInt).append(" = zext i1 ").append(printVar).append(" to i32\n");
                printVar = boolInt;
                formatPtr = "@.str_d";
            }
            default -> throw new RuntimeException("Tipo desconhecido: " + type);
        }

        llvmCode.append("    call i32 (i8*, ...) @printf(i8* ")
                .append(formatPtr).append(", ").append(type.equals("i1") ? "i32" : type).append(" ").append(printVar).append(")\n");
    }


    private String getLLVMType(String type) {
        return switch (type) {
            case "int" -> "i32";
            case "double" -> "double";
            case "bool" -> "i1";
            case "string" -> "i8*";
            default -> throw new RuntimeException("Tipo desconhecido: " + type);
        };
    }

    public void finalizeCode() {
        llvmCode.append("    ret i32 0\n");
        llvmCode.append("}\n");

        // Adiciona declarações globais de strings de formato
        globalDeclarations.insert(0, """
                    @.str_s = private unnamed_addr constant [3 x i8] c"%s\\00", align 1
                    @.str_d = private unnamed_addr constant [3 x i8] c"%d\\00", align 1
                    @.str_f = private unnamed_addr constant [4 x i8] c"%lf\\00", align 1
                """);

        llvmCode.insert(0, globalDeclarations.toString());
    }

    public String getLLVMCode() {
        return llvmCode.toString();
    }
}
