package lists;

import expressions.Expression;
import interpreter.Parser;
import tokens.Token;
import variables.ParseVariable;
import variables.Statement;

import java.util.ArrayList;
import java.util.List;

public class ListParser {

    private final Parser parser;
    private final ParseVariable parseVariable;
    public ListParser(Parser parser) {
        this.parser = parser;
        this.parseVariable = new ParseVariable(parser);
    }

    public Statement parseIdentifierStatement() {
        System.out.println(parser.tokens.get(parser.pos).getValue());
        String varName = parser.consume(Token.TokenType.IDENTIFIER).getValue();

        // Se próximo token for ".", é uma chamada de método
        if (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals(".")) {
            System.out.println(parser.tokens.get(parser.pos).getValue());
            parser.consume(Token.TokenType.DELIMITER); // Consome "."
            System.out.println(parser.tokens.get(parser.pos).getValue());
            String methodName = parser.consume(Token.TokenType.METHODS).getValue();
            System.out.println(parser.tokens.get(parser.pos).getValue());
            // Verifica abertura de parênteses
            if (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals("(")) {
                throw new RuntimeException("Erro de sintaxe: esperado '(' após nome do método.");
            }
            parser.consume(Token.TokenType.DELIMITER); // Consome "("

            List<Expression> arguments = new ArrayList<>();
            if (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals(")")) {
                do {
                    arguments.add(parser.parseExpression.parseExpression());
                } while (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals(",") && parser.consume(Token.TokenType.DELIMITER) != null);
            }

            // Fecha o parêntese ")"
            if (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals(")")) {
                throw new RuntimeException("Erro de sintaxe: esperado ')' no fechamento do método.");
            }
            parser.consume(Token.TokenType.DELIMITER); // Consome ")"

            // Confere ";"
            if (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals(";")) {
                throw new RuntimeException("Erro de sintaxe: esperado ';' após chamada de método.");
            }
            parser.consume(Token.TokenType.DELIMITER); // Consome ";"

            return new ListStatement(varName, methodName, arguments);
        }

        // Caso contrário, tenta interpretar como uma atribuição de variável
        return parseVariable.parseVariableAssignment();
    }

}
