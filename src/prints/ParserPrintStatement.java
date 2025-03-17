package prints;

import expressions.Expression;
import expressions.ParserExpression;
import interpreter.Parser;
import tokens.Token;

public class ParserPrintStatement {

    private final Parser parser;
    private final ParserExpression parserExpression;
    public ParserPrintStatement(Parser parser)
    {
        this.parser = parser;
        this.parserExpression = new ParserExpression(parser);
    }

    public PrintStatement parsePrintStatement() {
        parser.consume(Token.TokenType.KEYWORD);
        parser.consume(Token.TokenType.DELIMITER);
        Expression expression = parserExpression.parseExpression();
        parser.consume(Token.TokenType.DELIMITER);
        parser.consume(Token.TokenType.DELIMITER);
        return new PrintStatement(expression);
    }

}
