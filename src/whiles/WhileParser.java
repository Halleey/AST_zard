package whiles;

import expressions.Expression;
import expressions.ParserExpression;
import ifs.Block;
import interpreter.Parser;
import tokens.Token;
import variables.Statement;

import java.util.List;

public class WhileParser {
    private final Parser parser;
    private final ParserExpression expression;
    public WhileParser(Parser parser) {
        this.parser = parser;
        this.expression = new ParserExpression(parser);
    }

    public Statement parseWhileStatement() {
        parser.consume(Token.TokenType.KEYWORD);
        parser.consume(Token.TokenType.DELIMITER);
        Expression condition = expression.parseExpression();
        parser.consume(Token.TokenType.DELIMITER);
        parser.consume(Token.TokenType.DELIMITER);

        // Obtém os comandos dentro do bloco while
        List<Statement> statements = parser.parseBlock();
        Block whileBlock = new Block(statements);

        return new WhileStatement(condition, whileBlock);
    }
}
