package ifs;

import expressions.Expression;
import expressions.ParserExpression;
import interpreter.Parser;
import tokens.Token;
import variables.Statement;

import java.util.ArrayList;
import java.util.List;

public class IfParser {
    private final Parser parser;
    private final ParserExpression expression;
    private Block elseBlock = null;
    private List<ConditionBlock> conditionBlocks = new ArrayList<>();

    public IfParser(Parser parser) {
        this.parser = parser;
        this.expression = new ParserExpression(parser);
    }

    public Statement parseIfStatement() {
        parser.consume(Token.TokenType.KEYWORD);  // Consome "if"
        parser.consume(Token.TokenType.DELIMITER); // Consome "("
        Expression condition = expression.parseExpression();  // Parse da condição do if
        parser.consume(Token.TokenType.DELIMITER); // Consome ")"
        parser.consume(Token.TokenType.DELIMITER); // Consome "{"

        // Parse do bloco do if
        List<Statement> ifStatements = parser.parseBlock();
        Block ifBlock = new Block(ifStatements);

        conditionBlocks.add(new ConditionBlock(condition, ifBlock));



        while (parser.match(Token.TokenType.KEYWORD) && parser.tokens.get(parser.pos).getValue().equals("else")) {
            parser.consume(Token.TokenType.KEYWORD); // Consome "else"

            // Verifica se é um else if
            if (parser.match(Token.TokenType.KEYWORD) && parser.tokens.get(parser.pos).getValue().equals("if")) {
                parser.consume(Token.TokenType.KEYWORD);  // Consome "if"
                parser.consume(Token.TokenType.DELIMITER); // Consome "("
                Expression elseIfCondition = expression.parseExpression();  // Condição do else if
                parser.consume(Token.TokenType.DELIMITER); // Consome ")"
                parser.consume(Token.TokenType.DELIMITER); // Consome "{"

                // Parse do bloco do else if
                List<Statement> elseIfStatements = parser.parseBlock();
                Block elseIfBlock = new Block(elseIfStatements);

                // Adiciona um novo ConditionBlock à lista
                conditionBlocks.add(new ConditionBlock(elseIfCondition, elseIfBlock));
            } else {
                parser.consume(Token.TokenType.DELIMITER); // Consome "{"

                // Parse do bloco do else
                List<Statement> elseStatements = parser.parseBlock();
                elseBlock = new Block(elseStatements);
                break; // Sai do loop, pois o else finaliza a estrutura
            }
        }

        return new IfStatement(conditionBlocks, elseBlock);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("IfStatement:\n");

        for (ConditionBlock conditionBlock : conditionBlocks) {
            sb.append("  ").append(conditionBlock).append("\n");
        }

        if (elseBlock != null) {
            sb.append("  Else:\n  ").append(elseBlock).append("\n");
        }

        return sb.toString();
    }

}
