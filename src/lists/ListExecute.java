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
        parser.consume(Token.TokenType.KEYWORD);
        String listName = parser.consume(Token.TokenType.IDENTIFIER).getValue();

        List<Expression> elements = new ArrayList<>();


        if (parser.match(Token.TokenType.OPERATOR) && parser.tokens.get(parser.pos).getValue().equals("=")) {
            parser.consume(Token.TokenType.OPERATOR);

            if (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals("[")) {
                throw new RuntimeException("Erro de sintaxe: esperado '[' após '='.");
            }
            parser.consume(Token.TokenType.DELIMITER);

            while (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals("]")) {
                elements.add(expression.parseExpression());

                if (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals(",")) {
                    parser.consume(Token.TokenType.DELIMITER);
                } else if (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals("]")) {
                    break;
                } else {
                    throw new RuntimeException("Erro de sintaxe: elemento inesperado dentro da lista.");
                }
            }
            parser.consume(Token.TokenType.DELIMITER);
        }


        if (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals(";")) {
            throw new RuntimeException("Erro de sintaxe: esperado ';' após declaração da lista.");
        }
        parser.consume(Token.TokenType.DELIMITER);

        // Criando um Token para representar 'list'
        Token listToken = new Token(Token.TokenType.KEYWORD, "list");

        // Retorna a lista com os elementos (vazia se não foi inicializada com `= []`)
        return new VariableDeclaration(listToken, listName, new ListExpression(elements));
    }
}
