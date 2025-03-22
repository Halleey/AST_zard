package interpreter;
import expressions.ParserExpression;
import ifs.IfParser;
import inputs.ParserInput;
import lists.ListExecute;
import lists.ListParser;
import maps.MapExecute;
import prints.ParserPrintStatement;
import returns.ReturnStatement;
import tokens.Token;
import expressions.Expression;
import variables.*;
import whiles.WhileParser;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public final List<Token> tokens;
    public int pos = 0;
    private final IfParser ifParser;
    private final WhileParser whileParser;
    private final ParserPrintStatement printStatement;
    private final ParserInput parserInput;
    private final ParseVariable parseVariable;
    public final ParserExpression parseExpression;
    private final ListExecute listExecute;
    private final ListParser listParser;
    private final MapExecute mapExecute;


    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.ifParser = new IfParser(this);
        this.whileParser = new WhileParser(this);
        this.printStatement = new ParserPrintStatement(this);
        this.parserInput  = new ParserInput(this);
        this.parseVariable = new ParseVariable(this);
        this.parseExpression = new ParserExpression(this);
        this.listExecute = new ListExecute(this);
        this.listParser = new ListParser(this);
        this.mapExecute = new MapExecute(this);
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (pos < tokens.size()) {
            if (tokens.get(pos).getValue().trim().isEmpty()) {
                pos++;
                continue;
            }
            statements.add(parseStatement());
        }
        return statements;
    }

    private Statement parseStatement() {
        if (match(Token.TokenType.KEYWORD)) {
            String keyword = tokens.get(pos).getValue();

            if ("print".equals(keyword)) {
                return printStatement.parsePrintStatement();
            }
            else if ("return".equals(keyword)) {
                pos++; // Avança após "return"
                Expression returnValue = null;

                if (!match(Token.TokenType.DELIMITER) || !tokens.get(pos).getValue().equals(";")) {
                    returnValue = parseExpression.parseExpression();
                }

                consume(Token.TokenType.DELIMITER); // Consome ";"
                return new ReturnStatement(returnValue);
            }
            else if ("input".equals(keyword)) {
                return parserInput.parseInputStatement();
            }
            else if ("if".equals(keyword)) {
                return ifParser.parseIfStatement();
            }
            else if ("while".equals(keyword)) {
                return whileParser.parseWhileStatement();
            }
            else if ("list".equals(keyword)) {
                return listExecute.ParserListStatement();
            }
            else if ("map".equals(keyword)) {  // Aqui, adicionamos o caso para o mapa
                return mapExecute.ParserMapStatement();  // Usamos o MapParser
            }

            return parseVariable.parseVariableDeclaration();
        }

        if (match(Token.TokenType.IDENTIFIER)) {
            return listParser.parseIdentifierStatement();
        }

        throw new RuntimeException("Erro de sintaxe: declaração inválida em '" + tokens.get(pos).getValue() + "'");
    }



    public List<Statement> parseBlock() {
        List<Statement> statements = new ArrayList<>();
        boolean foundReturn = false; // Flag para detectar um return

        System.out.println("Iniciando parsing do bloco...");

        while (!match(Token.TokenType.DELIMITER) || !tokens.get(pos).getValue().equals("}")) {
            Statement stmt = parseStatement();
            statements.add(stmt);

            // Se for um return, interrompe apenas o loop atual
            if (stmt instanceof ReturnStatement) {
                foundReturn = true; // Marca que um return foi encontrado
                break; // Sai do loop atual
            }
        }

        System.out.println("Fim do bloco encontrado: " + tokens.get(pos).getValue());
        consume(Token.TokenType.DELIMITER); // Consome o "}"

        // Permite que código fora do bloco de ifs/whiles continue
        if (!foundReturn) {
            return statements;
        }

        System.out.println("Retorno encontrado, encerrando execução do bloco atual.");

        return statements;
    }


    public MainBlock parseMainBlock() {
        List<Statement> statements = new ArrayList<>();
        if (!match(Token.TokenType.KEYWORD)) {
            throw new RuntimeException("Erro: O programa deve começar com 'main'!");
        }

        Token mainToken = consume(Token.TokenType.KEYWORD);
        if (!mainToken.getValue().equals("main")) {
            throw new RuntimeException("Erro: O programa deve começar com 'main'!");
        }

        consume(Token.TokenType.DELIMITER);
        while (!match(Token.TokenType.DELIMITER)) {
            statements.add(parseStatement());
        }
        consume(Token.TokenType.DELIMITER);

        return new MainBlock(statements);
    }

    public Token consume(Token.TokenType expectedType) {
        if (pos < tokens.size() && tokens.get(pos).getType() == expectedType) {
            return tokens.get(pos++);
        }
        throw new RuntimeException("Erro de sintaxe: esperado " + expectedType + ", encontrado '" +
                (pos < tokens.size() ? tokens.get(pos).getValue() : "EOF") + "'");
    }

    public boolean match(Token.TokenType type) {
        return pos < tokens.size() && tokens.get(pos).getType() == type;
    }
}