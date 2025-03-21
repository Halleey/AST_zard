package llvms;

import interpreter.MainBlock;
import prints.PrintStatement;
import variables.Statement;
import variables.VariableDeclaration;
import variables.VariableReference;

public class LLVMGenerator {
    private StringBuilder llvmCode;
    private int tempVarCount = 0;

    public LLVMGenerator() {
        this.llvmCode = new StringBuilder();
        initLLVM();
    }

    private void initLLVM() {
        llvmCode.append("@.str = private unnamed_addr constant [4 x i8] c\"%s\\00\", align 1\n");
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

        String value = decl.getValue().toString();

        llvmCode.append(varName).append(" = alloca ").append(llvmType).append("\n");
        llvmCode.append("store ").append(llvmType).append(" ").append(value).append(", ").append(llvmType).append("* ").append(varName).append("\n");
    }

    private void generatePrintStatement(PrintStatement stmt) {
        String varName = "%" + ((VariableReference) stmt.getExpression()).getName();
        String printVar = "%t" + tempVarCount++;

        llvmCode.append(printVar).append(" = load i8*, i8** ").append(varName).append("\n");
        llvmCode.append("call i32 (i8*, ...) @printf(i8* ").append(printVar).append(")\n");
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

    public String getLLVMCode() {
        return llvmCode.toString();
    }

}

