package interpreter;
import llvms.LLVMGenerator;
import tokens.Lexer;
import tokens.Token;
import variables.VariableTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class ZardInterpreter {
    public static void main(String[] args) {
        try {
            VariableTable variableTable = new VariableTable();
            // Ler o arquivo de código Zard
            String code = new String(Files.readAllBytes(Paths.get("src/language/main.zd")));

            // Tokenizar código
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.tokenize();

            // Construir AST
            Parser parser = new Parser(tokens);
            MainBlock mainBlock = parser.parseMainBlock(); // Agora analisamos um MainBlock

            // Debug: Exibir a AST gerada
            System.out.println("AST Gerada:");
            System.out.println(mainBlock);

            // Gerar uma saída de TEXTO para a AST
            Files.write(Paths.get("ast_output.txt"), mainBlock.toString().getBytes());

            //Para executar o código da AST
            mainBlock.execute(variableTable);

            // Geração do código LLVM
            LLVMGenerator llvmGenerator = new LLVMGenerator(variableTable);
            llvmGenerator.generate(mainBlock);
            llvmGenerator.finalizeCode();
            System.out.println(llvmGenerator.getLLVMCode());
            // Salvar o código LLVM em um arquivo
            String llvmOutputPath = "output.ll";
            Files.write(Paths.get(llvmOutputPath), llvmGenerator.getLLVMCode().getBytes());

            System.out.println("Código LLVM gerado e salvo em: " + llvmOutputPath);

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro durante a execução: " + e.getMessage());
            e.printStackTrace(); // Exibir detalhes do erro para depuração
        }
    }
}

