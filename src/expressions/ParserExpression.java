package expressions;

import interpreter.Parser;
import tokens.Token;
import variables.VariableReference;

public class ParserExpression {


    private final Parser parser;

    public ParserExpression(Parser parser) {
        this.parser = parser;
    }


    public Expression parseExpression() {
        Expression left = parsePrimaryExpression();

        while (parser.match(Token.TokenType.OPERATOR)) {
            Token operator = parser.tokens.get(parser.pos);
            String op = operator.getValue();

            // Verifica se é um operador de comparação
            if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") ||
                    op.equals("==") || op.equals("!=") || op.equals("<") || op.equals("<=") ||
                    op.equals(">") || op.equals(">=")) {

                parser.consume(Token.TokenType.OPERATOR);  // Consome o operador
                Expression right = parsePrimaryExpression();  // A próxima expressão para a direita
                left = new BinaryExpression(left, operator, right);  // Cria a expressão binária
            } else {
                break;  // Sai do loop caso não seja um operador válido
            }
        }

        return left;
    }

    public Expression parsePrimaryExpression() {
        if (parser.match(Token.TokenType.NUMBER)) {
            return new LiteralExpression(parser.consume(Token.TokenType.NUMBER));
        }
        if (parser.match(Token.TokenType.STRING)) {
            return new LiteralExpression(parser.consume(Token.TokenType.STRING));
        }
        if (parser.match(Token.TokenType.BOOLEAN)) {
            return new LiteralExpression(parser.consume(Token.TokenType.BOOLEAN));
        }
        if (parser.match(Token.TokenType.IDENTIFIER)) {
            return new VariableReference(parser.consume(Token.TokenType.IDENTIFIER).getValue());
        }
        throw new RuntimeException("Erro de sintaxe: expressão inesperada em '" + parser.tokens.get(parser.pos).getValue() + "'");
    }


    @Override
    public String toString() {
        return "ParserExpression{" +
                "parser  " + parser +
                '}';
    }
}
