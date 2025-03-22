package maps;

import expressions.Expression;
import expressions.ParserExpression;
import interpreter.Parser;
import tokens.Token;
import variables.VariableDeclaration;

import java.util.HashMap;
import java.util.Map;

public class MapExecute {
    private final Parser parser;
    private final ParserExpression expression;

    public MapExecute(Parser parser) {
        this.parser = parser;
        this.expression = new ParserExpression(parser);
    }

    public VariableDeclaration ParserMapStatement() {
        // Consome a palavra-chave "map"
        parser.consume(Token.TokenType.KEYWORD);
        String mapName = parser.consume(Token.TokenType.IDENTIFIER).getValue();

        Map<Expression, Expression> elements = new HashMap<>();

        // Verifica se há uma atribuição de mapa, i.e., "mapName = { key: value, ... }"
        if (parser.match(Token.TokenType.OPERATOR) && parser.tokens.get(parser.pos).getValue().equals("=")) {
            parser.consume(Token.TokenType.OPERATOR);

            if (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals("{")) {
                throw new RuntimeException("Erro de sintaxe: esperado '{' após '='.");
            }
            parser.consume(Token.TokenType.DELIMITER);

            // Analisa os elementos chave-valor dentro do mapa
            while (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals("}")) {
                Expression key = expression.parseExpression();
                parser.consume(Token.TokenType.OPERATOR); // Consome o operador ':'
                Expression value = expression.parseExpression();
                elements.put(key, value);

                if (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals(",")) {
                    parser.consume(Token.TokenType.DELIMITER);
                } else if (parser.match(Token.TokenType.DELIMITER) && parser.tokens.get(parser.pos).getValue().equals("}")) {
                    break;
                } else {
                    throw new RuntimeException("Erro de sintaxe: elemento inesperado dentro do mapa.");
                }
            }
            parser.consume(Token.TokenType.DELIMITER);
        }

        // Consome o delimitador de fim (;) após a declaração do mapa
        if (!parser.match(Token.TokenType.DELIMITER) || !parser.tokens.get(parser.pos).getValue().equals(";")) {
            throw new RuntimeException("Erro de sintaxe: esperado ';' após declaração do mapa.");
        }
        parser.consume(Token.TokenType.DELIMITER);

        // Criando um Token para representar 'map'
        Token mapToken = new Token(Token.TokenType.KEYWORD, "map");

        // Retorna a variável com o mapa (vazio se não foi inicializado)
        return new VariableDeclaration(mapToken, mapName, new MapExpression(elements));
    }
}

