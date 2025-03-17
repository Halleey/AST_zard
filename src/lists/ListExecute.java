package lists;

import expressions.Expression;
import expressions.ParserExpression;
import interpreter.Parser;
import tokens.Token;
import variables.VariableDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ListExecute {


    private final Parser parser;
    private final ParserExpression expression;
    public ListExecute(Parser parser) {
        this.parser = parser;
        this.expression = new ParserExpression(parser);
    }


    public VariableDeclaration ParserListStatement() {
        parser.consume(Token.TokenType.KEYWORD); // Consome "list"
        String listName = parser.consume(Token.TokenType.IDENTIFIER).getValue();

        if (!parser.match(Token.TokenType.OPERATOR) || !parser.tokens.get(parser.pos).getValue().equals("=")) {
            throw new RuntimeException("Erro de sintaxe: esperado '=' após nome da lista.");
        }
        parser.consume(Token.TokenType.OPERATOR);

        if (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals("[")) {
            throw new RuntimeException("Erro de sintaxe: esperado '[' após '='.");
        }
        parser.consume(Token.TokenType.DELIMITER);

        List<Expression> elements = new ArrayList<>();

        while (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals("]")) {
            elements.add(expression.parseExpression());

            // Se houver uma vírgula, consome; senão, verifica se já fechamos a lista
            if (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals(",")) {
                parser.consume(Token.TokenType.DELIMITER);
            } else if (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals("]")) {
                break;
            } else {
                throw new RuntimeException("Erro de sintaxe: elemento inesperado dentro da lista.");
            }
        }

        parser.consume(Token.TokenType.DELIMITER); // Consome "]"

        // Verifica se existe ";" antes de consumir
        if (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals(";")) {
            throw new RuntimeException("Erro de sintaxe: esperado ';' após declaração da lista.");
        }
        parser.consume(Token.TokenType.DELIMITER); // Consome ";"

        // Criando um Token para representar 'list'
        Token listToken = new Token(Token.TokenType.KEYWORD, "list");

        return new VariableDeclaration(listToken, listName, new ListExpression(elements));
    }


}
