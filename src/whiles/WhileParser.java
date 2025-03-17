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
        parser.consume(Token.TokenType.KEYWORD);  // Consome "while"
        parser.consume(Token.TokenType.DELIMITER); // Consome "("
        Expression condition = expression.parseExpression();  // Obtém a condição
        parser.consume(Token.TokenType.DELIMITER); // Consome ")"
        parser.consume(Token.TokenType.DELIMITER); // Consome "{"

        // Obtém os comandos dentro do bloco while
        List<Statement> statements = parser.parseBlock();
        Block whileBlock = new Block(statements);

        return new WhileStatement(condition, whileBlock);
    }
}
