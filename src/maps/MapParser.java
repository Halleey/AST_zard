package maps;

import expressions.Expression;
import interpreter.Parser;
import tokens.Token;
import variables.Statement;

import java.util.ArrayList;
import java.util.List;

public class MapParser {
    private final Parser parser;

    public MapParser(Parser parser) {
        this.parser = parser;
    }

    public Statement parseIdentifierStatement() {
        String mapName = parser.consume(Token.TokenType.IDENTIFIER).getValue();  // Consome o nome do mapa

        // Verifica se a próxima operação é uma operação de mapa, como "put"
        if (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals(".")) {
            parser.consume(Token.TokenType.DELIMITER); // Consome "."
            String operation = parser.consume(Token.TokenType.METHODS).getValue(); // Consome o nome do método (put, get, etc.)

            // Processa os argumentos para a operação (put, get)
            List<Expression> arguments = new ArrayList<>();
            if (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals("(")) {
                parser.consume(Token.TokenType.DELIMITER); // Consome "("
                do {
                    arguments.add(parser.parseExpression.parseExpression()); // Analisa os argumentos da operação
                } while (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals(","));

                parser.consume(Token.TokenType.DELIMITER); // Consome ")"
            }

            // Cria e retorna um MapStatement com a operação
            return new MapStatement(mapName, operation, arguments);
        }

        throw new RuntimeException("Erro de sintaxe: operação inválida em '" + mapName + "'");
    }
}

